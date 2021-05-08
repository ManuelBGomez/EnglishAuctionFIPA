/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Client;

import java.util.Objects;

/**
 * Clase que representa a uno de los libros que desea comprar un cliente.
 * @author Manuel Benda�a
 */
public class DesiredBook {
    private String bookName;
    private Float maxPrice;

    /**
     * Constructor de la clase.
     * @param bookName Nombre del libro
     * @param maxPrice Precio m�ximo dispuesto a pagar.
     */
    public DesiredBook(String bookName, Float maxPrice) {
        this.bookName = bookName;
        this.maxPrice = maxPrice;
    }

    /**
     * M�todo que permite recuperar el nombre del libro deseado
     * @return El nombre del libro deseado.
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * M�todo que permite establecer el nombre del libro deseado
     * @param bookName El nombre a establecer
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * M�todo que permite recuperar el precio m�ximo dispuestos a pagar por el libro
     * @return El precio m�ximo dispuestos a pagar
     */
    public Float getMaxPrice() {
        return maxPrice;
    }

    /**
     * M�todo que permite establecer el precio m�ximo dispuestos a pagar por el libro.
     * @param maxPrice El precio m�ximo a establecer que se est� dispuesto a pagar.
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
     * M�todo que permite comprobar la igualdad de dos DesiredBook
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
