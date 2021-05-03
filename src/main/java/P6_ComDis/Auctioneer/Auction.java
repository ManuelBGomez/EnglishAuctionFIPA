/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Auctioneer;

/**
 * Clase que representa una subasta de las que se realizarán.
 * @author Manuel Bendaña
 */
public class Auction {
    private Integer id;
    private String productName;
    private Float price;
    private Float sum;

    /**
     * Constructor de la clase
     * @param id El id de la subasta
     * @param productName El producto a subastar
     * @param price El precio del producto
     * @param sum La suma de precio que se hace de cada vez
     */
    public Auction(Integer id, String productName, Float price, Float sum) {
        this.productName = productName;
        this.price = price;
        this.sum = sum;
    }

    /**
     * Método que permite recuperar el nombre del producto
     * @return El nombre del producto
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Método que permite cambiar el nombre del producto
     * @param productName El nombre del producto a cambiar
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Método que permite recuperar el precio de un producto
     * @return El precio actual del producto subastado
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Método que permite establecer el precio de un producto
     * @param price El precio del producto a cambiar.
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * Método que permite recuperar el incremento del precio del producto
     * @return El incremento actual
     */
    public Float getSum() {
        return sum;
    }

    /**
     * Método que permite establecer el incremento del precio del producto
     * @param sum El incremento a establecer
     */
    public void setSum(Float sum) {
        this.sum = sum;
    }

    /**
     * Método que permite recuperar el identificador de la subasta
     * @return El identificador a recuperar
     */
    public Integer getId() {
        return id;
    }

   /**
    * Método que permite establecer el identificador de la subasta
    * @param id El identificador a asociar
    */
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
}
