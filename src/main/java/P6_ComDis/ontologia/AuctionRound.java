package P6_ComDis.ontologia;



/**
* Protege name: AuctionRound
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public interface AuctionRound extends jade.content.Concept {

   /**
   * Protege name: auction
   */
   public void setAuction(Auction value);
   public Auction getAuction();

   /**
   * Protege name: roundPrice
   */
   public void setRoundPrice(float value);
   public float getRoundPrice();

}
