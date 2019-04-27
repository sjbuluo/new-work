package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class ShellSort2 implements Sort {
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for (int i = 3; i > 0; i--) {
            for (int j = i; j < length; j++) {
                int current = arr[j];
                int index = j - i;
                while(index >= 0 && current < arr[index]) {
                    swap(index + i, index, arr);
                    index -= i;
                }
                arr[index + i] = current;
            }
        }
        return arr;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    public void swap(int a, int b, int[] arr) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    @Test
    public void test1() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }
}
