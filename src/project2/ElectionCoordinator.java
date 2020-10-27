package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
    boolean[][] adjMatrix;
    String outputFilePath;


    ElectionCoordinator(int[] pID, boolean[][] adjMatrix, String outputFilePath){
        this.pID = pID;
        this.n = pID.length;
        this.messageService = new MessageService(n);
        leaderFound = false;
        countingSemaphore = new CountingSemaphore(n);
        this.adjMatrix = adjMatrix;
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
        System.out.println(getDiameter(adjMatrix,n));
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

    public int getDiameter(boolean[][] adjMatrix, int n){
        int diam = 0;
        for(int i=0; i<n; i++){
            diam = Math.max(diam, bfs(i, adjMatrix, n));
        }
        return diam;
    }

    private int bfs(int node, boolean[][] adjMatrix, int n) {
        int diam = -1;
        Queue<Integer> q = new LinkedList<>();
        boolean[] seen = new boolean[n];
        q.add(node);
        seen[node] = true;
        while(!q.isEmpty()){
            int size = q.size();
            diam++;
            for(int i=0; i<size; i++){
                int curVertex = q.poll();
                for(int j=0; j<n; j++){
                    // Adding an unseen node if there is an edge in between
                    if(adjMatrix[curVertex][j] && !seen[j]){
                        q.add(j);
                        seen[j] = true;
                    }
                }
            }
        }
        return diam;
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
        boolean[][] adjMatrix = new boolean[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                int isEdgePresent = in.nextInt();
                if(isEdgePresent == 1) {
                    adjMatrix[i][j] = true;
                }
            }
        }

        Thread electionCoordinator = new Thread(new ElectionCoordinator(pID,adjMatrix,args[1]));
        electionCoordinator.start();
    }

}
