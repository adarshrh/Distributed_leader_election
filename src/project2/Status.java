package project2;

public enum Status {
  EXPLORE("EXPLORE"),
  NACK("NACK"),
  ACK("ACK"),
  LEADER("LEADER");

  private String type;

  private Status(String type){
    this.type = type;
  }

}
