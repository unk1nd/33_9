package v13;

public class Message implements java.io.Serializable {
  private String message;
  private String user;


  public Message(String message) {
    this.message = message;

  }

  public String getUser() {
    return user;
  }

  public String getMessage() {
    return message;
  }


}
