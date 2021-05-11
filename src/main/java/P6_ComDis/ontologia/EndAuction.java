package P6_ComDis.ontologia;



/**
* Protege name: EndAuction
* @author OntologyBeanGenerator v4.1
* @version 2021/05/11, 21:25:06
*/
public class EndAuction implements jade.content.AgentAction {

    private static final long serialVersionUID = -772136991049806111L;

  private String _internalInstanceName = null;

  public EndAuction() {
    this._internalInstanceName = "";
  }

  public EndAuction(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: lastOffer
   */
   private BookOffer lastOffer;
   public void setLastOffer(BookOffer value) { 
    this.lastOffer=value;
   }
   public BookOffer getLastOffer() {
     return this.lastOffer;
   }

   /**
   * Protege name: auctionWinner
   */
   private jade.core.AID auctionWinner;
   public void setAuctionWinner(jade.core.AID value) { 
    this.auctionWinner=value;
   }
   public jade.core.AID getAuctionWinner() {
     return this.auctionWinner;
   }

}
