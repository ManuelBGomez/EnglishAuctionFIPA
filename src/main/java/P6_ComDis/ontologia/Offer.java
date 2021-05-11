package P6_ComDis.ontologia;



/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2021/05/11, 21:25:06
*/
public class Offer implements jade.content.AgentAction {

   private static final long serialVersionUID = -772136991049806111L;

  private String _internalInstanceName = null;

  public Offer() {
    this._internalInstanceName = "";
  }

  public Offer(String instance_name) {
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
