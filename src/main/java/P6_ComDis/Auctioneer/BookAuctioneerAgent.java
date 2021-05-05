package P6_ComDis.Auctioneer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa al agente subastador.
 * @author Manuel Benda�a
 */
public class BookAuctioneerAgent extends Agent {
    private static final Integer INTERVAL = 10000;
    private HashMap<Integer, Auction> activeAuctions;
    private ArrayList<AID> clients;
    private int numAuctions;
    private AuctioneerGUI auctioneerGUI;
    private MessageTemplate mt;
    
    
    /**
     * M�todo de inicializaci�n del agente.
     */
    @Override
    public void setup() {
        // Se imprime un mensaje que marca el comienzo del agente:
        System.out.println("Agente "+getAID().getName()+" comenzando.");
        
        // Inicializaci�n:
        this.numAuctions = 0;
        this.activeAuctions = new HashMap<>();
        this.auctioneerGUI = new AuctioneerGUI();
        this.auctioneerGUI.showGui();
        
        // A�adimos un tickerBehaviour que consulta los agentes disponibles:
        this.addBehaviour(new TickerBehaviour(this, INTERVAL) {
            @Override
            public void onTick() {
                System.out.println("Recuperando clientes disponibles");
                
                // Usaremos este tickerbehaviour para actualizar la lista de compradores:
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                
                sd.setType("auction");
                
                // Asociamos a la DFAgentDescription el servicio de subastas.
                // LOs vendedores interesados se a�adir�n:
                template.addServices(sd);
                
                // Hacemos la b�squeda de los agentes:
                try {
                    // Introducimos este agente y la plantilla elaborada previamente:
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    
                    clients = new ArrayList<>();
                    
                    // Para cada agente encontrado se recupera su AID:
                    for (DFAgentDescription result1 : result) {
                        clients.add(result1.getName());
                    }
                } catch (FIPAException fe) {
                    // Si se captura una excepci�n, se avisa de ello:
                    System.out.println("Error al recuperar los agentes: " + fe.getMessage());
                }
                
                // Desde aqu�, lo que hacemos es, para cada subasta, enviar peticiones a todos los clientes:
                
                // Ser� un mensaje cfp en todos los casos, as� que lo reciclaremos:
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                // La respuesta tampoco va a variar:
                cfp.setReplyWith("cfp " + System.currentTimeMillis());
                
                activeAuctions.entrySet().stream().map(entries -> entries.getValue()).forEachOrdered(auction -> {
                    // Se recuperan los datos y se env�an las peticiones pertinentes:
                    // A cada usuario, una por subasta:
                    // El contenido ser� la informaci�n de la subasta, que ir� cambiando:
                    // Se meten todos los agentes:
                    clients.forEach(client -> cfp.addReceiver(client));
                    // Se procede al env�o de los mensajes:
                    myAgent.send(cfp);
                });
                
                // La recepci�n de propuestas se har� seg�n este patr�n:
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("auction"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
            }
        });
    }
    
    @Override
    public void takeDown() {
        // Imprimimos mensaje de cierre
        System.out.println("Agente "+getAID().getName()+" acabando.");
    }
    
}
