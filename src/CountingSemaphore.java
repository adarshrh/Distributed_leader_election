import java.util.concurrent.Semaphore;

public class CountingSemaphore {
    int size = 0;
    public Semaphore semaphore;

    CountingSemaphore(int n){
        semaphore = new Semaphore(n);
        size =n;
    }
    public synchronized void semaphoreWait(){
        try {
            semaphore.acquire();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void semaphoreSignal(){
            semaphore.release();
    }

    public synchronized boolean isRoundCompleted(){
        if(semaphore.availablePermits()== size){
           return true;
        } else
            return false;
    }
}
