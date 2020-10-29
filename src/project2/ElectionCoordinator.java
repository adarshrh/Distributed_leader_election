package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ElectionCoordinator implements Runnable{


    int[] pID;

    List<Thread> processList = new ArrayList<>();
    int n;
    boolean[][] adjMatrix;
    String outputFilePath;


    ElectionCoordinator(int[] pID, boolean[][] adjMatrix, String outputFilePath){
        this.pID = pID;
        this.n = pID.length;
        this.adjMatrix = adjMatrix;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void run() {
        int diam=getDiameter(adjMatrix,n);
        Process[]  pArr = new Process[n];
        createProcess(diam, pArr);
        addAllNeigbors(pArr);
        for(int i=0; i<n; i++){
            Thread thread = new Thread(pArr[i]);
            thread.start();
        }
        countNumMessages(pArr);

    }

    private void countNumMessages(Process[] pArr) {
        int count =0;
        int totalMessages = 0;
        while(true){
            count = 0;
            for(Process p :pArr ){
                if(p.getLeaderFound()==false){
                    count++;
                }
            }
            if(count == 0){
                for(Process p : pArr){
                    totalMessages+=p.getMsgsCount();
                }
                System.out.println("Total Number of Messages : "+totalMessages);
                System.out.println("==========Leader election completed===========");
                return;
            }
        }
    }

    private void addAllNeigbors(Process[] pArr) {
        for(int i=0; i<n; i++){
            Process p = pArr[i];
            for(int j=0; j<n; j++){
                if(adjMatrix[i][j]){
                    p.addNeighbour(pArr[j]);
                }
            }
        }
    }

    private void createProcess(int diam, Process[] pArr) {
        for(int i=0; i<n; i++){
            pArr[i] = new Process(pID[i], diam,outputFilePath);
        }
    }





    private  int getDiameter(boolean[][] adjMatrix, int n){
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
