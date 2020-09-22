/**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Each thread of this class simulates a process.
 */
public class Process implements Runnable{
    int pID;
    private int processIndex;
    CountingSemaphore semaphore;
    private MessageService messageService;
    private Message message;
    private boolean leaderFound;
    File outputFile;

    public Process(int id, int pID, CountingSemaphore signal, MessageService messageService, String outputFilePath){
        processIndex = id;
        this.pID = pID;
        semaphore = signal;
        this.messageService = messageService;
        message = new Message(pID,false);
        leaderFound = false;
        this.outputFile = new File(outputFilePath);
    }

    /**
     * Reads incoming message and sends a message to its clockwise neighbor according to LCR algorithm
     */
    public void run(){
        while(!leaderFound){
            Message incomingMsg = messageService.readIncomingMessage(processIndex);
            if(incomingMsg!=null){
                if(incomingMsg.isLeader){
                    leaderFound = true;
                    message = incomingMsg;
                    System.out.println("Thread/Process ID: "+ pID +" Leader ID: "+incomingMsg.pID);
                    try {
                        writeOutput("Thread/Process ID: "+ pID +" Leader ID: "+incomingMsg.pID);
                    } catch (IOException e) {
                        System.out.println("Failed to write output to the file:"+outputFile.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
                else{
                    int nbr = incomingMsg.pID;
                    if(nbr < pID)
                        message = incomingMsg;
                    else if(nbr == pID) {
                        message = new Message(pID,true);
                    }
                }
            }
            messageService.sendMessage(processIndex, message);
            semaphore._signal();
            message = null;
            if(!leaderFound){
                synchronized (semaphore){
                    try{
                        semaphore.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Writes the leader election output to the output file
     * @param output
     * @throws IOException
     */
    public void writeOutput(String output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile,true));
        writer.write(output);
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
