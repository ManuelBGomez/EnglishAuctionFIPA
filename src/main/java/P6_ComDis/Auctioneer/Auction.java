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
public class Auction {
    private Integer id;
    private String productName;
    private Float price;
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
    public Auction(Integer id, String productName, Float price, Float sum) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.sum = sum;
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
}
