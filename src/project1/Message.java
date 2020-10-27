package project1;

/**
 * project2.Message class specifies the message format.
 * It includes the process ID and the status which indicates whether the leader is found or not
 */
public class Message {
    Integer pID;
    boolean isLeader;

    Message(Integer pID, boolean isLeader){
        this.pID = pID;
        this.isLeader = isLeader;
    }
}
