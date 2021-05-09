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
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.Color;
import java.util.ArrayList;

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
        this.clientGUI.showGui();
        
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
            clientGUI.addLog("Agente registrado en el DF.", 0);
        } catch(FIPAException e){
            // En caso de excepción se muestra un mensaje:
            clientGUI.addLog("Error registrándose en DF: " + e.getMessage(), 0);
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
        // Creamos el objeto:
        DesiredBook newBook = new DesiredBook(title, price);
        // Lo añadimos al array:
        desiredBooks.add(newBook);
        // Informamos del final:
        clientGUI.addLog("Añadido interés por el libro " + newBook.getBookName()
                + " con un tope de " + newBook.getMaxPrice() + "€.", 0);
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
                                if(db.getBookName().equals(of.getAuctionRound().getAuction().getBook())){
                                    // Si el precio ofrecido es menor que el precio que se está dispuesto a pagar... Se acepta.
                                    if(db.getMaxPrice() >= of.getAuctionRound().getRoundPrice()){
                                        // Se manda una respuesta aceptando el precio:
                                        ACLMessage resp = msg.createReply();
                                        // Se usa el AgentAction bid (puja):
                                        Bid bid = new Bid();
                                        // Se asocia el concept auctionround, indicando el precio que se acepta.
                                        bid.setAuctionRound(of.getAuctionRound());
                                        // Se rellena el mensaje:
                                        getContentManager().fillContent(resp, new Action(myAgent.getAID(), bid));
                                        // La performativa es un propose:
                                        resp.setPerformative(ACLMessage.PROPOSE);
                                        // Enviamos la respuesta pertinente:
                                        myAgent.send(resp);
                                        clientGUI.addLog("Pujando " + of.getAuctionRound().getRoundPrice() 
                                                + "€ en la subasta del libro " + of.getAuctionRound().getAuction().getBook() 
                                                + " del agente " + a.getActor().getName(), 0);
                                        // Se sale del bucle:
                                        break;
                                    }
                                }
                            }   
                            // Notificamos a la interfaz la llegada de esta subasta:
                            clientGUI.addTableRow(new AuctionClientData(of.getAuctionRound().getAuction().getAuctionID(),
                                        of.getAuctionRound().getAuction().getBook(),
                                        of.getAuctionRound().getRoundPrice(),
                                        of.getAuctionRound().getAuction().getActualWinner(),
                                        a.getActor().getName(),
                                        AuctionState.EN_PROGRESO));
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            // Si rechazan nuestra solicitud, notificaremos a la interfaz para que muestre al ganador de la ronda:
                            EndRound er = (EndRound) a.getAction();
                            clientGUI.addLog("Perdida puja por " + er.getAuctionRound().getRoundPrice() 
                                    + "€ en la subasta del libro " + er.getAuctionRound().getAuction().getBook() 
                                    + " del agente " + a.getActor().getName(), 0);
                            
                            // Notificamos a la interfaz la llegada de esta subasta:
                            clientGUI.addTableRow(new AuctionClientData(er.getAuctionRound().getAuction().getAuctionID(),
                                        er.getAuctionRound().getAuction().getBook(),
                                        er.getAuctionRound().getRoundPrice(),
                                        er.getAuctionRound().getAuction().getActualWinner(),
                                        a.getActor().getName(),
                                        AuctionState.EN_PROGRESO));
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            // Si rechazan nuestra solicitud, notificaremos a la interfaz para que muestre al ganador de la ronda:
                            EndRound endR = (EndRound) a.getAction();
                            clientGUI.addLog("Ganada puja por " + endR.getAuctionRound().getRoundPrice() 
                                    + "€ en la subasta del libro " + endR.getAuctionRound().getAuction().getBook() 
                                    + " del agente " + a.getActor().getName(), 0);
                            
                            // Notificamos a la interfaz la llegada de esta subasta:
                            clientGUI.addTableRow(new AuctionClientData(endR.getAuctionRound().getAuction().getAuctionID(),
                                        endR.getAuctionRound().getAuction().getBook(),
                                        endR.getAuctionRound().getRoundPrice(),
                                        endR.getAuctionRound().getAuction().getActualWinner(),
                                        a.getActor().getName(),
                                        AuctionState.EN_PROGRESO));
                            break;
                        case ACLMessage.INFORM:
                            // Se ha perdido esta subasta, por lo que se avisa y se mantiene el libro dentro de los deseados:
                            EndAuction ea = (EndAuction) a.getAction();
                            // Marcaremos que la subasta ha sido cancelada si nadie ha participado
                            AuctionState as = ea.getAuction().getActualWinner()==null 
                                    || ea.getAuction().getActualWinner().isEmpty() 
                                    ? AuctionState.CANCELADA : AuctionState.PERDIDA;
                            // Actualizamos:
                            clientGUI.addTableRow(new AuctionClientData(ea.getAuction().getAuctionID(),
                                        ea.getAuction().getBook(),
                                        ea.getAuction().getActualPrice(),
                                        ea.getAuction().getActualWinner(),
                                        a.getActor().getName(),
                                        as));
                            // Informamos del final:
                            clientGUI.addLog("Perdida subasta del libro " + ea.getAuction().getBook() 
                                    + " del subastador " + a.getActor().getName(), 1);
                            break;
                        case ACLMessage.REQUEST:
                            EndAuction endAuc = (EndAuction) a.getAction();
                            // Se ha ganado esta subasta, por lo que se avisa a la interfaz y se elimina el libro de los deseados:
                            desiredBooks.removeIf(book -> book.getBookName().equals(endAuc.getAuction().getBook()));
                            clientGUI.addTableRow(new AuctionClientData(endAuc.getAuction().getAuctionID(),
                                        endAuc.getAuction().getBook(),
                                        endAuc.getAuction().getActualPrice(),
                                        endAuc.getAuction().getActualWinner(),
                                        a.getActor().getName(),
                                        AuctionState.GANADA));
                            // Informamos del final
                            clientGUI.addLog("Ganada subasta del libro " + endAuc.getAuction().getBook() 
                                    + " del subastador " + a.getActor().getName() + " por " + endAuc.getAuction().getActualPrice() + "€.", 2);
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
