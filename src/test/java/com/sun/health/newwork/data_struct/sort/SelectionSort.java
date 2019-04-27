package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 选择排序
 */
public class SelectionSort implements Sort {
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            int min = arr[i];
            int index = i;
            for (int j = i + 1; j < length; j++) {
                if(arr[j] < min) {
                    min = arr[j];
                    index = j;
                }
            }
            arr[index] = arr[i];
            arr[i] = min;
        }
        return arr;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    @Test
    public void testSelectionSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }
}
