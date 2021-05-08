package P6_ComDis.ontologia;



/**
* Protege name: Auction
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public class Auction implements jade.content.Concept {

  private static final long serialVersionUID = -9098608021565808103L;

  private String _internalInstanceName = null;

  public Auction() {
    this._internalInstanceName = "";
  }

  public Auction(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: actualWinner
   */
   private String actualWinner;
   public void setActualWinner(String value) { 
    this.actualWinner=value;
   }
   public String getActualWinner() {
     return this.actualWinner;
   }

   /**
   * Protege name: auctionID
   */
   private int auctionID;
   public void setAuctionID(int value) { 
    this.auctionID=value;
   }
   public int getAuctionID() {
     return this.auctionID;
   }

   /**
   * Protege name: actualPrice
   */
   private float actualPrice;
   public void setActualPrice(float value) { 
    this.actualPrice=value;
   }
   public float getActualPrice() {
     return this.actualPrice;
   }

   /**
   * Protege name: book
   */
   private String book;
   public void setBook(String value) { 
    this.book=value;
   }
   public String getBook() {
     return this.book;
   }

}
