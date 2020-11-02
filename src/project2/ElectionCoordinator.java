package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.DelayQueue;


public class ElectionCoordinator implements Runnable{


    int[] pID;
    Map<Integer, List<Integer>> adjList = new HashMap();
    HashMap<Integer, DelayQueue<Message>> queueMap = new HashMap<>();
    MessageService messageService;
    int n;
    String outputFilePath;
    Process[] processes;


    ElectionCoordinator(int[] pID, Map<Integer, List<Integer>> adjList, String outputFilePath){
        this.pID = pID;
        this.n = pID.length;
        this.adjList = adjList;
        this.outputFilePath = outputFilePath;
        for (int i = 0; i < n; i++) {
            queueMap.put(pID[i], new DelayQueue<>());
        }
        messageService = new MessageService(adjList,queueMap,n);
        processes = new Process[n];
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
                Thread.sleep(10);
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

    private void createProcess(Thread[] threads) {
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(pID[i], messageService, adjList.get(pID[i]), queueMap.get(pID[i]),outputFilePath);
            threads[i] = new Thread(processes[i]);
        }

        for (Thread th: threads) {
            th.start();
        }
    }

    public void writeOutput(String output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath,true));
        writer.write(output);
        writer.newLine();
        writer.flush();
        writer.close();
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

        HashMap<Integer, List<Integer>> adjList = new HashMap();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if (in.nextInt() == 1) {
                    adjList.computeIfAbsent(pID[i], k -> new ArrayList<>()).add(pID[j]);
                }
            }
        }
        Thread electionCoordinator = new Thread(new ElectionCoordinator(pID,adjList,args[1]));
        electionCoordinator.start();
    }

}
