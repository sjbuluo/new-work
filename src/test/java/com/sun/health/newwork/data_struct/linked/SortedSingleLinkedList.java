package com.sun.health.newwork.data_struct.linked;

import org.junit.Test;

public class SortedSingleLinkedList {
    private class SortedSingleLinkedListNode {
        private int data;
        private SortedSingleLinkedListNode next;

        public SortedSingleLinkedListNode() {
        }

        public SortedSingleLinkedListNode(int data) {
            this.data = data;
        }

        public SortedSingleLinkedListNode(int data, SortedSingleLinkedListNode next) {
            this.data = data;
            this.next = next;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public SortedSingleLinkedListNode getNext() {
            return next;
        }

        public void setNext(SortedSingleLinkedListNode next) {
            this.next = next;
        }
    }

    private SortedSingleLinkedListNode root;

    public SortedSingleLinkedList() {
        root = new SortedSingleLinkedListNode();
    }

    public void insert(int data) {
        SortedSingleLinkedListNode currentNode = root;
        while(currentNode.getNext() != null) {
            if(currentNode.getNext().getData() < data) {
                currentNode.setNext(new SortedSingleLinkedListNode(data, currentNode.getNext()));
                return ;
            }
        }
        currentNode.setNext(new SortedSingleLinkedListNode(data));
    }

    public int remove(int data) {
        SortedSingleLinkedListNode currentNode = root;
        int index = 0;
        while(currentNode.getNext() != null) {
            index++;
            if(currentNode.getNext().getData() == data) {
                currentNode.setNext(currentNode.getNext().getNext());
                return index;
            }
            currentNode = currentNode.getNext();
        }
        return -1;
    }

    public int find(int data) {
        SortedSingleLinkedListNode currentNode = root;
        int index = 0;
        while(currentNode.getNext() != null) {
            index++;
            if(currentNode.getNext().getData() == data) {
                return index;
            }
        }
        currentNode = currentNode.getNext();
        return -1;
    }

    public int size() {
        SortedSingleLinkedListNode currentNode = root;
        int size = 0;
        while(currentNode.getNext() != null) {
            size++;
            currentNode = currentNode.getNext();
        }
        return size;
    }

    public boolean isEmpty() {
        return root.getNext() == null;
    }

    @Override
    public String toString() {
        SortedSingleLinkedListNode currentNode = root;
        StringBuilder stringBuilder = new StringBuilder("[");
        while(currentNode.getNext() != null) {
            stringBuilder.append(currentNode.getNext().getData());
            currentNode = currentNode.getNext();
            if(currentNode.getNext() != null) {
                stringBuilder.append("->");
            }
        }
        return stringBuilder.append("]").toString();
    }


    @Test
    public void test1() {
        SortedSingleLinkedList singleLinkedList = new SortedSingleLinkedList();
        System.out.println(singleLinkedList);
        singleLinkedList.insert(1);
        System.out.println(singleLinkedList);
        singleLinkedList.insert(2);
        System.out.println(singleLinkedList);
        singleLinkedList.insert(3);
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.remove(3));
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.remove(1));
        System.out.println(singleLinkedList);
        System.out.println(singleLinkedList.remove(2));
        System.out.println(singleLinkedList);
    }
}
