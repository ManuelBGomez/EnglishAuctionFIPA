package P6_ComDis.ontologia;



/**
* Protege name: Bid
* @author OntologyBeanGenerator v4.1
* @version 2021/05/10, 20:20:39
*/
public class Bid implements jade.content.AgentAction {

   private static final long serialVersionUID = -3759134217654635196L;

  private String _internalInstanceName = null;

  public Bid() {
    this._internalInstanceName = "";
  }

  public Bid(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: bookOffer
   */
   private BookOffer bookOffer;
   public void setBookOffer(BookOffer value) { 
    this.bookOffer=value;
   }
   public BookOffer getBookOffer() {
     return this.bookOffer;
   }

}
