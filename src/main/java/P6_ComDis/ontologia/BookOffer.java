package P6_ComDis.ontologia;



/**
* Protege name: BookOffer
* @author OntologyBeanGenerator v4.1
* @version 2021/05/11, 21:25:06
*/
public class BookOffer implements jade.content.Concept {

   private static final long serialVersionUID = -772136991049806111L;

  private String _internalInstanceName = null;

  public BookOffer() {
    this._internalInstanceName = "";
  }

  public BookOffer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: bookInfo
   */
   private Book bookInfo;
   public void setBookInfo(Book value) { 
    this.bookInfo=value;
   }
   public Book getBookInfo() {
     return this.bookInfo;
   }

   /**
   * Protege name: auctionId
   */
   private int auctionId;
   public void setAuctionId(int value) { 
    this.auctionId=value;
   }
   public int getAuctionId() {
     return this.auctionId;
   }

   /**
   * Protege name: price
   */
   private float price;
   public void setPrice(float value) { 
    this.price=value;
   }
   public float getPrice() {
     return this.price;
   }

}
