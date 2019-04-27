package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 插入排序
 */
public class InsertionSort implements Sort {
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            int current = arr[i];
            for (int j = i; j > 0; j--) {
                if(arr[j - 1] > current) {
                    arr[j] = arr[j - 1];
                    if(j == 1) {
                        arr[0] = current;
                    }
                } else {
                    arr[j] = current;
                    break;
                }
            }
        }
        return arr;
    }

    public int[] studySort(int[] arr) {
        int length = 0;
        for (int i = 1; i < length; i++) {
            int current = arr[i];
            int index= i - 1;
            while(index >=0 && arr[index] > current) {
                arr[index + 1] = arr[index];
                index--;
            }
            arr[index+1] = current;
        }
        return arr;
    }

    @Test
    public void testInsertionSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
        System.out.println(Arrays.toString(studySort(arr)));
    }
}
