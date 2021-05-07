/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Auctioneer;

import jade.core.AID;
import java.util.ArrayList;

/**
 * Clase que representa una subasta de las que se realizar�n.
 * @author Manuel Benda�a
 */
public class AuctionData {
    private Integer numRound;
    private Integer id;
    private String productName;
    private Float price;
    private Float lastRoundPrice;
    private Float sum;
    private ArrayList<AID> roundParticipants;
    private ArrayList<AID> prevRoundParticipants;

    /**
     * Constructor de la clase
     * @param id El id de la subasta
     * @param productName El producto a subastar
     * @param price El precio del producto
     * @param sum La suma de precio que se hace de cada vez
     */
    public AuctionData(Integer id, String productName, Float price, Float sum) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.lastRoundPrice = null;
        this.sum = sum;
        // Se empezar� siempre antes de la ronda 1:
        this.numRound = 0;
    }

    /**
     * M�todo que permite obtener el ganador de una ronda.
     * @return El nombre del ganador.
     */
    public String getRoundWinner() {
        if(roundParticipants != null && prevRoundParticipants != null){
            if(roundParticipants.size() >= 1){
                // Si hay solo un participante o m�s, se devuelve el primero (o �nico):
                return roundParticipants.get(0).getName();
            } else {
                // Si es 0, se devuelve el primero de la ronda anterior, si lo hay (si no se devuelve un string vac�o):
                return prevRoundParticipants.size() > 0 ? prevRoundParticipants.get(0).getName() : "";
            }
        } else {
            return "";
        }
    }
    
    /**
     * M�todo que permite validar la finalizaci�n de una subasta en base a los 
     * participantes en la �ltima ronda.
     * @return True si se termin�, false en otro caso.
     */
    public boolean isAuctionFinished(){
        if(roundParticipants != null && prevRoundParticipants != null){
            // Comprobamos si en la ronda actual hay 1 o 0 participantes:
            return roundParticipants.size() <= 1;
        } else {
            // Si a�n no se han creado los arrays de participantes es que ni siquiera se ha hecho una ronda:
            // En este punto, los instanciaremos (as� en la siguiente vuelta fijo no ser�n null):
            roundParticipants = new ArrayList<>();
            prevRoundParticipants = new ArrayList<>();
            return false;
        }
    }
    
    /**
     * M�todo que permite recuperar el nombre del producto
     * @return El nombre del producto
     */
    public String getProductName() {
        return productName;
    }

    /**
     * M�todo que permite cambiar el nombre del producto
     * @param productName El nombre del producto a cambiar
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * M�todo que permite recuperar el precio de un producto
     * @return El precio actual del producto subastado
     */
    public Float getPrice() {
        return price;
    }

    /**
     * M�todo que permite establecer el precio de un producto
     * @param price El precio del producto a cambiar.
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * M�todo que permite recuperar el incremento del precio del producto
     * @return El incremento actual
     */
    public Float getSum() {
        return sum;
    }

    /**
     * M�todo que permite establecer el incremento del precio del producto
     * @param sum El incremento a establecer
     */
    public void setSum(Float sum) {
        this.sum = sum;
    }

    /**
     * M�todo que permite recuperar el identificador de la subasta
     * @return El identificador a recuperar
     */
    public Integer getId() {
        return id;
    }

   /**
    * M�todo que permite establecer el identificador de la subasta
    * @param id El identificador a asociar
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * M�todo que permite recuperar los participantes de la ronda actual
     * @return El arraylist de participantes de la ronda actual
     */
    public ArrayList<AID> getRoundParticipants() {
        return roundParticipants;
    }

    /**
     * M�todo que permite establecer el array de participantes de la ronda actual.
     * @param roundParticipants El arraylist a asociar
     */
    public void setRoundParticipants(ArrayList<AID> roundParticipants) {
        this.roundParticipants = roundParticipants;
    }

    /**
     * M�todo que permite recuperar el array de participantes de la ronda previa.
     * @return El arraylist de participantes de la ronda anterior.
     */
    public ArrayList<AID> getPrevRoundParticipants() {
        return prevRoundParticipants;
    }

    /**
     * M�todo que permite establecer el array de participantes de la ronda previa
     * @param prevRoundParticipants El arraylist a asociar
     */
    public void setPrevRoundParticipants(ArrayList<AID> prevRoundParticipants) {
        this.prevRoundParticipants = prevRoundParticipants;
    }

    /**
     * M�todo que permite recuperar el precio de la anterior ronda.
     * @return el precio de la ronda anterior.
     */
    public Float getLastRoundPrice() {
        return lastRoundPrice;
    }

    /**
     * M�todo que permite establecer el precio de la anterior ronda.
     * @param lastRoundPrice el precio a establecer.
     */
    public void setLastRoundPrice(Float lastRoundPrice) {
        this.lastRoundPrice = lastRoundPrice;
    }

    /**
     * M�todo que permite recuperar el n�mero de ronda.
     * @return El n�mero de ronda almacenado.
     */
    public Integer getNumRound() {
        return numRound;
    }

    /**
     * M�todo que permite establecer el n�mero de ronda.
     * @param numRound El n�mero de ronda a establecer.
     */
    public void setNumRound(Integer numRound) {
        this.numRound = numRound;
    }
    
    /**
     * M�todo que permite avanzar una subasta de ronda.
     */
    public void nextRound(){
        this.numRound += 1;
        // SI la ronda era 0 (pasa a 1), entonces el precio se mantiene (el inicial):
        // Si es mayor, se suma.
        if(this.numRound > 1){
            // Adem�s, se actualiza el precio de la ronda previa:
            this.lastRoundPrice = this.price;
            this.price += this.sum;
            // Actualizamos arrays:
            this.prevRoundParticipants = this.roundParticipants;
            this.roundParticipants = new ArrayList<>();
        }
    }
    
}
