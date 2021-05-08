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
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
        
        // Instanciamos codec (SL):
        this.codec = new SLCodec();
        
        // Recuperamos instancia de la ontología:
        this.onto = AuctionOntology.getInstance();
        
        // Registramos lenguaje y ontología:
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(onto);
        
        // Definimos plantilla de recepción de los mensajes:
        mt = MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()), 
                MessageTemplate.MatchLanguage(codec.getName()));
        
        // Añadimos behaviour de recepción de mensajes:
        this.addBehaviour(new ManageAuctioneersRequests());
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
                                    if(db.getMaxPrice() > of.getAuctionRound().getRoundPrice()){
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
                                        // Se sale del bucle:
                                        break;
                                    }
                                }
                            }   break;
                        case ACLMessage.REJECT_PROPOSAL:
                        case ACLMessage.ACCEPT_PROPOSAL:
                            // Si rechazan nuestra solicitud, notificaremos a la interfaz para que muestre al ganador de la ronda:
                            EndRound er = (EndRound) a.getAction();
                            break;
                        case ACLMessage.INFORM:
                            {
                                // Se ha perdido esta subasta, por lo que se avisa y se mantiene el libro dentro de los deseados:
                                EndAuction ea = (EndAuction) a.getAction();
                                break;
                            }
                        case ACLMessage.REQUEST:
                            {
                                EndAuction ea = (EndAuction) a.getAction();
                                // Se ha ganado esta subasta, por lo que se avisa a la interfaz y se elimina el libro de los deseados:
                                desiredBooks.removeIf(book -> book.getBookName().equals(ea.getAuction().getBook()));
                                break;
                            }
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
