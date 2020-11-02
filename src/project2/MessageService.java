package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * This class acts as a link between two adjacent process
 */
public class MessageService {
    Map<Integer, List<Integer>> adj;
    HashMap<Integer, DelayQueue<Message>> queueMap;
    HashMap<Integer, Integer> channelDelay;
    boolean isLeaderElected = false;
    Random rand;
    final int FACTOR = 10000;
    final int MAX_TIME_UNITS = 12;
    final int MIN_TIME_UNITS = 1;
    private int size;

    Set<Integer> rejectMap = new HashSet<>();
    Set<Integer> completeMap = new HashSet<>();




    public MessageService(Map<Integer, List<Integer>> adj, HashMap<Integer, DelayQueue<Message>> queueMap, int n ){
        this.adj = adj;
        this.queueMap = queueMap;
        this.size = n;
        rand = new Random();
        channelDelay = new HashMap<>();
        for(int pid : adj.keySet()){
            //int delay = (rand.nextInt(MAX_TIME_UNITS) + MIN_TIME_UNITS)*FACTOR;
            int delay = 10000;
            channelDelay.put(pid,delay);
        }

    }
    synchronized public int sendNACK(int senderID, int maxID, Status status, Integer parentID){
        if(parentID!=null){
            LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(senderID));
            Message item = new Message(senderID, maxID, status, delay);
            // Add item to the neighbor's queue
            queueMap.get(parentID).offer(item);
            return 1;
        }
       return 0;
    }

    synchronized public int sendACK(int senderID, int maxID, Status status, int parentID){
        LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(senderID));
        Message item = new Message(senderID, maxID, status, delay);
        // Add item to the neighbor's queue
        queueMap.get(parentID).offer(item);
        return 1;
    }
   synchronized public int sendEXPLORE(int senderId, int maxId, Status status, Integer parentId) {
               // Send messages to neighbors
        for (int neighbor: adj.get(senderId)) {
            if (parentId!=null && parentId == neighbor)
                continue;
            // Generate random numbers
            LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(neighbor));
            Message item = new Message(senderId, maxId, status, delay);
            // Add item to the neighbor's queue
            queueMap.get(neighbor).offer(item);
        }
        return adj.get(senderId).size();
    }

    // Add message counts of each node to a hashmap

    public boolean isLeaderElected() {
        return isLeaderElected;
    }

    public void setLeaderElected(boolean leaderElected) {
        isLeaderElected = leaderElected;
    }

    public int getCompleteCount(int id){
        return completeMap.size();
    }

    public int getRejectCount(int id){
         return rejectMap.size();
    }


}
