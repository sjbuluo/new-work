package com.sun.health.newwork.data_struct.stack;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Stack;

public class TestStack {
    @Test
    public void test1() {
        BaseStack baseStack = new BaseStack(10);
        System.out.println(baseStack.peek());
        System.out.println(baseStack.isEmpty());
        System.out.println(baseStack.isFull());
        baseStack.push(1);
        baseStack.push(2);
        baseStack.push(3);
        baseStack.push(4);
        System.out.println(baseStack);
        System.out.println(baseStack.isEmpty());
        System.out.println(baseStack.isFull());
        System.out.println(baseStack.peek());
        System.out.println(baseStack.pop());
        System.out.println(baseStack.peek());
        System.out.println(baseStack);
        System.out.println(baseStack.isEmpty());
        System.out.println(baseStack.isFull());
    }

    @Test
    public void test2() {
        Stack<Character> stack = new Stack<>();
        for (char c : "Hello World!".toCharArray()) {
            stack.push(c);
        }
        while(!stack.isEmpty()) {
            System.out.print(stack.pop());
        }
        System.out.println();
    }

    public double compute(String str) {
        Stack<Double> numberStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('\0');
        char[] chars = str.toCharArray();
        double number = 0;
        double afterPoint = 0.1;
        boolean point = false;
        for (char c : chars) {
            if(c == ' ') {

            } else if(c >= '0' && c <= '9') {
                if(point) {
                    number = number + (c - '0') * afterPoint;
                    afterPoint /= 10;
                } else {
                    number = number * 10 + c - '0';
                }
            } else if(c == '.') {
                point = true;
            } else {
                char opterator;
                if(c == '(') {
                    operatorStack.push(c);
                } else if(c == ')') {
                    while((opterator = operatorStack.pop()) != '(') {
                        number = compute(numberStack.pop(), number, opterator);
                    }
                    numberStack.push(number);
                } else if(c == '-' || c == '+') {
                    opterator = operatorStack.peek();
                    if(opterator == '\0') {
                        numberStack.push(number);
                        operatorStack.push(c);
                    } else {
                        numberStack.push(compute(numberStack.pop(), number, operatorStack.pop()));
                        operatorStack.push(c);
                    }
                } else if(c == '*' || c == '/') {
                    numberStack.push(number);
                    operatorStack.push(c);
                } else if (c == '\0'){
                    while((opterator = operatorStack.pop()) != '\0') {
                        double before = numberStack.pop();
                        double after = numberStack.pop();
                        numberStack.push(compute(before, after, opterator));
                    }
                }
                number = 0;
                afterPoint = 0.1;
                point = false;
            }
        }
        System.out.println(numberStack);
        System.out.println(operatorStack);
        return numberStack.pop();
    }

    private double compute(double d1, double d2, char operator) {
        switch (operator) {
            case '-':
                return d1 - d2;
            case '+':
                return d1 + d2;
            case '*':
                return d1 * d2;
            case '/':
                return d1 / d2;
            default:
                return  0;
        }
    }

    public int addWithAdd(int a, int b) {
        int tmp = 0;
        while(b != 0) {
            tmp = a ^ b;
            b = (a & b) << 1;
            a = tmp;
        }
        return a;
    }

    @Test
    public void test3() {
        System.out.println(addWithAdd(100, 200));
    }

    @Test
    public void computeTest() {
        System.out.println(compute("1 + (4 - (2 * 3))\0"));
    }
}
