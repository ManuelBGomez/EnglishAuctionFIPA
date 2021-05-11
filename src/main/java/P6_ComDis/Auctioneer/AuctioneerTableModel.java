/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P6_ComDis.Auctioneer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Clase que representa la tabla de subastas de la interfaz del subastador.
 * @author Manuel Benda�a
 */
public class AuctioneerTableModel extends AbstractTableModel{
    // La lista que se representar�: lista de subastas
    private List<AuctionData> auctions;
    
    /**
     * Constructor de la clase.
     */
    public AuctioneerTableModel(){
        this.auctions = new ArrayList<>();
    }

    /**
     * M�todo que permite recuperar el n�mero de filas de la tabla.
     * @return El n�mero de filas de la tabla.
     */
    @Override
    public int getRowCount() {
        return auctions.size();
    }

    /**
     * M�todo que permite recuperar el n�mero de columnas de la tabla
     * @return 
     */
    @Override
    public int getColumnCount() {
        return 6;
    }
    
    /**
     * M�todo que permite recuperar el nombre de alguna columna:
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
            case 4: nombre = "Estado"; break;
            case 5: nombre = "N� interesados (ultima ronda)"; break;
        }
        
        return nombre;
    }
    
    /**
     * M�todo que permite recuperar la clase de una columna
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
            case 5: cl = Integer.class; break;
        }
        return cl;
    }

    /**
     * M�todo que permite recuperar el valor de una celda determinada.
     * @param rowIndex El n�mero de fila.
     * @param columnIndex El n�mero de columna.
     * @return El objeto de esa celda.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        switch (columnIndex) {
            case 0: result = auctions.get(rowIndex).getId(); break;
            case 1: result = auctions.get(rowIndex).getProductName(); break;
            case 2: result = auctions.get(rowIndex).getPrice() < 0 ? "-" : auctions.get(rowIndex).getPrice(); break;
            case 3: result = auctions.get(rowIndex).getRoundWinner() == null ? "-" : auctions.get(rowIndex).getRoundWinner().getName(); break;
            case 4: result = auctions.get(rowIndex).getIsFinished() ? "Terminada" : "En proceso"; break;
            case 5: result = auctions.get(rowIndex).getRoundParticipants() != null ? auctions.get(rowIndex).getRoundParticipants().size() : 0 ; break;
        }
        
        return result;
    }

    /**
     * M�todo que permite recuperar el array de subastas
     * @return El array de subastas almacenado
     */
    public List<AuctionData> getAuctions() {
        return auctions;
    }

    /**
     * M�todo que permite establecer el array de subastas en la tabla
     * @param auctions El array de subastas a establecer.
     */
    public void setAuctions(List<AuctionData> auctions) {
        this.auctions = auctions;
        fireTableDataChanged();
    }
    
    /**
     * M�todo que permite recuperar una de las subastas almacenadas
     * @param i El �ndice de la subasta a recuperar.
     * @return La informaci�n de la subasta correspondiente.
     */
    public AuctionData getAuction(int i) {
        return this.auctions.get(i);
    }
    
}
