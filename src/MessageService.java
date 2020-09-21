public class MessageService {
    private int size;
    public Message[] inMessageList;
    public Message[] outMessageList;



    public MessageService(int size){
        this.size = size;
        inMessageList = new Message[size];
        outMessageList = new Message[size];

    }

    public Message readIncomingMessage(int idx){
        Message message =  inMessageList[idx];
        inMessageList[idx] = null;
        return message;
    }

    public void sendMessage(int idx, Message send){
        outMessageList[(idx+1)%size] = send;
    }

}
