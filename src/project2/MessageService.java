package project2; /**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * MessageService provides communication service between two adjacent nodes
 */
public class MessageService {
    Map<Integer, List<Integer>> adj;
    HashMap<Integer, DelayQueue<Message>> queueMap;
    HashMap<Integer, Integer> channelDelay;
    Random rand;
    final int MAX_TIME_UNITS = 12;
    final int MIN_TIME_UNITS = 1;
    private int size;



    public MessageService(Map<Integer, List<Integer>> adj, HashMap<Integer, DelayQueue<Message>> queueMap, int n ){
        this.adj = adj;
        this.queueMap = queueMap;
        this.size = n;
        rand = new Random();
        channelDelay = new HashMap<>();
        for(int pid : adj.keySet()){
            int delay = (rand.nextInt(MAX_TIME_UNITS) + MIN_TIME_UNITS)*10000;
            channelDelay.put(pid,delay);
        }

    }
    synchronized public int sendNACK(int senderID, int maxID, Status status, Integer parentID){
        if(parentID!=null){
            LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(senderID));
            Message item = new Message(senderID, maxID, status, delay);
            queueMap.get(parentID).offer(item);
            return 1;
        }
       return 0;
    }

    synchronized public int sendACK(int senderID, int maxID, Status status, int parentID){
        LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(senderID));
        Message item = new Message(senderID, maxID, status, delay);
        queueMap.get(parentID).offer(item);
        return 1;
    }
   synchronized public int sendEXPLORE(int senderId, int maxId, Status status, Integer parentId) {
        for (int neighbor: adj.get(senderId)) {
            if (parentId!=null && parentId == neighbor)
                continue;
            LocalDateTime delay = LocalDateTime.now().plusNanos(channelDelay.get(neighbor));
            Message item = new Message(senderId, maxId, status, delay);
            queueMap.get(neighbor).offer(item);
        }
        return adj.get(senderId).size();
    }

}
