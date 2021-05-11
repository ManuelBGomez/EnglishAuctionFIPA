package P6_ComDis.ontologia;



/**
* Protege name: Book
* @author OntologyBeanGenerator v4.1
* @version 2021/05/11, 21:25:06
*/
public class Book implements jade.content.Concept {

   private static final long serialVersionUID = -772136991049806111L;

  private String _internalInstanceName = null;

  public Book() {
    this._internalInstanceName = "";
  }

  public Book(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: bName
   */
   private String bName;
   public void setBName(String value) { 
    this.bName=value;
   }
   public String getBName() {
     return this.bName;
   }
}
