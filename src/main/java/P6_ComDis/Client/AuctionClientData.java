/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Client;

/**
 * Clase que representa a los datos de la subasta que representa el cliente.
 * @author Manuel Bendaña
 */
public class AuctionClientData {
    private Integer id;
    private String productName;
    private Float price;
    private String roundWinner;
    private String auctioneer;
    private AuctionState state;

    /**
     * Constructor de la clase
     * @param id Identificador subasta
     * @param productName Producto
     * @param price Precio
     * @param roundWinner Ganador de la ronda
     * @param auctioneer Subastador
     * @param state El estado de la subasta (desde el punto de vista del interés para el cliente).
     */
    public AuctionClientData(Integer id, String productName, Float price, String roundWinner, String auctioneer, AuctionState state) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.roundWinner = roundWinner;
        this.auctioneer = auctioneer;
        this.state = state;
    }

    /**
     * Método que permite recuperar el id de la subasta.
     * @return El id correspondiente
     */
    public Integer getId() {
        return id;
    }

    /**
     * Método que permite establecer el id de la subasta.
     * @param id El id a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Método que permite recuperar el nombre del producto.
     * @return El nombre del producto a recuperar.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Método que permite establecer el nombre del producto.
     * @param productName El nombre del producto.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Método que permite recuperar el precio del producto.
     * @return El precio del producto.
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Método que permite establecer el precio del producto.
     * @param price El precio del producto.
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * Método que permite recuperar el ganador de una ronda.
     * @return El ganador de la ronda.
     */
    public String getRoundWinner() {
        return roundWinner;
    }

    /**
     * Método que permite establecer el ganador de una ronda.
     * @param roundWinner El ganador de la ronda.
     */
    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }

    /**
     * Método que permite recuperar al subastador.
     * @return El subastador.
     */
    public String getAuctioneer() {
        return auctioneer;
    }

    /**
     * Método que permite establecer el subastador
     * @param auctioneer El subastador 
     */
    public void setAuctioneer(String auctioneer) {
        this.auctioneer = auctioneer;
    }

    /**
     * Método que permite recuperar el estado de la subasta
     * @return El estado de la subasta.
     */
    public AuctionState getState() {
        return state;
    }

    /**
     * Método que permite establecer el estado de la subasta.
     * @param state El estado de la subasta.
     */
    public void setState(AuctionState state) {
        this.state = state;
    }
    
    
}
