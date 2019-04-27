package com.sun.health.newwork.data_struct.stack;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 利用栈计算四则运算
 */
public class ComputeStack {

    public double compute(String computeStr) {
        computeStr += '\0';
        Stack<Double> numberStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('\0');
        double number = 0;
        double beforePoint = 0;
        double afterPoint = 0.1;
        boolean point = false;
        char operator;
        for (char c : computeStr.toCharArray()) {
            if(c == ' ') {
                continue;
            } if(c >= '0' && c <= '9') {
                if(point) {
                    number += (c - '0') * afterPoint;
                    afterPoint /= 10;
                } else {
                    number = number * 10 + c - '0';
                    beforePoint += 1;
                }
            } else if(c == '.') {
                point = true;
            } else {
                if(c == '(') {
                    operatorStack.push(c);
                } else if(c == ')') {
                    if(beforePoint != 0 || afterPoint != 0.1) {
                        boolean push = false;
                        while((operator = operatorStack.pop()) != '(') {
                            push = true;
                            numberStack.push(compute(number, numberStack.pop(), operator));
                        }
                        if(!push)
                            numberStack.push(number);
                    } else {
                        while((operator = operatorStack.pop()) != '(') {
                            numberStack.push(compute(numberStack.pop(), numberStack.pop(), operator));
                        }
                    }
                } else if(c == '*' || c == '/') {
                    operator = operatorStack.peek();
                    if(operator == '*' || operator == '/') {
                        numberStack.push(compute(number, numberStack.pop(), operatorStack.pop()));
                    } else {
                        if(beforePoint != 0 || afterPoint != 0.1) {
                            numberStack.push(number);
                        }
                    }
                    operatorStack.push(c);
                } else if(c == '+' || c == '-') {
                    operator = operatorStack.peek();
                    if(operator != '\0' && operator != '(') {
                        numberStack.push(compute(number, numberStack.pop(), operatorStack.pop()));
                    } else {
                        if(beforePoint != 0 || afterPoint != 0.1) {
                            numberStack.push(number);
                        }
                    }
                    operatorStack.push(c);
                } else if(c == '\0') {
                    if(beforePoint != 0 || afterPoint != 0.1) {
                        numberStack.push(number);
                    }
                    while((operator = operatorStack.pop()) != '\0') {
                        numberStack.push(compute(numberStack.pop(), numberStack.pop(), operator));
                    }
                } else {

                }
                number = 0;
                beforePoint = 0;
                afterPoint = 0.1;
                point = false;
            }
        }
        return numberStack.pop();
    }

    private double compute(double b, double a, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                return 0;
        }
    }

    @Test
    public void test1() {
        System.out.println(compute("((1))"));
        System.out.println(compute("1 + (2 * (4 - 5))"));
        System.out.println(compute("2 * 3"));
        System.out.println(compute("2 / 4"));
        System.out.println(compute("(1 + 2) * 3 - 4 / 5"));
    }


    /**
     * 学到的中缀转前缀
     * @param computeStr
     * @return
     */
    public double studyMiddleToBackCompute(String computeStr) {
        Queue<Object> queue = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('\0');
        computeStr += '\0';
        double number = 0;
        int beforePoint = 1;
        double afterPoint = 0.1;
        boolean point = false;
        char operator;
        for (char c : computeStr.toCharArray()) {
            if (c == ' ') {
                continue;
            } else if(c >= '0' && c <= '9') {
                if(point) {
                    number = number + (c - '0') * afterPoint;
                    afterPoint /= 10;
                } else {
                    number = number * 10 + c - '0';
                    beforePoint += 1;
                }
            } else {
                if(beforePoint != 1 || afterPoint != 0.1) {
                    queue.add(number);
                    number = 0;
                    beforePoint = 1;
                    afterPoint = 0.1;
                    point = false;
                }
                if(c == '(') {
                    operatorStack.push(c);
                } else if(c == ')') {
                    while((operator = operatorStack.pop()) != '(') {
                        queue.add(operator);
                    }
                } else if(c == '\0') {

                } else {
                    while(true) {
                        operator = operatorStack.peek();
                        if(operator == '\0' || operator == '(') {
                            operatorStack.push(c);
                            break;
                        } else if(operatorLevel(c) > operatorLevel(operator)) {
                            operatorStack.push(c);
                            break;
                        } else {
                            queue.add(operatorStack.pop());
                        }
                    }
                }
            }
        }
        while((operator = operatorStack.pop()) != '\0') {
            queue.add(operator);
        }
        System.out.println(queue);
        Stack<Double> numberStack = new Stack<>();
        while(!queue.isEmpty()) {
            Object obj = queue.remove();
            if(obj instanceof Double) {
                numberStack.push((Double) obj);
            } else {
                numberStack.push(compute(numberStack.pop(), numberStack.pop(), (char) obj));
            }
        }
        return numberStack.pop();
    }

    public int operatorLevel(char operator) {
        switch (operator) {
            case '+':
                return 1;
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
            case '\0':
                return 0;
            default:
                return -1;
        }
    }

    @Test
    public void test2() {
        System.out.println(studyMiddleToBackCompute("((1))"));
        System.out.println(studyMiddleToBackCompute("1 + (2 * (4 - 5))"));
        System.out.println(studyMiddleToBackCompute("2 * 3"));
        System.out.println(studyMiddleToBackCompute("2 / 4"));
        System.out.println(studyMiddleToBackCompute("(1 + 2) * 3 - 4 / 5"));
    }



}
