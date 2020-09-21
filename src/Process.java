public class Process implements Runnable{
    int pID;
    private int processIndex;
    CountingSemaphore signalIndicator;
    private MessageService messageService;
    private Message message;
    private boolean leaderFound;

    public Process(int id, int pID, CountingSemaphore signal, MessageService messageService){
        processIndex = id;
        this.pID = pID;
        signalIndicator = signal;
        this.messageService = messageService;
        message = new Message(pID,false);
        leaderFound = false;
    }

    public void run(){
        while(!leaderFound){
            Message incomingMsg = messageService.readIncomingMessage(processIndex);
            if(incomingMsg!=null){
                if(incomingMsg.isLeader){
                    leaderFound = true;
                    message = incomingMsg;
                    System.out.println("Thread/Process ID: "+ pID +" Leader ID: "+incomingMsg.pID);
                }
                else{
                    int nbr = incomingMsg.pID;
                    if(nbr > pID)
                        message = incomingMsg;
                    else if(nbr == pID) {
                        message = new Message(pID,true);
                        System.out.println("Thread/Process ID: "+ pID +" Leader ID: "+pID);
                    }
                }
            }
            messageService.sendMessage(processIndex, message);
            signalIndicator.semaphoreSignal();
            message = null;
            if(!leaderFound){
                synchronized (signalIndicator){
                    try{
                        signalIndicator.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
