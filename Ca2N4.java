package Auditoriski.AUD4Synchronization.Ispitni;


import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ca2N4 {

    private static Semaphore caHere = new Semaphore(0);
    private static Semaphore nHere = new Semaphore(0);
    private static Semaphore canBond = new Semaphore(0);
    private static Semaphore canExit = new Semaphore(0);

    private static int totalAtoms = 0;

    private static Lock lock = new ReentrantLock();

    @Override
    public void execute() throws InterruptedException{

        caHere.acquire();

        lock.lock();
        totalAtoms++;
        if(totalAtoms == 6){
            // notify other atoms that are waiting to bond
            canBond.release(5);
        }
        lock.unlock();

        canBond.acquire(1);
        state.bond();
        // empty

        lock.lock();
        totalAtoms--;
        if(totalAtoms==0){
            state.validate();
            canExit.release(2);
            // ili canBond.release(), ili prazno..

        }
        lock.unlock();
        canExit.acquire(1);
        caHere.release(1);   // ako ima opcija da go nema ova, to e tocen odgovor...
    }
}
