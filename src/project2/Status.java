package project2;

public enum Status {
  EXPLORE("EXPLORE"),
  NACK("NACK"),
  ACK("ACK"),
  LEADER("LEADER");

  private String type;

  private Status(String _type){
    this.type = _type;
  }

  public String getType(){
    return this.type;
  }
}
