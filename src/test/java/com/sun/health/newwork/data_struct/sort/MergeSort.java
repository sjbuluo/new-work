package com.sun.health.newwork.data_struct.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class MergeSort implements Sort {

    public int[] sortWithArr(int[] arr) {
        if(arr.length == 1) {
            return arr;
        }
        int middle = arr.length / 2;
        int[] left = new int[middle];
        int[] right = new int[arr.length - middle];
        for (int i = 0; i < middle; i++) {
            left[i] = arr[i];
        }
        for (int i = middle; i < arr.length; i++) {
            right[i - middle] = arr[i];
        }
      return mergeArrWithArr(left, right);
    }

    public int[] sortWithIndex(int[] arr) {
        mergeArrWithIndex(0, arr.length / 2, arr.length, arr);
        return arr;
    }

    @Override
    public int[] sort(int[] arr) {
        return new int[0];
    }

    @Override
    public int[] studySort(int[] arr) {
        return new int[0];
    }


    /**
     *
     * @param left
     * @param right
     * @param arr
     */
    private void mergeArrWithIndex(int left, int middle, int right, int[] arr) {
        if(middle - left > 1) {
            mergeArrWithIndex(left, left + (middle - left) / 2, middle, arr);
        }
        if(right - middle > 1) {
            mergeArrWithIndex(middle, middle + (right - middle) / 2, right, arr);
        }
        int[] tmp = new int[right - left];
        int lIndex = left;
        int rIndex = middle;
        int index = 0;
        while(lIndex < middle && rIndex < right) {
            if(arr[lIndex] < arr[rIndex]) {
                tmp[index++] = arr[lIndex++];
            } else {
                tmp[index++] = arr[rIndex++];
            }
        }
        while(lIndex < middle) {
            tmp[index++] = arr[lIndex++];
        }
        while(rIndex < right) {
            tmp[index++] = arr[rIndex++];
        }
        for (int i = 0; i < tmp.length; i++) {
            arr[left + i] = tmp[i];
        }
    }

    /**
     * 使用数组归并
     * @param left
     * @param right
     * @return
     */
    private int[] mergeArrWithArr(int[] left, int[] right) {
        if(left.length > 1) {
            int middle = left.length / 2;
            int[] ll = new int[middle];
            int[] lr = new int[left.length - middle];
            for (int i = 0; i < middle; i++) {
                ll[i] = left[i];
            }
            for (int i = middle; i < left.length; i++) {
                lr[i - middle] = left[i];
            }
            left = mergeArrWithArr(ll, lr);
        }
        if(right.length > 1) {
            int middle = right.length / 2;
            int[] rl = new int[middle];
            int[] rr = new int[right.length - middle];
            for (int i = 0; i < middle; i++) {
                rl[i] = right[i];
            }
            for (int i = middle; i < right.length; i++) {
                rr[i - middle] = right[i];
            }
            right = mergeArrWithArr(rl, rr);
        }
        int[] result = new int[left.length + right.length];
        int lIndex = 0;
        int rIndex= 0;
        while(lIndex < left.length && rIndex < right.length) {
            if(left[lIndex] < right[rIndex]) {
                result[lIndex + rIndex] = left[lIndex];
                lIndex++;
            } else {
                result[lIndex + rIndex] = right[rIndex];
                rIndex++;
            }
        }
        while(lIndex < left.length) {
            result[lIndex + rIndex] = left[lIndex];
            lIndex++;
        }
        while(rIndex < right.length) {
            result[lIndex + rIndex] = right[rIndex];
            rIndex++;
        }
        return result;
    }

    @Test
    public void testMergeSort() {
        Random random = new Random();
        int[] arr = new int[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(sortWithArr(arr)));
        System.out.println(Arrays.toString(sortWithIndex(arr)));
    }

}
