package com.sun.health.newwork.algorithm;

import org.junit.Test;

import java.util.Arrays;

public class FibonacciQueue {

    public int fibonacciWithRecursion(int n) {
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        return fibonacciWithRecursion(n - 1) + fibonacciWithRecursion(n - 2);
    }

    public int fibonacciWithoutRecursion(int n) {
        if(n <= 0 )
            return 0;
        int num1 = 0;
        int tmp = 0;
        int result = 1;
        for (int i = 1; i < n; i++) {
            tmp = result;
            result += num1;
            num1 = tmp;
        }
        return result;
    }

    public int fibonacciWithArray(int n) {
        if (n < 0)
            return 0;
        if (n < 2) {
            return n;
        }
        int[] arr = new int[n + 1];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }

    @Test
    public void testFibonacci() {
        for (int i = 0; i < 10; i++) {
            System.out.println(fibonacciWithoutRecursion(i) + " - " + fibonacciWithRecursion(i) + " - " + fibonacciWithArray(i) + " - ");
        }
    }



}
