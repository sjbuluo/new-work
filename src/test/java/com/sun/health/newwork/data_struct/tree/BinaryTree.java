package com.sun.health.newwork.data_struct.tree;

import jdk.nashorn.internal.ir.BinaryNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BinaryTree<T extends Comparable<T>> {

    private class BinaryTreeNode<T> {
        private T data;
        private BinaryTreeNode<T> leftNode;
        private BinaryTreeNode<T> rightNode;

        public BinaryTreeNode() {
        }

        public BinaryTreeNode(T data) {
            this.data = data;
        }

        public BinaryTreeNode(T data, BinaryTreeNode<T> leftNode, BinaryTreeNode<T> rightNode) {
            this.data = data;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public BinaryTreeNode<T> getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(BinaryTreeNode<T> leftNode) {
            this.leftNode = leftNode;
        }

        public BinaryTreeNode<T> getRightNode() {
            return rightNode;
        }

        public void setRightNode(BinaryTreeNode<T> rightNode) {
            this.rightNode = rightNode;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }

    private BinaryTreeNode<T> root = null;

    public void insert(T data) {
        if(root == null) {
            root = new BinaryTreeNode<>(data);
        } else {
            BinaryTreeNode<T> currentNode = this.root;
            while(true) {
                if(currentNode.getData().compareTo(data) == 0) {
                    return ;
                } else if(currentNode.getData().compareTo(data) < 0) {
                    if(currentNode.getRightNode() == null) {
                        currentNode.setRightNode(new BinaryTreeNode<>(data));
                        break;
                    } else {
                        currentNode = currentNode.getRightNode();
                    }
                } else {
                    if(currentNode.getLeftNode() == null) {
                        currentNode.setLeftNode(new BinaryTreeNode<>(data));
                        break;
                    } else {
                        currentNode = currentNode.getLeftNode();
                    }
                }
            }
        }
    }

    public BinaryTreeNode<T> find(T data) {
        BinaryTreeNode<T> currentNode = this.root;
        while(currentNode != null) {
            if(currentNode.getData().compareTo(data) == 0) {
                break;
            } else if(currentNode.getData().compareTo(data) < 0) {
                currentNode = currentNode.getRightNode();
            } else {
                currentNode = currentNode.getLeftNode();
            }
        }
        return currentNode;
    }

    public BinaryTreeNode<T> remove(T data) {
        BinaryTreeNode<T> currentNode = root;
        BinaryTreeNode<T> parentNode = null;
        boolean left = false;
        while(currentNode != null) {
            if(currentNode.getData().compareTo(data) == 0) {
//                if(currentNode.getRightNode() == null && currentNode.getLeftNode() == null) {
//                    if(parentNode == null) {
//                        root = null;
//                    } else {
//                        if(left) {
//                            parentNode.setLeftNode(null);
//                        } else {
//                            parentNode.setRightNode(null);
//                        }
//                    }
//                } else if(currentNode.getLeftNode() != null && currentNode.getRightNode() == null) {
//                    if(parentNode == null) {
//                        root = currentNode.getLeftNode();
//                    } else {
//                        if(left) {
//                            parentNode.setLeftNode(parentNode.getLeftNode());
//                        } else {
//                            parentNode.setRightNode(parentNode.getLeftNode());
//                        }
//                    }
//                } else if(currentNode.getLeftNode() == null && currentNode.getRightNode() != null) {
//                    if(parentNode == null) {
//                        root = currentNode.getRightNode();
//                    } else {
//                        if(left) {
//                            parentNode.setLeftNode(currentNode.getRightNode());
//                        } else {
//                            parentNode.setRightNode(currentNode.getRightNode());
//                        }
//                    }
                if(currentNode.getLeftNode() == null || currentNode.getRightNode() == null) {
                    BinaryTreeNode<T> next = currentNode.getLeftNode() != null ? currentNode.getLeftNode() : currentNode.getRightNode() != null ? currentNode.getRightNode() : null;
                    if(parentNode == null) {
                        root = next;
                    } else {
                        if(left) {
                            parentNode.setLeftNode(next);
                        } else {
                            parentNode.setRightNode(next);
                        }
                    }
                } else {
                    BinaryTreeNode<T> rightNode = currentNode.getRightNode();
                    if(rightNode.getLeftNode() == null) {
                        rightNode.setLeftNode(currentNode.getLeftNode());
                        if(parentNode == null) {
                            root = rightNode;
                        } else {
                            if(left) {
                                parentNode.setLeftNode(rightNode);
                            } else {
                                parentNode.setRightNode(rightNode);
                            }
                        }
                    } else {
                        BinaryTreeNode<T> leftNode = rightNode.getLeftNode();
                        BinaryTreeNode<T> leftParentNode = rightNode;
                        while(leftNode.getLeftNode() != null) {
                            leftParentNode = leftNode;
                            leftNode = leftNode.getLeftNode();
                        }
                        leftParentNode.setLeftNode(leftNode.getRightNode());
                        leftNode.setLeftNode(currentNode.getLeftNode());
                        leftNode.setRightNode(currentNode.getRightNode());
                        if(parentNode == null) {
                            root = leftNode;
                        } else {
                            if(left) {
                                parentNode.setLeftNode(leftNode);
                            } else {
                                parentNode.setRightNode(leftNode);
                            }
                        }
                    }
                }
                break;
            } else if(currentNode.getData().compareTo(data) < 0) {
                parentNode = currentNode;
                currentNode = currentNode.getRightNode();
                left = false;
            } else {
                parentNode = currentNode;
                currentNode = currentNode.getLeftNode();
                left = true;
            }
        }
        return currentNode;
    }

    public BinaryTreeNode<T> findNext(BinaryTreeNode<T> root) {
        if(root.getLeftNode() == null && root.getRightNode() == null) {
            return null;
        } else if(root.getRightNode() != null && root.getLeftNode() == null) {
            return root.getRightNode();
        } else if(root.getLeftNode() != null && root.getRightNode() == null) {
            return root.getLeftNode();
        } else {
            return middleWithStack(root);
        }
    }

    public BinaryTreeNode middleWithStack(BinaryTreeNode root) {
        List<BinaryTreeNode<T>> resultList = new ArrayList<>();
        Stack<BinaryTreeNode<T>> stack = new Stack<>();
        BinaryTreeNode<T> currentNode = root;
        while (currentNode != null) {
            if(currentNode.getLeftNode() == null) {
                resultList.add(currentNode);
                if(stack.isEmpty()) {
                    currentNode = currentNode.getRightNode();
                } else {
                    currentNode = stack.pop();
                    resultList.add(currentNode);
                    currentNode = currentNode.getRightNode();
                }
            } else {
                stack.push(currentNode);
                currentNode = currentNode.getLeftNode();
            }
        }
        currentNode = null;
        for (int i = 0; i < resultList.size(); i++) {
            currentNode = resultList.get(i);
            if(currentNode.equals(root)) {
                return resultList.get(i + 1);
            }
        }
        return null;
    }

    public void middleWithStack(BinaryTreeNode root, List<BinaryTreeNode> resultList) {
        Stack<BinaryTreeNode<T>> stack = new Stack<>();
        BinaryTreeNode<T> currentNode = root;
        while (currentNode != null) {
            if(currentNode.getLeftNode() == null) {
                resultList.add(currentNode);
                if(stack.isEmpty()) {
                    currentNode = currentNode.getRightNode();
                } else {
                    currentNode = stack.pop();
                    resultList.add(currentNode);
                    currentNode = currentNode.getRightNode();
                }
            } else {
                stack.push(currentNode);
                currentNode = currentNode.getLeftNode();
            }
        }
    }

    public  void middleWithRecursion(BinaryTreeNode root, List<BinaryTreeNode> resultList) {
        if(root == null) {
            return;
        }
        middleWithRecursion(root.getLeftNode(), resultList);
        resultList.add(root);
        middleWithRecursion(root.getRightNode(), resultList);
    }

    public void print() {
        printMiddleWithRecursion(this.root);
        System.out.println();
    }

    public void printMiddleWithRecursion(BinaryTreeNode<T> root) {
        if(root == null) {
            return ;
        }
        printMiddleWithRecursion(root.getLeftNode());
        System.out.print(root + " ");
        printMiddleWithRecursion(root.getRightNode());
    }

    public BinaryTreeNode getRoot() {
        return root;
    }

    @Test
    public void test1() {
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);
        tree.insert(13);
        tree.insert(17);
        tree.insert(2);
        tree.insert(4);
        tree.insert(6);
        tree.insert(8);
        tree.insert(12);
        tree.insert(14);
        tree.insert(16);
        tree.insert(18);
        List<BinaryTreeNode> resultListWithStack = new ArrayList<>();
        List<BinaryTreeNode> resultListWithRecursion = new ArrayList<>();
        middleWithRecursion(tree.getRoot(), resultListWithRecursion);
        middleWithStack(tree.getRoot(), resultListWithStack);
        System.out.println(resultListWithRecursion);
        System.out.println(resultListWithStack);

        System.out.println(tree.find(5));
        tree.print();
        System.out.println(tree.remove(5));
        tree.print();
        System.out.println(tree.find(5));
        tree.print();
        System.out.println(tree.find(10));
        tree.print();
        System.out.println(tree.remove(10));
        tree.print();
        System.out.println(tree.find(10));
        tree.print();
    }
}
