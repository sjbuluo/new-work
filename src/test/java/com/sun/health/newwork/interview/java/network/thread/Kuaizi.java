package com.sun.health.newwork.interview.java.network.thread;

public class Kuaizi {

    private boolean taken = false;

    public synchronized void take() throws InterruptedException {
        while (taken)
            wait();
        taken = true;
    }

    public synchronized void put() {
        taken = false;
        notifyAll();
    }


}
