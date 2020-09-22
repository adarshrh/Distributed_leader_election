/**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */
import java.util.concurrent.Semaphore;

/**
 * This class maintains set of permits. Initialized with n permits.
 *
 */
public class CountingSemaphore {
    int size = 0;
    public Semaphore semaphore;

    CountingSemaphore(int n){
        semaphore = new Semaphore(n);
        size =n;
    }

    /**
     * This function acquires a permit from this semaphore, blocking until one is available
     */
    public synchronized void _wait(){
        try {
            semaphore.acquire();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Releases a permit, returning it to the semaphore
     */
    public synchronized void _signal(){
            semaphore.release();
    }

    /**
     * Checks if the execution of round is completed.
     * @return
     */
    public synchronized boolean isRoundCompleted(){
        //If the number of available permits is equal to the number of processes then the round is completed
        if(semaphore.availablePermits()== size){
           return true;
        } else
            return false;
    }
}
