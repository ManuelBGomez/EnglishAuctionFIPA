package P6_ComDis.ontologia.impl;


import P6_ComDis.ontologia.*;

/**
* Protege name: AuctionRound
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public class DefaultAuctionRound implements AuctionRound {

  private static final long serialVersionUID = -9098608021565808103L;

  private String _internalInstanceName = null;

  public DefaultAuctionRound() {
    this._internalInstanceName = "";
  }

  public DefaultAuctionRound(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: auction
   */
   private Auction auction;
   public void setAuction(Auction value) { 
    this.auction=value;
   }
   public Auction getAuction() {
     return this.auction;
   }

   /**
   * Protege name: roundPrice
   */
   private float roundPrice;
   public void setRoundPrice(float value) { 
    this.roundPrice=value;
   }
   public float getRoundPrice() {
     return this.roundPrice;
   }

}
