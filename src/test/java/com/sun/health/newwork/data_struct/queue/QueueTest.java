package com.sun.health.newwork.data_struct.queue;

import org.junit.Test;

import java.util.Random;

public class QueueTest {

    @Test
    public void test1() {
        UnidirectionalQueue unidirectionalQueue = new UnidirectionalQueue();
        System.out.println(unidirectionalQueue.isFull());
        System.out.println(unidirectionalQueue.isEmpty());
        System.out.println(unidirectionalQueue);
        unidirectionalQueue.insert(1);
        unidirectionalQueue.insert(2);
        unidirectionalQueue.insert(3);
        unidirectionalQueue.insert(4);
        unidirectionalQueue.insert(5);
        System.out.println(unidirectionalQueue.isEmpty());
        System.out.println(unidirectionalQueue.isFull());
        System.out.println(unidirectionalQueue);
        unidirectionalQueue.insert(6);
        unidirectionalQueue.insert(7);
        unidirectionalQueue.insert(8);
        unidirectionalQueue.insert(9);
        unidirectionalQueue.insert(10);
        System.out.println(unidirectionalQueue.isFull());
        System.out.println(unidirectionalQueue.isEmpty());
        System.out.println(unidirectionalQueue);
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        System.out.println(unidirectionalQueue.isEmpty());
        System.out.println(unidirectionalQueue.isFull());
        System.out.println(unidirectionalQueue);
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        unidirectionalQueue.remove();
        System.out.println(unidirectionalQueue.isFull());
        System.out.println(unidirectionalQueue.isEmpty());
        System.out.println(unidirectionalQueue);
    }

    @Test
    public void test2() {
        TwoWayQueue twoWayQueue = new TwoWayQueue();
        twoWayQueue.insertTail(1);
        System.out.println(twoWayQueue);
        System.out.println(twoWayQueue.removeTail());
        System.out.println(twoWayQueue);

    }

    @Test
    public void test3() {
        PriorityQueue priorityQueue = new PriorityQueue();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomInt = random.nextInt(100);
            System.out.println(randomInt);
            priorityQueue.insert(randomInt);
            System.out.println(priorityQueue);
        }
        System.out.println(priorityQueue.remove());
        System.out.println(priorityQueue);
        priorityQueue.insert(random.nextInt(100));
        System.out.println(priorityQueue);
    }
}
