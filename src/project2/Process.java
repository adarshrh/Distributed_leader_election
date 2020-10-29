package project2;

/**
 * Group Members:
 * Adarsh Raghupti Hegde  axh190002
 * Akash Akki            apa190001
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadLocalRandom;


public class Process implements Runnable{


    private static final int DELAY_MIN = 1;
    private static final int DELAY_MAX = 12;
    int pID;
    private int countMsg;
    private int diam;
    private int maxProcessId;
    private int neighborsCount;
    private List<Process> neighbors;
    private HashMap<Integer, DelayQueue<Message>> msgQueue;
    private boolean leaderFound;
    private int round;
    File outputFile;

    public Process(int pID,int diam,String outputFilePath){
        this.pID = pID;
        this.countMsg=0;
        this.diam=diam;
        this.maxProcessId= this.pID;
        this.neighbors= new ArrayList<>();
        this.msgQueue= intializeMsg();
        this.leaderFound=false;

        this.round = 0;
        this.outputFile = new File(outputFilePath);
    }

    private HashMap<Integer, DelayQueue<Message>> intializeMsg() {
        HashMap<Integer, DelayQueue<Message>> msgMap = new HashMap<>();
        for(int i=0; i<this.diam; i++){
            msgMap.put(i, new DelayQueue<>());
        }
        return msgMap;
    }


    @Override
    public void run() {
        boolean isMessageSent = true;
        while (!this.leaderFound) {
            if (this.round < this.diam) {
                if (isMessageSent) {
                    this.sendMessage();
                    isMessageSent = false;
                }
                boolean isProcessCompleted = this.receiveIncomingMessage();
                if (isProcessCompleted)
                    isMessageSent = true;
            } else if (this.round == diam) {
                if (maxProcessId == pID) {
                    this.leaderFound = true;

                } else {
                    this.leaderFound = true;

                }
                System.out.print("My Process id " + this.pID+"  ");
                System.out.println("Leader Id " + maxProcessId );
                this.round += 1;
            }
        }
    }

    private boolean receiveIncomingMessage() {

            if (this.msgQueue.get(this.round).size() == this.neighborsCount) {


            List<Message> availableMessages;

            while (this.msgQueue.get(this.round).size() > 0) {

                availableMessages = new ArrayList<>();

                this.msgQueue.get(this.round).drainTo(availableMessages);

                for (Message message : availableMessages) {
                    maxProcessId = Math.max(maxProcessId, message.getpID());


                }
            }


            this.msgQueue.remove(this.round);
            this.round += 1;

            return true;
        }
        return false;


    }

    private void sendMessage() {
        for(Process p : this.neighbors){

            int randDelay = ThreadLocalRandom.current().nextInt(DELAY_MIN, DELAY_MAX+1);


            p.msgQueue.get(this.round).add(new Message(maxProcessId, randDelay,this.round));

            this.countMsg += 1;


        }
    }
    public void addNeighbour(Process process){
        this.neighbors.add(process);
        this.neighborsCount++;
    }
    public int getMsgsCount() {
        return countMsg;
    }
    public boolean getLeaderFound(){
        return  leaderFound;
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


