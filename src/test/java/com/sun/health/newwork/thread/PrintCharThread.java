package com.sun.health.newwork.thread;

public class PrintCharThread extends Thread {

//    private Thread printNumThread;

    private Object lock;

    public PrintCharThread() {
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                for (int i = 0; i < 26; i++) {
//                this.wait();
                    lock.wait();
                    System.out.print((char)('A' + i) + " ");
//                printNumThread.notify();
                    lock.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public void setPrintNumThread(Thread printNumThread) {
//        this.printNumThread = printNumThread;
//    }


    public void setLock(Object lock) {
        this.lock = lock;
    }
}
