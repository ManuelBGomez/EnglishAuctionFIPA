package P6_ComDis.Auctioneer;

import jade.core.Agent;
import java.util.HashMap;

/**
 * Clase que representa al agente subastador.
 * @author Manuel Benda�a
 */
public class BookAuctioneerAgent extends Agent {
    private HashMap<Integer, Auction> activeAuctions;
    private int numAuctions;
    private AuctioneerGUI auctioneerGUI;
    
    
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
        
        
    }
    
    @Override
    public void takeDown() {
        // Imprimimos mensaje de cierre
        System.out.println("Agente "+getAID().getName()+" acabando.");
    }
}
