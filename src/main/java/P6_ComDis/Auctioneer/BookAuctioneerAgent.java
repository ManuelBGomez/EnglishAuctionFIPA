package P6_ComDis.Auctioneer;

import P6_ComDis.ontologia.Auction;
import P6_ComDis.ontologia.AuctionOntology;
import P6_ComDis.ontologia.AuctionRound;
import P6_ComDis.ontologia.Bid;
import P6_ComDis.ontologia.EndAuction;
import P6_ComDis.ontologia.EndRound;
import P6_ComDis.ontologia.Offer;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que representa al agente subastador.
 * @author Manuel Bendaña
 */
public class BookAuctioneerAgent extends Agent {
    private static final Integer INTERVAL = 10000;
    private HashMap<Integer, AuctionData> activeAuctions;
    private ArrayList<AID> clients;
    private int numAuctions;
    private AuctioneerGUI auctioneerGUI;
    private MessageTemplate mt;
    
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
        this.numAuctions = 0;
        this.activeAuctions = new HashMap<>();
        this.auctioneerGUI = new AuctioneerGUI(this);
        this.auctioneerGUI.showGui("Subastador " + getAID().getName());
        
        // Instanciamos codec (SL):
        this.codec = new SLCodec();
        
        // Recuperamos instancia de la ontología:
        this.onto = AuctionOntology.getInstance();
        
        // Registramos lenguaje y ontología:
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(onto);
        
