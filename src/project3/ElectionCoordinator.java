package project3; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.DelayQueue;


public class ElectionCoordinator implements Runnable{


    int[] pID;
    Map<Integer, List<Edge>> adjList = new HashMap();
    HashMap<Integer, DelayQueue<Message>> queueMap = new HashMap<>();
    MessageService messageService;
    int n;
    String outputFilePath;
    Process[] processes;
    int rootPid;
    CountingSemaphore countingSemaphore;


    ElectionCoordinator(int[] pID, Map<Integer, List<Edge>> adjList, String outputFilePath, int rootId){
        this.pID = pID;
        this.rootPid = rootId;
        this.n = pID.length;
        this.adjList = adjList;
        this.outputFilePath = outputFilePath;
        for (int i = 0; i < n; i++) {
            queueMap.put(pID[i], new DelayQueue<>());
        }
        messageService = new MessageService(adjList,queueMap,n);
        processes = new Process[n];
        countingSemaphore = new CountingSemaphore(n);
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[n];
        createProcess(threads);
        try{
            int terminatedCount = 0;
            while (terminatedCount < n) {
                terminatedCount = 0;
                for (Thread thread: threads){
                    if(!thread.isAlive()){
                        terminatedCount++;
                    }
                }
                if(terminatedCount==n)
                    break;
                while (!countingSemaphore.isRoundCompleted()){
                    Thread.sleep(10);
                //    System.out.println("#####Checking if algo complete");
                }

                synchronized (countingSemaphore){
                    countingSemaphore.notifyAll();
                }



            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int msgCount = 0;
        for(Process p : processes){
            msgCount += p.getMessageCount();
        }
        System.out.println("Total msg sent:"+msgCount);
        try {
            writeOutput("Total msg sent:"+msgCount);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Create n processes and initialize
     * @param threads
     */
    private void createProcess(Thread[] threads) {
        for (int i = 0; i < n; i++) {
            if(i == this.rootPid)
                processes[i] = new Process(pID[i], messageService, adjList.get(pID[i]), queueMap.get(pID[i]),outputFilePath,true, countingSemaphore);
            else
                processes[i] = new Process(pID[i], messageService, adjList.get(pID[i]), queueMap.get(pID[i]),outputFilePath,false, countingSemaphore);
            threads[i] = new Thread(processes[i]);
        }

        for (Thread th: threads) {
            th.start();
        }
    }

    /**
     * Write output to file
     * @param output
     * @throws IOException
     */
    public void writeOutput(String output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath,true));
        writer.write(output);
        writer.newLine();
        writer.flush();
        writer.close();
    }

    public static void main(String[] args)  {
        Scanner in = null;
        int rootPid;
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
        rootPid = in.nextInt();
        int[] pID = new int[n];
        for(int i=0;i<n;i++){
            pID[i] = i+1;
        }
        //Create adjacency list
        HashMap<Integer, List<Edge>> adjList = new HashMap();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                int wt = in.nextInt();
                if (wt > 0) {
                    adjList.computeIfAbsent(pID[i], k -> new ArrayList<>()).add(new Edge(pID[j],wt));
                }
            }
        }
        Thread electionCoordinator = new Thread(new ElectionCoordinator(pID,adjList,args[1],rootPid));
        electionCoordinator.start();
    }

}
