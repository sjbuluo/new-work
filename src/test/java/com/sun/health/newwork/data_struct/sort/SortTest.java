package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class SortTest {

    @Test
    public void testSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(new BubbleSort().sort(arr)));
    }

}
