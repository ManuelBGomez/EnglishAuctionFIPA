package P6_ComDis.ontologia;



/**
* Protege name: EndAuction
* @author OntologyBeanGenerator v4.1
* @version 2021/05/5, 21:39:32
*/
public class EndAuction implements jade.content.AgentAction {

  private static final long serialVersionUID = -9098608021565808103L;

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
   * Protege name: auction
   */
   private Auction auction;
   public void setAuction(Auction value) { 
    this.auction=value;
   }
   public Auction getAuction() {
     return this.auction;
   }
}