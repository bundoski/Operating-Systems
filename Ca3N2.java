package Auditoriski.AUD4Synchronization.Ispitni;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Ca3N2 {

    static Semaphore n;
    static Semaphore ca;
    static Semaphore nHere;
    static Semaphore caHere;
    static Semaphore ready;
    static Semaphore done;
    static Semaphore leave;
    static int caNum = 0;
    static Semaphore lock;

    public static void init() {
        n = new Semaphore(2);
        ca = new Semaphore(3);
        nHere = new Semaphore(0);
        caHere = new Semaphore(0);
        ready = new Semaphore(0);
        done = new Semaphore(0);
        leave = new Semaphore(0);
        lock = new Semaphore(1);

    }

    public static class Calcium {


        public Calcium(int numRuns) {
        }

        public void bond() {
            System.out.println("O is bonding now.");
        }

        public static void execute() throws InterruptedException {

            ca.acquire();
            lock.acquire();
            caNum++;
            if (caNum == 3) {
                // kod za atom koordinator
                // tuka izvrsuva to so e vo vremenska ramka na koordinator...
                caNum = 0;
                lock.release();
                caHere.release(2);
                nHere.acquire(2);
                ready.release(4);
                // niven metod state.bond();
                done.acquire(4);
                leave.release(4);
            //niven     state.validate();
                ca.release();
            } else {   // kod za ostanatite ca atomi
                lock.release();
                caHere.release();
                ready.acquire();
                // isto state.bond();
                done.release();
                leave.acquire();
                ca.release();
                // Za ova zemame uste eden semafor lock, za da go postavime kaj delo za inkrementiranje na caNum..
            }
        }

    }

    public static class Nitrogen {

/*        public Nitrogen(int numRuns) {
            super(numRuns);
        }*/


        public void execute() throws InterruptedException {
            n.acquire();
            nHere.release();
            ready.acquire(); // x2 n atoms
            //niven metod  state.bond();
            done.release();
            leave.acquire();
            n.release();
        }

    }


  /*  public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }
*/
  /*  public static void run() {
        try {
            Scanner s = new Scanner(System.in);
            int numRuns = 1;
            int numIterations = 100;
            s.close();

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                Nitrogen n = new Nitrogen(numRuns);
                threads.add(n);
                Calcium ca = new Calcium(numRuns);
                threads.add(ca);
                ca = new Calcium(numRuns);
                threads.add(ca);
                n = new Nitrogen(numRuns);
                threads.add(n);
                ca = new Calcium(numRuns);
                threads.add(ca);
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

}