        // Añadimos un tickerBehaviour que consulta los agentes disponibles:
        this.addBehaviour(new TickerBehaviour(this, INTERVAL) {
            @Override
            public void onTick() {
                System.out.println("Recuperando clientes disponibles");
                
                // Usaremos este tickerbehaviour para actualizar la lista de compradores:
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                
                sd.setType("auction");
                
                // Asociamos a la DFAgentDescription el servicio de subastas.
                // LOs vendedores interesados se añadirán:
                template.addServices(sd);
                
                // Hacemos la búsqueda de los agentes:
                try {
                    // Introducimos este agente y la plantilla elaborada previamente:
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    
                    clients = new ArrayList<>();
                    
                    // Para cada agente encontrado se recupera su AID:
                    for (DFAgentDescription result1 : result) {
                        clients.add(result1.getName());
                    }
                } catch (FIPAException fe) {
                    // Si se captura una excepción, se avisa de ello:
                    System.out.println("Error al recuperar los agentes: " + fe.getMessage());
                }
                
                activeAuctions.entrySet().stream().map(entries -> entries.getValue()).forEachOrdered(auction -> {
                    // Si se ha marcado el booleano de terminada, no se hace nada en esta iteración (la subasta ya estará terminada):
                    if(!auction.getIsFinished()){
                        // Se comprueba si ha podido terminar la subasta:
                        if(auction.isAuctionFinished()){
                            // Si ha terminado, enviaremos un mensaje de final de subasta a todos los usuarios:
                            Auction ac = new Auction();
                            // El precio final puede variar en función de si gana un participante de la ronda actual o de la previa:
                            if(auction.getRoundParticipants().isEmpty()) ac.setActualPrice(auction.getLastRoundPrice());
                            else ac.setActualPrice(auction.getPrice());
                            ac.setActualWinner(auction.getRoundWinner());
                            ac.setAuctionID(auction.getId());
                            ac.setBook(auction.getProductName());
                            // Se usa un agentaction específico de la finalización:
                            EndAuction ea = new EndAuction();
                            ea.setAuction(ac);

                            // Dos mensajes inform y request al finalizar una subasta: uno para el ganador y otro para el resto:
                            ACLMessage inf = new ACLMessage(ACLMessage.INFORM);
                            inf.setOntology(onto.getName());
                            inf.setLanguage(codec.getName());
                            ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                            req.setOntology(onto.getName());
                            req.setLanguage(codec.getName());

                            try {
                                // Creado el agentaction y los concepts necesarios, se introducen en el mensaje:
                                getContentManager().fillContent(inf, new Action(getAID(), ea));
                                getContentManager().fillContent(req, new Action(getAID(), ea));
                            } catch (CodecException | OntologyException ex){
                                System.out.println("Error enviando mensajes inform y request: " + ex.getMessage());
                            }

                            // Iteramos por todos los participantes:
                            clients.forEach(client -> {
                               if(client.getName().equals(ac.getActualWinner())){
                                   // Ganador:
                                   req.addReceiver(client);
                               } else {
                                   // Otro cliente:
                                   inf.addReceiver(client);
                               }
                            });

                            // Se envían los mensajes:
                            myAgent.send(req);
                            myAgent.send(inf);

                            // Se establece el fin de la subasta:
                            auction.setIsFinished(true);
                        } else {
                            // Lo primero es devolver una respuesta a los interesados de la ronda anterior:
                            // Para cada subasta, miraremos si hubo ronda previa y actuaremos en consecuencia.
                            if(auction.getNumRound() != 0) {
                                // En caso de tener una ronda actual mayor que 0, lo que se hará será notificar el resultado de la ronda:
                                Auction ac = new Auction();
                                ac.setActualPrice(auction.getPrice());
                                ac.setActualWinner(auction.getRoundWinner());
                                ac.setAuctionID(auction.getId());
                                ac.setBook(auction.getProductName());
                                AuctionRound ar = new AuctionRound();
                                ar.setAuction(ac);
                                ar.setRoundPrice(auction.getPrice());
                                EndRound er = new EndRound();
                                er.setAuctionRound(ar);

                                // Mensajes accept-proposal y reject-proposal para ganadores/perdedores de una ronda:
                                ACLMessage ap = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                                ap.setOntology(onto.getName());
                                ap.setLanguage(codec.getName());
                                ACLMessage rp = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                                rp.setOntology(onto.getName());
                                rp.setLanguage(codec.getName());

                                try {
                                    // Creado el agentaction y los concepts necesarios, se introducen en el mensaje:
                                    getContentManager().fillContent(ap, new Action(getAID(), er));
                                    getContentManager().fillContent(rp, new Action(getAID(), er));
                                } catch (CodecException | OntologyException ex){
                                    System.out.println("Error enviando mensajes accept y reject proposal: " + ex.getMessage());
                                }

                                // Enviaremos el accept proposal al agente ganador de esta ronda, evidentemente:
                                auction.getRoundParticipants().forEach(client -> {
                                    if(client.getName().equals(ac.getActualWinner())){
                                        // Ganador:
                                        ap.addReceiver(client);
                                    } else {
                                        // Otro participante - reject:
                                        rp.addReceiver(client);
                                    }
                                });

                                // Enviamos mensajes y avanzamos de ronda:
                                myAgent.send(ap);
                                myAgent.send(rp);

                            }
                            auction.nextRound();
                            
                            // Hecho lo anterior, se recuperan los datos y se envían las peticiones pertinentes:
                            // A cada usuario, una por subasta:
                            Auction ac = new Auction();
                            ac.setActualPrice(auction.getLastRoundPrice());
                            ac.setActualWinner(auction.getRoundWinner());
                            ac.setAuctionID(auction.getId());
                            ac.setBook(auction.getProductName());
                            AuctionRound ar = new AuctionRound();
                            ar.setAuction(ac);
                            ar.setRoundPrice(auction.getPrice());
                            Offer of = new Offer();
                            of.setAuctionRound(ar);

                            // Será un mensaje cfp para enviar solicitud de ronda de subasta:
                            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                            // La ontología y el lenguaje no variarán:
                            cfp.setOntology(onto.getName());
                            cfp.setLanguage(codec.getName());

                            try {
                                // Creado el agentaction y los concepts necesarios, se introducen en el mensaje:
                                getContentManager().fillContent(cfp, new Action(getAID(), of));
                            } catch (CodecException | OntologyException ex){
                                System.out.println("Error enviando el mensaje cfp: " + ex.getMessage());
                                ex.printStackTrace();
                            }

                            // Se establecen los destinatarios
                            clients.forEach(client -> cfp.addReceiver(client));
                            // Se procede al envío de los mensajes:
                            myAgent.send(cfp);
                        }
                    }
                    
                });
                
                // La recepción de propuestas se hará según este patrón:
                mt = MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()),
                        MessageTemplate.MatchLanguage(codec.getName()));
                
                // Actualizamos la interfaz:
                auctioneerGUI.updateAuctions(activeAuctions);
            }
        });
        
        // Añadimos un comportamiento cíclico para recepción de mensajes de los 
        // participantes de las subastas:
        addBehaviour(new ManageClientRequests());
    }
    
    /**
     * Método de finalización del agente.
     */
    @Override
    public void takeDown() {
        // Imprimimos mensaje de cierre
        System.out.println("Agente "+getAID().getName()+" acabando.");
    }
    
    /**
     * Método que permite añadir una nueva subasta a la interfaz
     * 
     * @param bookName El nombre del libro
     * @param price El precio inicial
     * @param step El salto
     */
    public void addAuction(String bookName, Float price, Float step) {
        // Añadimos un OneShotBehaviour que haga las acciones corespondientes:
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Incrementamos el número de subastas disponibles:
                numAuctions += 1;
                // Creamos el objeto a añadir:
                AuctionData auctionData = new AuctionData(numAuctions, bookName, price, step);
                // Añadimos la nueva subasta:
                activeAuctions.put(auctionData.getId(), auctionData);
                // Actualizamos la interfaz:
                auctioneerGUI.updateAuctions(activeAuctions);
            }
        });
    }
    
    /**
     * Comportamiento cíclio usado para manejar las peticiones
     * enviadas por los clientes participantes en las subastas.
     */
    private class ManageClientRequests extends CyclicBehaviour{

        /**
         * Método desde el cual se esperará la llegada de las solicitudes de los clientes.
         */
        @Override
        public void action() {
            // Hacemos la recepción de los mensajes con la template puesta antes:
            ACLMessage msg = myAgent.receive(mt);
            // Miramos si se ha podido recibir un mensaje o no:
            if(msg != null) {
                try {
                    // Se procesa el mensaje. Sólo se recibirán en el servidor mensajes
                    // de puja en una subasta (AgentAction bid):
                    Action cont = (Action) getContentManager().extractContent(msg);
                    Bid bid = (Bid) cont.getAction();
                    AuctionRound ar = bid.getAuctionRound();
                    // Recuperamos también al actor que acepta:
                    AID agent = cont.getActor();
                    // Vamos a comprobar si la puja con el id indicado sigue activa 
                    if(activeAuctions.containsKey(ar.getAuction().getAuctionID())) {
                        // Si está activa, comprobamos que el precio por el que se puja no se ha incrementado
                        // (o sea, seguimos en la ronda actual):
                        if(activeAuctions.get(ar.getAuction().getAuctionID()).getPrice().equals(ar.getRoundPrice())) {
                            // Si los precios coinciden, añadimos al agente a la lista de participantes de la ronda:
                            activeAuctions.get(ar.getAuction().getAuctionID()).getRoundParticipants().add(agent);
                        }
                    }
                    
                } catch (CodecException | OntologyException ex) {
                    System.out.println("Error en la recepción de una solicitud: " + ex.getMessage());
                }
            } else {
                // Evidentemente, si no se ha podido recibir ningún mensaje, se bloquea:
                block();
            }
        }
    }
    
}
