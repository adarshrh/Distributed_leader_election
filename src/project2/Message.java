package project2;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class Message  implements Delayed {

    private int pID;
    private long timeTaken;
    private long delayedTime;
    private int round;
    private static final int f = 100;

    public Message(int pID,long delayedTime,int round) {
        this.pID = pID;
        this.timeTaken =System.currentTimeMillis()
            + delayedTime*f;
        this.delayedTime = delayedTime;

        this.round= round;
    }


    @Override
    public long getDelay(TimeUnit unit) {
        long d = timeTaken - System.currentTimeMillis();
        return unit.convert(d, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.timeTaken > ((Message)o).timeTaken) {
            return 1;
        }
        else if (this.timeTaken < ((Message)o).timeTaken) {
            return -1;
        }
        return 0;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public long getDelayedTime() {
        return delayedTime;
    }

    public void setDelayedTime(long delayedTime) {
        this.delayedTime = delayedTime;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
