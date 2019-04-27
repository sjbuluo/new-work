package com.sun.health.newwork.data_struct.linked;

import org.junit.Test;

public class TwoWayLinkedList<T> {
    private class TwoWayLinkedListNode<T> {
        private T data;
        private TwoWayLinkedListNode<T> next;
        private TwoWayLinkedListNode<T> prev;

        public TwoWayLinkedListNode() {
        }

        public TwoWayLinkedListNode(T data) {
            this.data = data;
        }

        public TwoWayLinkedListNode(T data, TwoWayLinkedListNode<T> next, TwoWayLinkedListNode<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public TwoWayLinkedListNode<T> getNext() {
            return next;
        }

        public void setNext(TwoWayLinkedListNode<T> next) {
            this.next = next;
        }

        public TwoWayLinkedListNode<T> getPrev() {
            return prev;
        }

        public void setPrev(TwoWayLinkedListNode<T> prev) {
            this.prev = prev;
        }
    }

    private TwoWayLinkedListNode<T> head;

    private TwoWayLinkedListNode<T> tail;

    public TwoWayLinkedList() {
        head = new TwoWayLinkedListNode<>();
        tail = head;
    }

    public void insert(T t) {
        tail.setNext(new TwoWayLinkedListNode<>(t, null, tail));
        tail = tail.getNext();
    }

    public void insertHead(T t) {
        head.setNext(new TwoWayLinkedListNode<>(t, head.getNext(), head));
        if(isEmpty()) {
            tail = head.getNext();
        }
    }

    public void insertTail(T t) {
        tail.setNext(new TwoWayLinkedListNode<>(t, null, tail));
        tail = tail.getNext();
    }

    public int remove(T t) {
        int index = 0;
        TwoWayLinkedListNode<T> currentNode = head;
        while(currentNode.getNext() != null) {
            index++;
            if (currentNode.getNext().getData().equals(t)) {
                currentNode.setNext(currentNode.getNext().getNext());
                if(currentNode.getNext() == null) {
                    tail = currentNode;
                } else {
                    currentNode.getNext().setPrev(currentNode);
                }
                return index;
            }
            currentNode = currentNode.getNext();
        }
        return -1;
    }

    public int find(T t) {
        int index = 0;
        TwoWayLinkedListNode<T> currentNode = head;
        while(currentNode.getNext() != null) {
            index++;
            if (currentNode.getNext().getData().equals(t)) {
                return index;
            }
            currentNode = currentNode.getNext();
        }
        return -1;
    }

    public int size() {
        int size = 0;
        TwoWayLinkedListNode<T> currentNode = head;
        while(currentNode.getNext() != null) {
            size++;
            currentNode = currentNode.getNext();
        }
        return size;
    }

    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        TwoWayLinkedListNode<T> currentNode = head;
        while(currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
            stringBuilder.append(currentNode.getData());
            if(currentNode.getNext() != null) {
                stringBuilder.append("<->");
            }
        }
        return stringBuilder.append("]").toString();
    }

    @Test
    public void test1() {
        TwoWayLinkedList<Integer> twoWayLinkedList = new TwoWayLinkedList<>();
        System.out.println(twoWayLinkedList);
        twoWayLinkedList.insert(1);
        System.out.println(twoWayLinkedList);
        twoWayLinkedList.insert(2);
        System.out.println(twoWayLinkedList);
        twoWayLinkedList.insert(3);
        System.out.println(twoWayLinkedList);
        twoWayLinkedList.insertTail(5);
        System.out.println(twoWayLinkedList);
        twoWayLinkedList.insertTail(6);
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.remove(3));
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.remove(1));
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.remove(2));
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.remove(5));
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.remove(6));
        System.out.println(twoWayLinkedList);
        System.out.println(twoWayLinkedList.isEmpty());
    }

}
