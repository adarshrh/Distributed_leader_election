package project3;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class Message implements Delayed {
  private int dist;
  private int senderUID;
  private LocalDateTime localDateTime;
  private Status status;

  public Message(int senderUID, int dist, Status status, LocalDateTime localDateTime) {
    super();
    this.dist = dist;
    this.senderUID = senderUID;
    this.status = status;
    this.localDateTime = localDateTime;
  }

  public int getSenderID() {
    return senderUID;
  }

  public int getDist() {
    return dist;
  }

  public Status getStatus() {
    return status;
  }


  @Override
  public int compareTo(Delayed d) {
    long result = this.getDelay(TimeUnit.NANOSECONDS) - d.getDelay(TimeUnit.NANOSECONDS);
    if (result < 0) {
      return -1;
    } else if (result > 0) {
      return 1;
    }
    return 0;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    LocalDateTime now = LocalDateTime.now();
    long diff = now.until(localDateTime, ChronoUnit.MILLIS);
    return unit.convert(diff, TimeUnit.MILLISECONDS);
  }

}
