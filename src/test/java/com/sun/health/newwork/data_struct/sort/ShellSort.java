package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 希尔排序
 */
public class ShellSort implements Sort {
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for (int gap = length / 2; gap > 0; gap = gap / 2) {
            for (int i = gap; i < length; i++) {
                int current = arr[i];
                int index=  i - gap;
                while(index >= 0 && arr[index] > current) {
                    arr[index + gap] = arr[index];
                    index = index - gap;
                }
                arr[index + gap] = current;
            }
        }
        return arr;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    @Test
    public void testShellSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }
}
