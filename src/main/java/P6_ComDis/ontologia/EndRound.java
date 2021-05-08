package P6_ComDis.ontologia;



/**
* Protege name: EndRound
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public class EndRound implements jade.content.AgentAction {

   private static final long serialVersionUID = -9098608021565808103L;

  private String _internalInstanceName = null;

  public EndRound() {
    this._internalInstanceName = "";
  }

  public EndRound(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: auctionRound
   */
   private AuctionRound auctionRound;
   public void setAuctionRound(AuctionRound value) { 
    this.auctionRound=value;
   }
   public AuctionRound getAuctionRound() {
     return this.auctionRound;
   }

}
