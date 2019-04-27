package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class QuickSort implements Sort {
    @Override
    public int[] sort(int[] arr) {
        quickSort(0, arr.length, arr);
        return arr;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    public void quickSort(int start, int end, int[] arr) {
        if(end - start <= 1) {
            return;
        }
        int middle = start + 1;
        for (int i = start + 1; i < end; i++) {
            if(arr[i] < arr[start]) {
                swap(i, middle++, arr);
            }
        }
        int pivot = arr[start];
        for (int i = start + 1; i < middle; i++) {
            arr[i - 1] = arr[i];
        }
        arr[middle - 1] = pivot;
        quickSort(start, middle, arr);
        quickSort(middle, end, arr);
    }

    private void swap(int i, int j, int[] arr) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @Test
    public void testMergeSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }
}
