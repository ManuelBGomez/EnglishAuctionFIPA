/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author manub
 */
public class ClientTableModel extends AbstractTableModel{
    // La lista que se representará: lista de subastas
    private List<AuctionClientData> auctions;
    
    /**
     * Constructor de la clase.
     */
    public ClientTableModel(){
        this.auctions = new ArrayList<>();
    }

    /**
     * Método que permite recuperar el número de filas de la tabla.
     * @return El número de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return auctions.size();
    }

    /**
     * Método que permite recuperar el número de columnas de la tabla
     * @return 
     */
    @Override
    public int getColumnCount() {
        return 6;
    }
    
    /**
     * Método que permite recuperar el nombre de alguna columna:
     * @param col La columna a recuperar
     * @return El nombre de la columna
     */
    @Override
    public String getColumnName(int col){
        String nombre = "";
        
        switch(col){
            case 0: nombre = "Id subasta"; break;
            case 1: nombre = "Libro"; break;
            case 2: nombre = "Precio actual"; break;
            case 3: nombre = "Ganador actual"; break;
            case 4: nombre = "Subastador"; break;
            case 5: nombre = "Estado"; break;
        }
        
        return nombre;
    }
    
    /**
     * Método que permite recuperar la clase de una columna
     * @param col La columna a recuperar
     * @return El nombre de la columna
     */
    @Override
    public Class getColumnClass(int col){
        Class cl=null;

        switch (col){
            case 0: cl = Integer.class; break;
            case 1: cl = String.class; break;
            case 2: cl = String.class; break;
            case 3: cl = String.class; break;
            case 4: cl = String.class; break;
            case 5: cl = String.class; break;
        }
        return cl;
    }

    /**
     * Método que permite recuperar el valor de una celda determinada.
     * @param rowIndex El número de fila.
     * @param columnIndex El número de columna.
     * @return El objeto de esa celda.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        switch (columnIndex) {
            case 0: result = auctions.get(rowIndex).getId(); break;
            case 1: result = auctions.get(rowIndex).getProductName(); break;
            case 2: result = auctions.get(rowIndex).getPrice() < 0 ? "-" : auctions.get(rowIndex).getPrice() ; break;
            case 3: result = auctions.get(rowIndex).getRoundWinner().isEmpty() ? "-" : auctions.get(rowIndex).getRoundWinner(); break;
            case 4: result = auctions.get(rowIndex).getAuctioneer(); break;
            case 5: result = auctions.get(rowIndex).getState(); break;
        }
        
        return result;
    }

    /**
     * Método que permite recuperar el array de subastas
     * @return El array de subastas almacenado
     */
    public List<AuctionClientData> getAuctions() {
        return auctions;
    }

    /**
     * Método que permite establecer el array de subastas en la tabla
     * @param auctions El array de subastas a establecer.
     */
    public void setAuctions(List<AuctionClientData> auctions) {
        this.auctions = auctions;
        fireTableDataChanged();
    }
    
    /**
     * Método que permite añadir una subasta a la lista
     * @param auction La subasta a añadir.
     */
    public void addAuction(AuctionClientData auction) {
        int i;
        for(i = 0; i < auctions.size(); i++){
            // Buscamos una subasta del mismo subastador con el mismo id, que esté marcada en progreso:
            if(auction.getId().equals(auctions.get(i).getId()) &&
                auction.getAuctioneer().equals(auctions.get(i).getAuctioneer()) &&
                auctions.get(i).getState().equals(AuctionState.EN_PROGRESO)) {
                // Si la encontramos, paramos el bucle:
                break;
            }
        }
        
        // Si i supera el tamaño del número de subastas, la subasta se añade:
        if(i >= auctions.size()) {
            auctions.add(auction);
        } else {
            // En otro caso, se hace reemplazo en la posición correspondiente de los parámetros deseables no nulos:
            if(auction.getRoundWinner() != null) auctions.get(i).setRoundWinner(auction.getRoundWinner());
            if(auction.getPrice() != null) auctions.get(i).setPrice(auction.getPrice());
            if(auction.getState() != null) auctions.get(i).setState(auction.getState());
        }
        fireTableDataChanged();
    }
    
    /**
     * Método que permite recuperar una de las subastas almacenadas
     * @param i El índice de la subasta a recuperar.
     * @return La información de la subasta correspondiente.
     */
    public AuctionClientData getAuction(int i) {
        return this.auctions.get(i);
    }
    
}
