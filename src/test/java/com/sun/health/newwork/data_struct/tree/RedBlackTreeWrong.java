package com.sun.health.newwork.data_struct.tree;

import org.junit.Test;

public class RedBlackTreeWrong {
    private class RedBlackTreeNode {
        private int data;
        private RedBlackTreeNode leftChild;
        private RedBlackTreeNode rightChild;
        private RedBlackTreeNode parent;
        private boolean red = true;

        public RedBlackTreeNode() {
        }

        public RedBlackTreeNode(int data) {
            this.data = data;
        }

        public RedBlackTreeNode(int data, RedBlackTreeNode leftChild, RedBlackTreeNode rightChild, RedBlackTreeNode parent) {
            this.data = data;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.parent = parent;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public RedBlackTreeNode getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(RedBlackTreeNode leftChild) {
            this.leftChild = leftChild;
        }

        public RedBlackTreeNode getRightChild() {
            return rightChild;
        }

        public void setRightChild(RedBlackTreeNode rightChild) {
            this.rightChild = rightChild;
        }

        public RedBlackTreeNode getParent() {
            return parent;
        }

        public void setParent(RedBlackTreeNode parent) {
            this.parent = parent;
        }

        public boolean isRed() {
            return red;
        }

        public void setRed(boolean red) {
            this.red = red;
        }

        @Override
        public String toString() {
            return data + "(" + (isRed() ? "红色" : "黑色") + ")";
        }
    }

    private RedBlackTreeNode root;

    public RedBlackTreeWrong() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public RedBlackTreeNode find(int data) {
        return null;
    }


    public void insert(int data) {
        RedBlackTreeNode insertNode = new RedBlackTreeNode(data);
        RedBlackTreeNode  currentNode = root;
        if(isEmpty()) {
            root = insertNode;
            root.setRed(false);
            return;
        }
        while(true) {
            if(currentNode.data < data) {
                if(currentNode.getRightChild() == null) {
                    currentNode.setRightChild(insertNode);
                    insertNode.setParent(currentNode);
                    insertChange(insertNode);
                    break;
                } else {
                    currentNode = currentNode.getRightChild();
                }
            } else {
                if(currentNode.getLeftChild() == null) {
                    currentNode.setLeftChild(insertNode);
                    insertNode.setParent(currentNode);
                    insertChange(insertNode);
                    break;
                } else {
                    currentNode = currentNode.getLeftChild();
                }
            }
        }

    }

//    private void insertChange(RedBlackTreeNode r) {
//        RedBlackTreeNode parent = r.getParent();
//        if(parent == null) {
//            r.setRed(false);
//            root = r;
//            return ;
//        }
//        if(!parent.isRed()) {
//            return ;
//        }
//        RedBlackTreeNode grand = parent.getParent();
//        if(grand == null) {
//            return;
//        }
//        RedBlackTreeNode uncle = null;
//        if(isLeft(parent)) {
//            uncle = grand.getRightChild();
//            if(uncle == null) {
//                if(isLeft(r)) {
//                    parent.setRed(false);
//                    grand.setRed(true);
//                    rightRotate(grand);
//                    insertChange(parent);
//                } else {
//                    leftRotate(parent);
//                    insertChange(parent);
//                }
//            } else {
//                if(uncle.isRed()) {
//                    parent.setRed(false);
//                    uncle.setRed(false);
//                    grand.setRed(true);
//                    insertChange(grand);
//                } else {
//                    if(isLeft(r)) {
//                        parent.setRed(false);
//                        grand.setRed(true);
//                        rightRotate(grand);
//                        insertChange(parent);
//                    } else {
//                        leftRotate(parent);
//                        insertChange(parent);
//                    }
//                }
//            }
//        } else {
//            uncle = grand.getLeftChild();
//            if(uncle == null) {
//                if(isLeft(r)) {
//                    rightRotate(parent);
//                    insertChange(parent);
//                } else {
//                    parent.setRed(false);
//                    grand.setRed(true);
//                    leftRotate(grand);
//                    insertChange(parent);
//                }
//            } else {
//                if(uncle.isRed()) {
//                    uncle.setRed(false);
//                    parent.setRed(false);
//                    grand.setRed(true);
//                    insertChange(grand);
//                } else {
//                    if(isLeft(r)) {
//                        rightRotate(parent);
//                        insertChange(parent);
//                    } else {
//                        parent.setRed(false);
//                        grand.setRed(true);
//                        leftRotate(grand);
//                        insertChange(parent);
//                    }
//                }
//            }
//        }
//    }

    private void insertChange(RedBlackTreeNode r) {
        RedBlackTreeNode parent = r.getParent();
        if(parent == null) {
            r.setRed(false);
            root = r;
            return;
        }
        if(!parent.isRed()) {
            return;
        }
        RedBlackTreeNode grant = parent.getParent();
        RedBlackTreeNode uncle;
        if(isLeft(parent)) {
            uncle = grant.getRightChild();
            if(uncle != null && uncle.isRed()) {
                uncle.setRed(false);
                parent.setRed(false);
                grant.setRed(true);
                insertChange(grant);
            } else {
                if(isLeft(r)) {
                    parent.setRed(false);
                    grant.setRed(true);
                    rightRotate(grant);
                    insertChange(parent);
                } else {
                    leftRotate(parent);
                    insertChange(parent);
                }
            }
        } else {
            uncle = grant.getLeftChild();
            if(uncle != null && uncle.isRed()) {
                uncle.setRed(false);
                parent.setRed(false);
                grant.setRed(true);
                insertChange(grant);
            } else {
                if(isLeft(r)) {
                    rightRotate(parent);
                    insertChange(parent);
                } else {
                    parent.setRed(false);
                    grant.setRed(true);
                    leftRotate(grant);
                    insertChange(parent);
                }
            }
        }
    }

