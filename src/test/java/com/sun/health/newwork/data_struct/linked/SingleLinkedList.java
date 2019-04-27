package com.sun.health.newwork.data_struct.linked;

import org.junit.Test;

public class SingleLinkedList {
    public static class SingleLinkedListNode {
        private Object data;
        private SingleLinkedListNode next;

        public SingleLinkedListNode() {
        }

        public SingleLinkedListNode(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public SingleLinkedListNode getNext() {
            return next;
        }

        public void setNext(SingleLinkedListNode next) {
            this.next = next;
        }
    }

    private SingleLinkedListNode root = new SingleLinkedListNode();

    public void insert(Object object) {
        SingleLinkedListNode currentNode = root;
        while(currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
        }
        currentNode.setNext(new SingleLinkedListNode(object));
    }

    public boolean remove(Object object) {
        SingleLinkedListNode currentNode = root;
        while(currentNode.getNext() != null) {
            if(currentNode.getNext().getData().equals(object)) {
                currentNode.setNext(currentNode.getNext().getNext());
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    public boolean find(Object object) {
        SingleLinkedListNode currentNode = root;
        while(currentNode.getNext() != null) {
            if(currentNode.getNext().getData().equals(object)) {
                return true;
            }
        }
        return false;
    }

    public void push(Object object) {
        this.insert(object);
    }

    public Object pop() {
        if(!isEmpty()) {
            Object result = root.getNext().getData();
            root.setNext(root.getNext().getNext());
            return result;
        }
        return null;
    }

    public Object peek() {
        if(!isEmpty()) {
            Object result = root.getNext().getData();
            return result;
        }
        return null;
    }

    public int size() {
        SingleLinkedListNode currentNode = root;
        int size = 0;
        while(currentNode.getNext() != null) {
            size++;
            currentNode = currentNode.getNext();
        }
        return size;
    }

    @Override
    public String toString() {
        SingleLinkedListNode currentNode = root;
        StringBuilder stringBuilder = new StringBuilder("[");
        while(currentNode.getNext() != null) {
            stringBuilder.append(currentNode.getNext().getData());
            currentNode = currentNode.getNext();
            if(currentNode.getNext() != null) {
                stringBuilder.append("->");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean isEmpty() {
        return this.root.getNext() == null;
    }

    @Test
    public void test1() {
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        System.out.println(singleLinkedList);
        singleLinkedList.insert(1);
        System.out.println(singleLinkedList);
        singleLinkedList.insert(2);
        System.out.println(singleLinkedList);
        singleLinkedList.insert(3);
        System.out.println(singleLinkedList);
        singleLinkedList.remove(2);
        System.out.println(singleLinkedList);
        singleLinkedList.remove(1);
        System.out.println(singleLinkedList);
        singleLinkedList.remove(3);
        System.out.println(singleLinkedList);
    }
    
    @Test
    public void test2() {
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        System.out.println(singleLinkedList);
        singleLinkedList.push(1);
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
        singleLinkedList.push(2);
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
        singleLinkedList.push(3);
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.pop());
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.pop());
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.pop());
        System.out.println(singleLinkedList.size());
        System.out.println(singleLinkedList);
    }
}
