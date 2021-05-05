package P6_ComDis.ontologia;



/**
* Protege name: Auction
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public interface Auction extends jade.content.Concept {

   /**
   * Protege name: actualWinner
   */
   public void setActualWinner(String value);
   public String getActualWinner();

   /**
   * Protege name: auctionID
   */
   public void setAuctionID(int value);
   public int getAuctionID();

   /**
   * Protege name: actualPrice
   */
   public void setActualPrice(float value);
   public float getActualPrice();

   /**
   * Protege name: book
   */
   public void setBook(String value);
   public String getBook();

}
