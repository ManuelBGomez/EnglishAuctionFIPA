/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Client;

import P6_ComDis.ontologia.AuctionOntology;
import P6_ComDis.ontologia.Bid;
import P6_ComDis.ontologia.EndAuction;
import P6_ComDis.ontologia.EndRound;
import P6_ComDis.ontologia.Offer;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa al participante de una subasta
 * @author Manuel Bendaña
 */
public class AuctionParticipantAgent extends Agent {
    
    private ClientGUI clientGUI;
    private MessageTemplate mt;
    private ArrayList<DesiredBook> desiredBooks;
    // Atributos para gestionar las ontologías:
    private Codec codec;
    private Ontology onto;
        
    /**
     * Método de inicialización del agente.
     */
    @Override
    public void setup() {
        // Se imprime un mensaje que marca el comienzo del agente:
        System.out.println("Agente "+getAID().getName()+" comenzando.");
        
        // Inicialización:
        this.desiredBooks = new ArrayList<>();
        this.clientGUI = new ClientGUI(this);
        this.clientGUI.showGui("Cliente " + getAID().getName());
        
        // Instanciamos codec (SL):
        this.codec = new SLCodec();
        
        // Recuperamos instancia de la ontología:
        this.onto = AuctionOntology.getInstance();
        
        // Registramos lenguaje y ontología:
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(onto);
        
        // Nos registramos en el servicio DF:
        
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        
        // El tipo de servicio es "Auction"
        sd.setType("auction");
        // Nombre + propietario:
        sd.setName(getName());
        sd.setOwnership("ComDisP6");
        // Se establece AID en el dfd y los servicios:
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try {
            // Procedemos al registro:
            DFService.register(this, dfd);
            clientGUI.addLog("Agente registrado en el DF.");
        } catch(FIPAException e){
            // En caso de excepción se muestra un mensaje:
            clientGUI.addLog("Error registrándose en DF: " + e.getMessage());
        }
        
        // Definimos plantilla de recepción de los mensajes:
        mt = MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()), 
                MessageTemplate.MatchLanguage(codec.getName()));
        
        // Añadimos behaviour de recepción de mensajes:
        this.addBehaviour(new ManageAuctioneersRequests());
    }

    /**
     * Método que permite añadir un libro deseado por el cliente.
     * @param title El nombre del libro que se desea adquirir.
     * @param price El precio máximo que se está dispuesto a pagar.
     */
    void addDesiredBook(String title, Float price) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Creamos el objeto:
                DesiredBook newBook = new DesiredBook(title, price);
                // Lo añadimos al array:
                desiredBooks.add(newBook);
                // Informamos del final:
                clientGUI.addLog("Añadido interés por el libro " + newBook.getBookName()
                        + " con un tope de " + newBook.getMaxPrice() + "€.");
            }
        });
    }
    
    /**
     * Método de finalización del agente.
     */
    @Override
    public void takeDown() {
        try {
            // Nos salimos del DF:
            DFService.deregister(this);
        } catch (FIPAException ex) {
            // Excepción lanzada en caso de producirse algún problema:
            System.out.println("Error en el derregistro del comprador: " + ex.getMessage());
        }
        // Imprimimos mensaje de cierre
        System.out.println("Agente "+getAID().getName()+" acabando.");
    }
    
    /**
     * Comportamiento cíclico que permite gestionar las solicitudes de los 
     * subastadores.
     */
    private class ManageAuctioneersRequests extends CyclicBehaviour {
        @Override
        public void action() {
            // Intentamos reibir el mensaje:
            ACLMessage msg = myAgent.receive(mt);
            // Si el mensaje es nulo, bloqueamos:
            if (msg == null) {
                block();
            } else {
                try {
                    // Extraemos el contenido del mensaje:
                    Action a = (Action) getContentManager().extractContent(msg);
                    // Comprobamos el tipo del performativa:
                    switch (msg.getPerformative()) {
                        case ACLMessage.CFP:
                            // Si se manda una oferta, se mira si interesa y se envía respuesta en ese caso:
                            Offer of = (Offer) a.getAction();
                            // Se procesa si tiene sentido:
                            for(DesiredBook db: desiredBooks) {
                                if(db.getBookName().equals(of.getBookOffer().getBookInfo().getBName())){
                                    // Notificamos a la interfaz la llegada de esta subasta:
                                    clientGUI.addTableRow(new AuctionClientData(of.getBookOffer().getAuctionId(),
                                                of.getBookOffer().getBookInfo().getBName(),
                                                of.getBookOffer().getPrice(),
                                                null,
                                                a.getActor().getName(),
                                                AuctionState.EN_PROGRESO));
                                    // Si el precio ofrecido es menor que el precio que se está dispuesto a pagar... Se acepta.
                                    if(db.getMaxPrice() >= of.getBookOffer().getPrice()){
                                        // Se manda una respuesta aceptando el precio:
                                        ACLMessage resp = msg.createReply();
                                        // Se usa el AgentAction bid (puja):
                                        Bid bid = new Bid();
                                        // Se asocia el concept auctionround, indicando el precio que se acepta.
                                        bid.setBookOffer(of.getBookOffer());
                                        // Se rellena el mensaje:
                                        getContentManager().fillContent(resp, new Action(myAgent.getAID(), bid));
                                        // La performativa es un propose:
                                        resp.setPerformative(ACLMessage.PROPOSE);
                                        // Enviamos la respuesta pertinente:
                                        myAgent.send(resp);
                                        clientGUI.addLog("Pujando " + of.getBookOffer().getPrice() 
                                                + "€ en la subasta del libro " + of.getBookOffer().getBookInfo().getBName()
                                                + " del agente " + a.getActor().getName());
                                        // Se sale del bucle:
                                        break;
                                    }
                                }
                            }
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            // Si rechazan nuestra solicitud, notificaremos a la interfaz para que muestre al ganador de la ronda:
                            EndRound er = (EndRound) a.getAction();
                            clientGUI.addLog("Perdida puja por " + er.getLastOffer().getPrice() 
                                    + "€ en la subasta del libro " + er.getLastOffer().getBookInfo().getBName() 
                                    + " del agente " + a.getActor().getName());
                            
                            // Notificamos a la interfaz la llegada de esta subasta:
                            clientGUI.addTableRow(new AuctionClientData(er.getLastOffer().getAuctionId(),
                                        er.getLastOffer().getBookInfo().getBName(),
                                        er.getLastOffer().getPrice(),
                                        er.getWinner().getName(),
                                        a.getActor().getName(),
                                        AuctionState.EN_PROGRESO));
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            // Si rechazan nuestra solicitud, notificaremos a la interfaz para que muestre al ganador de la ronda:
                            EndRound endR = (EndRound) a.getAction();
                            clientGUI.addLog("Ganada puja por " + endR.getLastOffer().getPrice() 
                                    + "€ en la subasta del libro " + endR.getLastOffer().getBookInfo().getBName() 
                                    + " del agente " + a.getActor().getName());
                            
                            // Notificamos a la interfaz la llegada de esta subasta:
                            clientGUI.addTableRow(new AuctionClientData(endR.getLastOffer().getAuctionId(),
                                        endR.getLastOffer().getBookInfo().getBName(),
                                        endR.getLastOffer().getPrice(),
                                        endR.getWinner().getName(),
                                        a.getActor().getName(),
                                        AuctionState.EN_PROGRESO));
                            break;
                        case ACLMessage.INFORM:
                            // Se ha perdido esta subasta, por lo que se avisa y se mantiene el libro dentro de los deseados:
                            EndAuction ea = (EndAuction) a.getAction();
                            // Comprobamos si es un libro en el que realmente ahora mismo estamos interesados:
                            for(DesiredBook db: desiredBooks) {
                                if(db.getBookName().equals(ea.getLastOffer().getBookInfo().getBName())) {
                                    // Marcaremos que la subasta ha sido cancelada si nadie ha participado
                                    AuctionState as = ea.getAuctionWinner()==null
                                            ? AuctionState.CANCELADA : AuctionState.PERDIDA;
                                    // Actualizamos:
                                    clientGUI.addTableRow(new AuctionClientData(ea.getLastOffer().getAuctionId(),
                                                ea.getLastOffer().getBookInfo().getBName(),
                                                ea.getLastOffer().getPrice(),
                                                ea.getAuctionWinner() != null ? ea.getAuctionWinner().getName() : "-",
                                                a.getActor().getName(),
                                                as));
                                    // Informamos del final:
                                    clientGUI.addLog("Perdida subasta del libro " + ea.getLastOffer().getBookInfo().getBName() 
                                            + " del subastador " + a.getActor().getName());
                                    break;
                                }
                            }
                            break;
                        case ACLMessage.REQUEST:
                            EndAuction endAuc = (EndAuction) a.getAction();
                            // Se ha ganado esta subasta, por lo que se avisa a la interfaz y se elimina el libro de los deseados:
                            desiredBooks.removeIf(book -> book.getBookName().equals(endAuc.getLastOffer().getBookInfo().getBName()));
                            clientGUI.addTableRow(new AuctionClientData(endAuc.getLastOffer().getAuctionId(),
                                        endAuc.getLastOffer().getBookInfo().getBName(),
                                        endAuc.getLastOffer().getPrice(),
                                        endAuc.getAuctionWinner().getName(),
                                        a.getActor().getName(),
                                        AuctionState.GANADA));
                            // Informamos del final
                            clientGUI.addLog("Ganada subasta del libro " + endAuc.getLastOffer().getBookInfo().getBName() 
                                    + " del subastador " + a.getActor().getName() + " por " + endAuc.getLastOffer().getPrice() + "€.");
                            break;
                        default:
                            break;
                    }
                } catch (Codec.CodecException | OntologyException ex) {
                    System.out.println("Error recibiendo el mensaje: " + ex.getMessage());
                }
            }
        }
        
    }
    
}
