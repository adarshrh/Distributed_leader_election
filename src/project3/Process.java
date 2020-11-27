package project3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * Group Members:
 * Adarsh Raghupti Hegde  axh190002
 * Akash Akki            apa190001
 */

class Process implements Runnable {
    Integer pid;
    int maxUID;
    DelayQueue<Message> queue;
    Status status;
    int messageCount;

    int NACK_COUNT;
    int ACK_COUNT;
    int neighborCount;
    Integer parentId;
    int dist;

    Set<Integer> others = new HashSet<>();
    Set<Integer> children = new HashSet<>();
    Set<Integer> terminatedChild = new HashSet<>();
    Map<Integer,Integer> nbrs;
    MessageService messageService;
    String outputPath;
    boolean isRoot;
    CountingSemaphore countingSemaphore;

    public Process(int id, MessageService messageService, List<Edge> neighbors, DelayQueue<Message> queue, String outputPath, boolean isRoot, CountingSemaphore countingSemaphore
    ) {
        this.pid = id;
        this.maxUID = id;
        this.NACK_COUNT = 0;
        this.ACK_COUNT = 0;
        this.messageService = messageService;
        this.neighborCount = neighbors.size();
        this.queue = queue;
        this.parentId = null;
        this.outputPath = outputPath;
        this.isRoot = isRoot;
        this.status = Status.UNKNOWN;
        nbrs = new HashMap<>();
        for(Edge e : neighbors){
            nbrs.put(e.toVertex,e.weight);
        }
        this.countingSemaphore = countingSemaphore;
        if(isRoot){
            dist = 0;
        }
        else dist = Integer.MAX_VALUE;
        this.messageCount = 0;
    }

    @Override
    public void run() {
        if(isRoot){
           messageCount += messageService.sendEXPLORE(pid,0,Status.EXPLORE,null);
          // countingSemaphore._signal();
        }

        while (true){
            countingSemaphore._wait();
            int count = neighborCount;
            try{
                while (!countingSemaphore.isRoundCompleted()){
//                    if(isRoot)
//                        System.out.println("Root Process");
                    Message message = queue.take();
                    int sender = message.getSenderID();
                    int distFromSender = message.getDist();
                    Status st = message.getStatus();
                    if(st.equals(Status.EXPLORE)){
                        if(dist > (distFromSender+nbrs.get(sender))){
                            dist = distFromSender+nbrs.get(sender);
                            if(parentId!=null && parentId!=sender)
                                messageCount += messageService.sendNACK(pid,dist,Status.NACK,parentId);
                            parentId = sender;
                            messageCount += messageService.sendACK(pid,dist,Status.ACK,parentId);
                            messageCount += messageService.sendEXPLORE(pid,dist,Status.EXPLORE,parentId);
                        } else {
                            messageCount += messageService.sendNACK(pid,dist,Status.NACK,sender);
                        }
                    } else if(st.equals(Status.ACK)){
                        if(parentId==null || sender!=parentId){
                            children.add(sender);
                            if(others.contains(sender))
                                others.remove(sender);
                        }

                    } else if(st.equals(Status.NACK)){
                        if(parentId==null || sender!=parentId){
                            others.add(sender);
                            if(children.contains(sender))
                                children.remove(sender);
                        }


                    } else if(st.equals(Status.CONVERGECAST)){
                        terminatedChild.add(sender);
                       //System.out.println("pid:"+pid+" terminated:"+terminatedChild.toString()+" child:"+children.toString()+" other:"+others.toString() +" parent:"+parentId);
                        if(parentId!=null  && terminatedChild.size()==children.size() && (children.size()+others.size()==neighborCount-1)){
                         //   System.out.println("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist);
                            messageCount += messageService.sendConverge(pid,dist,Status.CONVERGECAST,parentId);
                            status = Status.TERMINATE;
                            countingSemaphore._signal();
                            break;
                        } else if(parentId==null  && (children.size()+others.size()==neighborCount)){
                           // System.out.println("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist);
                           // System.out.println("Childrens:"+children.toString());
                           // System.out.println("Parent:"+parentId);
                            status = Status.TERMINATE;
                            countingSemaphore._signal();
                            break;
                        } else{
//                            System.out.println("My id:"+pid+" Distance to root:"+dist);
//                            System.out.println("Childrens:"+children.toString());
//                            System.out.println("Others:"+others.toString());
//                            System.out.println("Parent:"+parentId);
                            //System.out.println("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist+ " terminated:"+terminatedChild.toString());
                            messageCount += messageService.sendEXPLORE(pid,dist,Status.EXPLORE,parentId);
                        }
                    }

                    if(parentId!=null && (others.size()==neighborCount-1)){
                      //entId);
                       // System.out.println("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist);
                        messageCount += messageService.sendConverge(pid,dist,Status.CONVERGECAST,parentId);
                        status = Status.TERMINATE;
                        countingSemaphore._signal();
                        break;
                    }

                }
                if(status.equals(Status.TERMINATE)){
                    System.out.println("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist);
                    writeOutput("My id:"+pid+" parentId:"+parentId+" Distance to root:"+dist);
                    break;
                }
                synchronized (countingSemaphore){
                    try{
                        countingSemaphore.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       // System.out.println("End of pid:"+pid);
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void writeOutput(String output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath,true));
        writer.write(output);
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
