import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ElectionCoordinator implements Runnable{
    boolean leaderFound;
    int[] pID;
    MessageService messageService;
    CountingSemaphore countingSemaphore;
    List<Thread> processList = new ArrayList<>();
    int n;

    ElectionCoordinator(int[] pID){
        this.pID = pID;
        this.n = pID.length;
        this.messageService = new MessageService(n);
        leaderFound = false;
        countingSemaphore = new CountingSemaphore(n);
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

    public void createProcess(){
        for(int i=0;i<n;i++){
            Thread process = new Thread(new Process(i,pID[i],countingSemaphore, messageService));
            process.setName(String.valueOf(pID[i]));
            countingSemaphore.semaphoreWait();
            process.start();
            processList.add(process);
        }
    }

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
                        countingSemaphore.semaphoreWait();
                    }
                }
                if(this.countingSemaphore.isRoundCompleted()) this.leaderFound = true;
                countingSemaphore.notifyAll();
            }
        }
    }
    public static void main(String[] args)  {
        Scanner in = null;
        try {
        if (args.length > 0) {
                in =  new Scanner(new File(args[0]));
        } else {
            System.out.println("Please provide input file path as command line argument to the program");
            System.exit(1);
        }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found. Please provide absolute file path as command line argument to the program ");
            e.printStackTrace();
            System.exit(1);
        }
        int n = in.nextInt();
        int[] pID = new int[n];
        for(int i=0;i<n;i++){
            pID[i] = in.nextInt();
        }
        Thread electionCoordinator = new Thread(new ElectionCoordinator(pID));
        electionCoordinator.start();
    }

}
