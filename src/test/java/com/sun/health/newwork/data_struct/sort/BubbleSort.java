package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 冒泡排序
 *
 */
public class BubbleSort implements Sort {

    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length - i; j++) {
                if(arr[j -1] > arr[j]) {
                    int tmp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    @Test
    public void testBubbleSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }

}
