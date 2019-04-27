package com.sun.health.newwork.data_struct.recursion;

import org.junit.Test;

public class RecursionTest {

    public int factorial(int n) {
        if(n == 2) {
            return 2;
        }
        return n * factorial(n - 1);
    }

    @Test
    public void testFactorial() {
        System.out.println(factorial(10));
    }

    public int binarySearch(int left, int right, int[] arr, int search) {
        if(left == right) {
            return -1;
        }
        int middle = (left + right) / 2;
        if(arr[middle] == search) {
            return middle + 1;
        } else if(arr[middle] > search) {
            return binarySearch(left, middle, arr, search);
        } else {
            return binarySearch(middle + 1, right, arr, search);
        }
    }

    @Test
    public void testBinarySearch() {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(binarySearch(0, arr.length, arr, 10));
    }

    public void hannoTower(String from, String to, int height, String middle) {
        if(height == 1) {
            System.out.println(from + " -> " + to);
            return;
        }
        hannoTower(from, middle, height - 1, to);
        System.out.println(from + " -> " + to);
        hannoTower(middle, to , height - 1, from);
    }

    @Test
    public void testHanno() {
        hannoTower("A", "C", 4, "B");
    }

}
