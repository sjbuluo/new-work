package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;

public class InselectionSort2 implements Sort {
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length;
        for(int i = 1; i < length; i++) {
            int index = i - 1;
            int current = arr[i];
            while(index >= 0 && current < arr[index]) {
                swap(index+1, index, arr);
                index--;
            }
            arr[index + 1] = current;
        }
        return arr;
    }

    public void swap(int a, int b, int[] arr) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }

    @Test
    public void test1() {
        int[] arr = new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sort(arr)));
    }
}
