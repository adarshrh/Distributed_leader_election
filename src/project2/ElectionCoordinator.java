package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * project2.ElectionCoordinator is the master thread which reads input file and spawns n processes.
 * The master thread notifies all other threads when a round is completed.
 */
public class ElectionCoordinator implements Runnable{

    boolean leaderFound;
    int[] pID;
    MessageService messageService;
    CountingSemaphore countingSemaphore;
    List<Thread> processList = new ArrayList<>();
    int n;
    String outputFilePath;

    ElectionCoordinator(int[] pID, String outputFilePath){
        this.pID = pID;
        this.n = pID.length;
        this.messageService = new MessageService(n);
        leaderFound = false;
        countingSemaphore = new CountingSemaphore(n);
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void run() {
        createProcess();
        try {
            execRounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create n threads with corresponding process IDs and starts the thread.
     */
    public void createProcess(){
        for(int i=0;i<n;i++){
            Thread process = new Thread(new Process(i,pID[i],countingSemaphore, messageService,outputFilePath));
            process.setName(String.valueOf(pID[i]));
            countingSemaphore._wait();
            process.start();
            processList.add(process);
        }
    }

    /**
     * Co-ordinates the execution of rounds by making use of counting semaphore
     * @throws InterruptedException
     */
    public void execRounds() throws InterruptedException {
        while(!leaderFound){
            while(!countingSemaphore.isRoundCompleted()){
                Thread.sleep(10);
            }
            for(int i=0;i<messageService.outMessageList.length;i++){
                messageService.inMessageList[i] = messageService.outMessageList[i];
            }
            Arrays.fill(messageService.outMessageList,null);
            synchronized (countingSemaphore){
                for(Thread th : processList){
                    if(th.getState()!= Thread.State.TERMINATED){
                        countingSemaphore._wait();
                    }
                }
                if(this.countingSemaphore.isRoundCompleted())
                    this.leaderFound = true;
                countingSemaphore.notifyAll();
            }
        }
    }

    public static void main(String[] args)  {
        Scanner in = null;
        try {
        if (args.length > 1) {
                in =  new Scanner(new File(args[0]));
        } else {
            System.out.println("Please provide input and output file path as command line argument to the program");
            System.exit(1);
        }
        } catch (FileNotFoundException e) {
            System.out.println("Input/Output file not found. Please provide absolute file path as command line argument to the program ");
            e.printStackTrace();
            System.exit(1);
        }
        int n = in.nextInt();
        int[] pID = new int[n];
        for(int i=0;i<n;i++){
            pID[i] = in.nextInt();
        }
        Thread electionCoordinator = new Thread(new ElectionCoordinator(pID,args[1]));
        electionCoordinator.start();
    }

}
