package project3;

public enum Status {
  EXPLORE("EXPLORE"),
  NACK("NACK"),
  ACK("ACK"),
  CONVERGECAST("CONVERGECAST"),
  TERMINATE("TERMINATE"),
  UNKNOWN("UNKNOWN");
  private String type;

  private Status(String type){
    this.type = type;
  }

}
