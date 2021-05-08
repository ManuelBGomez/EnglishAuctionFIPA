/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Client;

import java.util.Objects;

/**
 * Clase que representa a uno de los libros que desea comprar un cliente.
 * @author Manuel Bendaña
 */
public class DesiredBook {
    private String bookName;
    private Float maxPrice;

    /**
     * Constructor de la clase.
     * @param bookName Nombre del libro
     * @param maxPrice Precio máximo dispuesto a pagar.
     */
    public DesiredBook(String bookName, Float maxPrice) {
        this.bookName = bookName;
        this.maxPrice = maxPrice;
    }

    /**
     * Método que permite recuperar el nombre del libro deseado
     * @return El nombre del libro deseado.
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * Método que permite establecer el nombre del libro deseado
     * @param bookName El nombre a establecer
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * Método que permite recuperar el precio máximo dispuestos a pagar por el libro
     * @return El precio máximo dispuestos a pagar
     */
    public Float getMaxPrice() {
        return maxPrice;
    }

    /**
     * Método que permite establecer el precio máximo dispuestos a pagar por el libro.
     * @param maxPrice El precio máximo a establecer que se está dispuesto a pagar.
     */
    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * HashCode
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.bookName);
        return hash;
    }

    /**
     * Método que permite comprobar la igualdad de dos DesiredBook
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DesiredBook other = (DesiredBook) obj;
        if (!Objects.equals(this.bookName, other.bookName)) {
            return false;
        }
        return true;
    }
    
    
}
