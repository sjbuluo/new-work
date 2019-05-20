package com.sun.health.newwork.interview.java.network.thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Thinker implements Runnable {

    private Kuaizi left;

    private Kuaizi right;

    private static int i = 1;

    private final int id;

    private final int ponderFactor;

    private Random rand = new Random();

    public Thinker(Kuaizi left, Kuaizi right, int ponder) {
        this.left = left;
        this.right = right;
        id = i++;
        this.ponderFactor = ponder;
    }

    public void pause() throws InterruptedException {
        if (ponderFactor == 0)
            return ;
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }


    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(id + " 想");
                pause();
                System.out.println(id + " 拿起左边筷子");
                left.take();
                System.out.println(id + " 拿起右边筷子");
                right.take();
                System.out.println(id + " 吃");
                pause();
                left.put();
                Thread.yield();
                right.put();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
