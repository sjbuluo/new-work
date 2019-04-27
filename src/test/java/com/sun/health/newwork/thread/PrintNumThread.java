package com.sun.health.newwork.thread;

public class PrintNumThread extends Thread {

//    private Thread printCharThread;

    private Object lock;

    @Override
    public void run() {
        synchronized (lock) {
            try {
                for (int i = 1; i < 53; ) {
                    System.out.print(i++);
                    System.out.print(i++);
//                printCharThread.notify();
//                this.wait();
                    lock.notify();
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public void setPrintCharThread(Thread printCharThread) {
//        this.printCharThread = printCharThread;
//    }


    public void setLock(Object lock) {
        this.lock = lock;
    }
}