    public boolean remove(int data) {
        RedBlackTreeNode currentNode = this.root;
        while(currentNode != null) {
           if(currentNode.getData() == data) {
                if(currentNode.getLeftChild() == null || currentNode.getRightChild() == null) {
                    RedBlackTreeNode next = currentNode.getLeftChild() == null? currentNode.getRightChild() == null ? null : currentNode.getRightChild() : currentNode.getLeftChild();
                    if(next != null) {
                        next.setParent(currentNode.getParent());
                    }
                    if(currentNode.getParent() == null) {
                        root = next;
                    } else {
                        if(isLeft(currentNode)) {
                            currentNode.getParent().setLeftChild(next);
                        } else {
                            currentNode.getParent().setRightChild(next);
                        }
                    }
                } else {
                    RedBlackTreeNode next = findNext(currentNode);
                    currentNode.setData(next.getData());
                    if(isLeft(next)) {
                        next.getParent().setLeftChild(next.getRightChild());
                    } else {
                        next.getParent().setRightChild(next.getRightChild());
                    }
                }
                break;
           } else if(currentNode.getData() < data) {
               currentNode = currentNode.getRightChild();
           } else {
               currentNode = currentNode.getLeftChild();
           }
        }
        return false;
    }

    private void removeChange(RedBlackTreeNode changeNode) {

    }

    private RedBlackTreeNode findNext(RedBlackTreeNode node) {
        RedBlackTreeNode currentNode = node.getRightChild();
        while(currentNode.getLeftChild() != null) {
            currentNode = currentNode.getLeftChild();

        }
        return currentNode;
    }

    /**
     * 左旋
     * @param r
     */
    private void leftRotate(RedBlackTreeNode r) {
        Boolean isLeft = isLeft(r);
        RedBlackTreeNode parent = r.getParent();
        RedBlackTreeNode leftChild = r.getLeftChild();
        RedBlackTreeNode rightChild = r.getRightChild();
        if(rightChild == null) {
            return ;
        }
        RedBlackTreeNode rightChildLeftChild = rightChild.getLeftChild();
        r.setRightChild(rightChildLeftChild);
        if(rightChildLeftChild != null) {
            rightChildLeftChild.setParent(r);
        }
        r.setParent(rightChild);
        rightChild.setLeftChild(r);
        rightChild.setParent(parent);
        if(parent == null) {

        } else if(isLeft) {
            parent.setLeftChild(rightChild);
        } else {
            parent.setRightChild(rightChild);
        }
    }

    /**
     * 右旋
     * @param r
     */
    private void rightRotate(RedBlackTreeNode r) {
        Boolean isLeft = isLeft(r);
        RedBlackTreeNode parent = r.getParent();
        RedBlackTreeNode rightChild = r.getRightChild();
        RedBlackTreeNode leftChild = r.getLeftChild();
        if(leftChild == null) {
            return ;
        }
        RedBlackTreeNode leftChildRightChild = leftChild.getRightChild();
        r.setLeftChild(leftChildRightChild);
        if(leftChildRightChild != null) {
            leftChildRightChild.setParent(r);
        }
        r.setParent(leftChild);
        leftChild.setRightChild(r);
        leftChild.setParent(parent);
        if (isLeft == null) {

        } if (isLeft) {
            parent.setLeftChild(leftChild);
        } else {
            parent.setRightChild(leftChild);
        }
    }

    /**
     * 判断当前节点是否是父结点的左节点
     * @param r
     * @return
     */
    private Boolean isLeft(RedBlackTreeNode r) {
        RedBlackTreeNode parentNode = r.getParent();
        return parentNode == null ? null : r.equals(parentNode.getLeftChild()) ? true : false;
    }

    public void print() {
        printWithRecursion(root);
        System.out.println();
    }

    public void printWithRecursion(RedBlackTreeNode node) {
        if(node == null) {
            return;
        }
        printWithRecursion(node.getLeftChild());
        System.out.print(node + "->");
        printWithRecursion(node.getRightChild());
    }

    @Test
    public void test1() {
        RedBlackTreeWrong redBlackTreeWrong = new RedBlackTreeWrong();
        redBlackTreeWrong.insert(10);
        redBlackTreeWrong.insert(5);
        redBlackTreeWrong.insert(15);
        redBlackTreeWrong.insert(3);
        redBlackTreeWrong.insert(7);
        redBlackTreeWrong.insert(13);
        redBlackTreeWrong.insert(17);
        redBlackTreeWrong.insert(2);
        redBlackTreeWrong.insert(4);
        redBlackTreeWrong.insert(6);
        redBlackTreeWrong.insert(8);
        redBlackTreeWrong.insert(12);
        redBlackTreeWrong.insert(14);
        redBlackTreeWrong.insert(16);
        redBlackTreeWrong.insert(18);

//        redBlackTreeWrong.insert(6);
//        redBlackTreeWrong.insert(8);
//        redBlackTreeWrong.insert(12);
//        redBlackTreeWrong.insert(14);
//        redBlackTreeWrong.insert(16);
//        redBlackTreeWrong.insert(18);
//        redBlackTreeWrong.insert(10);
//        redBlackTreeWrong.insert(5);
//        redBlackTreeWrong.insert(15);
//        redBlackTreeWrong.insert(3);
//        redBlackTreeWrong.insert(7);
//        redBlackTreeWrong.insert(13);
//        redBlackTreeWrong.insert(17);
//        redBlackTreeWrong.insert(2);
//        redBlackTreeWrong.insert(4);


        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(5);
        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(2);
        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(10);
        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(13);
        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(18);
        redBlackTreeWrong.print();
        redBlackTreeWrong.remove(7);
        redBlackTreeWrong.print();
    }

}
