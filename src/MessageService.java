/**
 * This class acts as a link between two adjacent process
 */
public class MessageService {
    private int size;
    public Message[] inMessageList;
    public Message[] outMessageList;



    public MessageService(int size){
        this.size = size;
        inMessageList = new Message[size];
        outMessageList = new Message[size];

    }

    /**
     * Reads the inbound message of a particular process
     * @param index
     * @return
     */
    public Message readIncomingMessage(int index){
        Message message =  inMessageList[index];
        inMessageList[index] = null;
        return message;
    }

    /**
     * Sends the message to clockwise neighbor
     * @param index
     * @param send
     */
    public void sendMessage(int index, Message send){
        outMessageList[(index+1)%size] = send;
    }

}
