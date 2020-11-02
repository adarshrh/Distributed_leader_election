package project2;

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
    Integer uid;
    int maxUID;
    DelayQueue<Message> queue;
    Status status;
    int messageCount;

    int NACK_COUNT;
    int ACK_COUNT;
    int neighborCount;
    Integer parentId;

    Set<Integer> NACK_SET = new HashSet<>();
    Set<Integer> ACK_SET = new HashSet<>();
    MessageService messageService;
    String outputPath;

    public Process(int id, MessageService messageService, List<Integer> neighbors, DelayQueue<Message> queue, String outputPath) {
        this.uid = id;
        this.maxUID = id;
        this.NACK_COUNT = 0;
        this.ACK_COUNT = 0;
        this.messageService = messageService;
        this.neighborCount = neighbors.size();
        this.queue = queue;
        this.parentId = uid;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        try {
            messageCount += messageService.sendEXPLORE(uid, maxUID, Status.EXPLORE, null);
            while (true) {
              Message message = queue.take();
              Integer senderID = message.getSenderID();
              Integer maxAtSender = message.getMaxId();
              Status messageStatus = message.getStatus();
              if(messageStatus.equals(Status.EXPLORE)){
                  if (maxAtSender > maxUID) {
                      if (maxUID != uid ) {
                          System.out.println("Found new maxID: Send NACK to old parent");
                          System.out.println("Sending NACK from:"+uid+" to:"+parentId);
                          messageCount+=messageService.sendNACK(uid, maxAtSender, Status.NACK,  parentId);
                      }
                      this.parentId = senderID;
                      this.maxUID = maxAtSender;
                      // send explore to neighbors except to parent node
                      System.out.println("Found new maxID: Send EXPLORE to all neighbors except parent");
                      messageCount+=messageService.sendEXPLORE(uid, maxUID, Status.EXPLORE, parentId);

                  } else {
                     messageCount+= messageService.sendNACK(uid, maxUID, Status.NACK, senderID);
                  }
              }
              else if (messageStatus.equals(Status.ACK) || messageStatus.equals(Status.NACK)) {
                  if (messageStatus.equals(Status.ACK)) {
                      ACK_SET.add(senderID);
                  }
                  else NACK_SET.add(senderID);
                  if ((NACK_SET.size() == neighborCount || (NACK_SET.size() + ACK_SET.size() == neighborCount)) && parentId != uid) {
                      // update parent that its complete
                      messageCount+=messageService.sendACK(uid, maxUID, Status.ACK,  parentId);
                  } else if (ACK_SET.size() == neighborCount) {

                      // let master know that leader got elected
                      status=Status.LEADER;

                      // update all neighbors about leader election
                      System.out.println("My ID:"+uid+" Leader ID:"+maxUID);
                      writeOutput("My ID:"+uid+" Leader ID:"+maxAtSender);
                      messageCount+=messageService.sendEXPLORE(uid, maxUID, Status.LEADER, null);
                      break;
                  }
              } else if (messageStatus.equals(Status.LEADER)) {
                  // Notify all process of leader being elected
                  messageCount+=messageService.sendEXPLORE(uid, maxUID, Status.LEADER, senderID);
                  System.out.println("My ID:"+uid+" Leader ID:"+maxAtSender);
                  writeOutput("My ID:"+uid+" Leader ID:"+maxAtSender);
                  break;
              }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
