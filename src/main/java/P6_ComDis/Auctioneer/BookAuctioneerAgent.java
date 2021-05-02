package P6_ComDis.Auctioneer;

import jade.core.Agent;

/**
 * Clase que representa al agente subastador.
 * @author Manuel Benda�a
 */
public class BookAuctioneerAgent extends Agent {
    
    /**
     * M�todo de inicializaci�n del agente.
     */
    @Override
    public void setup() {
        // Se imprime un mensaje que marca el comienzo del agente:
        System.out.println("Agente "+getAID().getName()+" comenzando.");
        
        
    }
    
    @Override
    public void takeDown() {
        // Imprimimos mensaje de cierre
        System.out.println("Agente "+getAID().getName()+" acabando.");
    }
}
