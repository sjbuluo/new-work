package com.sun.health.newwork.data_struct.tree;

import org.junit.Test;
import org.springframework.security.core.parameters.P;

public class RedBlackTree {

    private class RedBlackTreeNode {
        private int data;
        private boolean red;
        private boolean nil;
        private RedBlackTreeNode parent;
        private RedBlackTreeNode leftChild;
        private RedBlackTreeNode rightChild;

        /**
         * 没有数据 表示此为NIL节点 即叶节点 颜色为黑色
         */
        public RedBlackTreeNode() {
            this.nil = true;
            this.red = false;
            this.leftChild = null;
            this.rightChild = null;
        }

        public RedBlackTreeNode(RedBlackTreeNode parent) {
            this.nil = true;
            this.red = false;
            this.parent = parent;
            this.leftChild = null;
            this.rightChild = null;
        }

        /**
         * 有数据 表示这不是个NIL节点 即为有效节点 颜色默认为红色 且有两个NIL叶节点
         * @param data
         */
        public RedBlackTreeNode(int data) {
            this.data = data;
            this.nil = false;
            this.red = true;
            this.leftChild = new RedBlackTreeNode(this);
            this.rightChild = new RedBlackTreeNode(this);
        }

        public RedBlackTreeNode(int data, RedBlackTreeNode parent) {
            this.data = data;
            this.parent = parent;
            this.nil = false;
            this.red = true;
            this.leftChild = new RedBlackTreeNode(this);
            this.rightChild = new RedBlackTreeNode(this);
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public boolean isRed() {
            return red;
        }

        public void setRed(boolean red) {
            this.red = red;
        }

        public boolean isNil() {
            return nil;
        }

        public void setNil(boolean nil) {
            this.nil = nil;
        }

        public RedBlackTreeNode getParent() {
            return parent;
        }

        public void setParent(RedBlackTreeNode parent) {
            this.parent = parent;
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

        @Override
        public String toString() {
            return data + "(" + (isRed() ? '红' : "黑") + ")";
        }
    }

    private RedBlackTreeNode root;

    public RedBlackTree() {
        root = new RedBlackTreeNode();
    }

    /**
     * 树是否为空
     * @return
     */
    public boolean isEmpty() {
        return this.root.isNil();
    }

    /**
     * 查询
     * @param data
     * @return
     */
    public boolean find(int data) {
        return false;
    }

    /**
     * 插入新数据
     * @param data
     */
    public void insert(int data) {
        if(root.isNil()) {
            root = new RedBlackTreeNode(data);
            insertChange(root);
            return ;
        }
        RedBlackTreeNode currentNode = this.root;
        while(!currentNode.isNil()) {
            if(currentNode.getData() < data) {
                if(currentNode.getRightChild().isNil()) {
                    RedBlackTreeNode insertNode = new RedBlackTreeNode(data, currentNode);
                    currentNode.setRightChild(insertNode);
                    insertChange(insertNode);
                    break;
                } else {
                    currentNode = currentNode.getRightChild();
                }
            } else if(currentNode.getData() > data) {
                if(currentNode.getLeftChild().isNil()) {
                    RedBlackTreeNode insertNode = new RedBlackTreeNode(data, currentNode);
                    currentNode.setLeftChild(insertNode);
                    insertChange(insertNode);
                    break;
                } else {
                    currentNode = currentNode.getLeftChild();
                }
             } else {
                return ;
            }
        }
    }

    /**
     * 由于红黑树的颜色存在一定的规则 所以在插入之后需要进行一定的重设颜色和旋转以符合规则
     * @param node
     */
    private void insertChange(RedBlackTreeNode node) {
        RedBlackTreeNode parent = node.getParent();
        // 如果当前节点为根节点 则将颜色置为黑色即可 并设置当前结点为root
        if(parent == null) {
            root = node;
            node.setRed(false);
            return ;
        }
        // 如果当前节点的父结点为黑色 则无需额外操作即可
        if(!parent.isRed()) {
            return ;
        }
        // 如果当前节点父结点为红色 需要进行条件判断
        RedBlackTreeNode grand = parent.getParent();
        RedBlackTreeNode uncle = null;
        // 因为左右的旋转方式不同 所以需要判断
        if(isLeft(parent)) {
            uncle = grand.getRightChild();
            // 如果父结点和叔叔结点都为红 则需要设置父结点和叔叔结点为黑 祖父结点为红 再从祖父结点开始判断
            if(!uncle.isNil() && uncle.isRed()) {
                grand.setRed(true);
                parent.setRed(false);
                uncle.setRed(false);
                insertChange(grand);
            } else { // 如果叔叔结点为NIL叶子结点 或者为黑色时
                // 如果父结点是祖父节点的左子结点 当前节点为父结点的左子结点（同边）则需要以祖父节点为支点进行右旋操作 再以父结点重新判断（有可能变为root）
                if(isLeft(node)) {
                    grand.setRed(true);
                    parent.setRed(false);
                    rightRotate(grand);
                    insertChange(parent);
                } else { // 如果父结点是祖父结点的左子结点 当前节点是父结点的右子结点（不同边） 则需要先以父结点为支点进行左旋 再以父结点重新开始判断(转化为同边条件)
                    leftRotate(parent);
                    insertChange(parent);
                }
            }
        } else { // 与以上旋转方向相反 其他一致
            uncle = grand.getLeftChild();
            if(!uncle.isNil() && uncle.isRed()) {
                grand.setRed(true);
                uncle.setRed(false);
                parent.setRed(false);
                insertChange(grand);
            } else {
                if(isLeft(node)) {
                    rightRotate(parent);
                    insertChange(parent);
                } else {
                    grand.setRed(true);
                    parent.setRed(false);
                    leftRotate(grand);
                    insertChange(parent);
                }
            }
        }

    }

    /**
     * 删除对应数据的节点
     * @param data
     * @return true为找到对应节点并删除成功 false为未找到对应节点
     */
    public boolean remove(int data) {
        if(root.isNil()) {
            return false;
        }
        RedBlackTreeNode currentNode = this.root;
        while(!currentNode.isNil()) {
            if(currentNode.getData() == data) {
                RedBlackTreeNode parent = currentNode.getParent();
                RedBlackTreeNode leftChild = currentNode.getLeftChild();
                RedBlackTreeNode rightChild = currentNode.getRightChild();
                RedBlackTreeNode next = null;
                if(leftChild.isNil() || rightChild.isNil()) {
                    next = leftChild.isNil() ? rightChild : leftChild;
                    next.setParent(parent);
                    if(parent == null) {
                        root = next;
                        root.setRed(false);
                    } else {
                        if(isLeft(currentNode)) {
                            parent.setLeftChild(next);
                        } else {
                            parent.setRightChild(next);
                        }
                        removeChange(next, currentNode);
                    }
                } else {
                    next = findNext(currentNode);
                    currentNode.setData(next.getData());
                    RedBlackTreeNode nextParent = next.getParent();
                    RedBlackTreeNode nextChild = findChildNode(next);
                    nextChild.setParent(nextParent);
                    if(isLeft(next)) {
                        nextParent.setLeftChild(nextChild);
                    } else {
                        nextParent.setRightChild(nextChild);
                    }
                    removeChange(nextChild, next);
                }
                return true;
            } else if(currentNode.getData() < data) {
                currentNode = currentNode.getRightChild();
            } else {
                currentNode = currentNode.getLeftChild();
            }
        }
        return false;
    }

    private void removeChange(RedBlackTreeNode node, RedBlackTreeNode removeNode) {
        // 删除红色节点无需操作
        if(removeNode.isRed()) {
            return ;
        } else { // 删除的节点为黑色
            // 替换的节点为红色 则只需将颜色设置为黑色即可
            if(node.isRed()) {
                node.setRed(false);
                return ;
            }
            RedBlackTreeNode parent = node.getParent();
            RedBlackTreeNode bro = null;
            RedBlackTreeNode broLeftChild = null;
            RedBlackTreeNode broRightChild = null;
            if(isLeft(node)) {
                bro = parent.getRightChild();
                broLeftChild = bro.getLeftChild();
                broRightChild = bro.getRightChild();
                // 情况一 如果父结点为红色 兄弟节点和兄弟结点的左右子结点都为黑色时 只要将父结点置为黑色 兄弟结点置为红色
                if(parent.isRed() && !bro.isRed() && !broLeftChild.isRed() && !broRightChild.isRed()) {
                    bro.setRed(true);
                    parent.setRed(false);
                }
                // 情况二 如果兄弟结点为黑色 且兄弟结点不同边（比如当前结点是父结点的左子结点 则不同边的是兄弟结点的右子结点）子结点为红色
                // 则以当前节点对父结点的方向旋（比如当前节点是父结点的右子结点则右旋） 然后替换父结点和兄弟结点的颜色 再设置兄弟结点的不同边结点颜色为黑色
                else if(!bro.isRed() && broRightChild.isRed()) {
                    leftRotate(parent);
                    swapNodeColor(parent, bro);
                    broRightChild.setRed(false);
                }
                // 如果兄弟结点为黑色 但是兄弟结点同边（即替换结点是父结点的左子结点，则对应的是兄弟结点的左子结点）为红色 则先以兄弟结点为支点 以替换结点相对父结点方向的反向旋转（替换结点是父结点的左子结点则右旋）再交换兄弟结点和同边结点的颜色 （制造出情况3） 然后再重新进行判断
                else if(!bro.isRed() && broLeftChild.isRed() && !broRightChild.isRed()) {
                    rightRotate(bro);
                    swapNodeColor(bro, broLeftChild);
                    removeChange(node, removeNode);
                }

                // 如果兄弟结点为红色 其他为黑色 则以父结点为支点 以替换结点相对父结点的方向旋转 在交换父结点和兄弟结点的颜色 再重新判断(制造出情况2)
                else if(bro.isRed()) {
                    leftRotate(parent);
                    swapNodeColor(parent, bro);
                    removeChange(node, removeNode);
                }
                // 如果全都是黑色 则设置兄弟结点为红色 再以父结点为替换结点 重新判断（此时父结点所在的所有路线都比其他路线少了一个黑色结点）
                else if(!parent.isRed() && !bro.isRed() && !broLeftChild.isRed() && !broRightChild.isRed()) {
                    bro.setRed(true);
                    removeChange(parent, removeNode);
                }

            }
            // 替换结点是父结点的右节点
            else {
                bro = parent.getLeftChild();
                broLeftChild = bro.getLeftChild();
                broRightChild = bro.getRightChild();
                if(parent.isRed() && !bro.isRed() && !broLeftChild.isRed() && !broRightChild.isRed()) {
                    parent.setRed(false);
                    bro.setRed(true);
                }
                else if(!bro.isRed() && broLeftChild.isRed()) {
                    rightRotate(parent);
                    swapNodeColor(parent, bro);
                    broLeftChild.setRed(false);
                }
                else if(!bro.isRed() && broRightChild.isRed()) {
                    leftRotate(bro);
                    swapNodeColor(bro, broRightChild);
                    removeChange(node, removeNode);
                }
                else if(bro.isRed()) {
                    rightRotate(parent);
                    swapNodeColor(parent, bro);
                    removeChange(node, removeNode);
                }
                else if(!parent.isRed() && !bro.isRed() && !broLeftChild.isRed() && !broRightChild.isRed()) {
                    bro.setRed(true);
                    removeChange(parent, removeNode);
                }
            }

        }
    }

    private void swapNodeColor(RedBlackTreeNode nodeA, RedBlackTreeNode nodeB) {
        boolean red = nodeA.isRed();
        nodeA.setRed(nodeB.isRed());
        nodeB.setRed(red);
    }

    private RedBlackTreeNode findChildNode(RedBlackTreeNode node) {
        return node.getLeftChild().isNil() ? node.getRightChild() : node.getLeftChild();
    }

    private RedBlackTreeNode findNext(RedBlackTreeNode node) {
        RedBlackTreeNode currentNode = node.getRightChild();
        while(!currentNode.getLeftChild().isNil()) {
            currentNode = currentNode.getLeftChild();
        }
        return currentNode;
    }

    /**
     * 以node为支点左旋
     * @param node
     */
    public void leftRotate(RedBlackTreeNode node) {
        if(node == null || node.isNil()) {
            return;
        }
        RedBlackTreeNode leftChild = node.getLeftChild();
        RedBlackTreeNode rightChild = node.getRightChild();
        RedBlackTreeNode rightChildLeftChild = rightChild.getLeftChild();
        RedBlackTreeNode parent = node.getParent();
        // 原右子结点设置和父结点的设置
        rightChild.setParent(parent);
        if(parent != null) {
            if(isLeft(node)) {
                parent.setLeftChild(rightChild);
            } else {
                parent.setRightChild(rightChild);
            }
        }
        // 原结点和原右子结点的设置
        rightChild.setLeftChild(node);
        node.setParent(rightChild);
        // 原结点和原右子结点的左子结点设置
        node.setRightChild(rightChildLeftChild);
        rightChildLeftChild.setParent(node);
    }

    /**
     * 以node为支点右旋
     * @param node
     */
    public void rightRotate(RedBlackTreeNode node) {
        if(node == null || node.isNil()) {
            return;
        }
        RedBlackTreeNode parent = node.getParent();
        RedBlackTreeNode rightChild = node.getRightChild();
        RedBlackTreeNode leftChild = node.getLeftChild();
        RedBlackTreeNode leftChildRightChild = leftChild.getRightChild();
        // 设置原左子结点和父结点的关系
        leftChild.setParent(parent);
        if(parent != null) {
            if(isLeft(node)) {
                parent.setLeftChild(leftChild);
            } else {
                parent.setRightChild(leftChild);
            }
        }
        // 设置原左子结点和原结点的关系
        leftChild.setRightChild(node);
        node.setParent(leftChild);
        // 设置原结点和原左子结点的右子结点的关系
        node.setLeftChild(leftChildRightChild);
        leftChildRightChild.setParent(node);
    }

    public Boolean isLeft(RedBlackTreeNode node) {
        RedBlackTreeNode parent = node.getParent();
        return  parent == null ? null : node.equals(parent.getLeftChild()) ? true : false;
    }

    /**
     * 输出树的中缀遍历 红黑树为从小到大
     */
    public void print() {
        printWithRecursion(root);
        System.out.println();
    }

    private void printWithRecursion(RedBlackTreeNode node) {
        if(node.isNil()) {
            return ;
        }
        printWithRecursion(node.getLeftChild());
        System.out.print(node);
        printWithRecursion(node.getRightChild());
    }


    @Test
    public void test1() {
        RedBlackTree redBlackTree = new RedBlackTree();
//        redBlackTree.insert(10);
//        redBlackTree.insert(5);
//        redBlackTree.insert(15);
//        redBlackTree.insert(3);
//        redBlackTree.insert(7);
//        redBlackTree.insert(13);
//        redBlackTree.insert(17);
//        redBlackTree.insert(2);
//        redBlackTree.insert(4);
//        redBlackTree.insert(6);
//        redBlackTree.insert(8);
//        redBlackTree.insert(12);
//        redBlackTree.insert(14);
//        redBlackTree.insert(16);
//        redBlackTree.insert(18);

        redBlackTree.insert(6);
        redBlackTree.insert(8);
        redBlackTree.insert(12);
        redBlackTree.insert(14);
        redBlackTree.insert(16);
        redBlackTree.insert(18);
        redBlackTree.insert(10);
        redBlackTree.insert(5);
        redBlackTree.insert(15);
        redBlackTree.insert(3);
        redBlackTree.insert(7);
        redBlackTree.insert(13);
        redBlackTree.insert(17);
        redBlackTree.insert(2);
        redBlackTree.insert(4);


        redBlackTree.print();
        redBlackTree.remove(5);
        redBlackTree.print();
        redBlackTree.remove(2);
        redBlackTree.print();
        redBlackTree.remove(10);
        redBlackTree.print();
        redBlackTree.remove(13);
        redBlackTree.print();
        redBlackTree.remove(18);
        redBlackTree.print();
        redBlackTree.remove(7);
        redBlackTree.print();
        redBlackTree.remove(17);
        redBlackTree.print();
        redBlackTree.remove(8);
        redBlackTree.print();
    }
}
