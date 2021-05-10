package P6_ComDis.ontologia;



/**
* Protege name: EndRound
* @author OntologyBeanGenerator v4.1
* @version 2021/05/10, 20:20:39
*/
public class EndRound implements jade.content.AgentAction {

  private static final long serialVersionUID = -3759134217654635196L;

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
   * Protege name: winner
   */
   private jade.core.AID winner;
   public void setWinner(jade.core.AID value) { 
    this.winner=value;
   }
   public jade.core.AID getWinner() {
     return this.winner;
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

}
