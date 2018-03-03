package code;

import java.util.Stack;

public class RedBlackTree {


    private Node root = null;
    private int size = 0;

    RedBlackTree() {
    }

    public void insert(int data) {
        if (root == null) {
            root = new Node(data, null);
            root.color = Node.BLACK;
            size++;
            return;
        }
        Node current = root;
        while (current != null) {
            if (data > current.data) {
                if (current.right == null) {
                    current.right = new Node(data, current);
                    current = current.right;
                    break;
                } else {
                    current = current.right;
                }
            } else if (data < current.data) {
                if (current.left == null) {
                    current.left = new Node(data, current);
                    current = current.left;
                    break;
                } else {
                    current = current.left;
                }
            } else {
                return;
            }
        }
        size++;
        fixUpAfterInsert(current);
    }

    public void remove(int data) {
        Node cur = null;
        if ((cur = search(data)) != null) {
            deleteNode(cur);
            size--;
        }
    }

    private void deleteNode(Node cur) {

        if (cur.left != null && cur.right != null) {
            Node replace = cur.right;
            while (replace.left != null) {
                replace = replace.left;
            }
            //copy data
            cur.data = replace.data;
            cur = replace;
        }
        // delete one child or no child
        Node replace = (cur.left != null ? cur.left : cur.right);
        if (replace != null) {
            replace.father = cur.father;
            if (replace.father != null) {
                if (replace.father.left == cur) {
                    replace.father.left = replace;
                } else if (replace.father.right == cur) {
                    replace.father.right = replace;
                } else {
                    root = replace;
                }
            }

            if (cur.color == Node.BLACK) {
                fixUpAfterDelete(replace);
            }
        } else if (cur.father == null) {
            root = replace;
        } else {
            if (cur.color == Node.BLACK) {
                fixUpAfterDelete(cur);
            }
            if (cur.father != null) {
                if (cur.father.left == cur) {
                    cur.father.left = null;
                } else if (cur.father.right == cur) {
                    cur.father.right = null;
                }
                // help gc
                cur.father = null;
            }
        }


    }

    private void fixUpAfterDelete(Node cur) {
        while (cur != root && colorOf(cur) == Node.BLACK) {
            if (cur == cur.father.left) {
                Node bro = cur.father.right;
                // case 1: bro is red
                if (colorOf(bro) == Node.RED) {
                    bro.color = Node.BLACK;
                    bro.father.color = Node.RED;
                    rotateLeft(cur.father);
                    bro = cur.father.right;
                }
                // case 2: bro is black and has two black sons
                if (colorOf(bro.left) == Node.BLACK && colorOf(bro.right) == Node.BLACK) {
                    bro.color = Node.RED;
                    // cur's sons is balanced
                    cur = cur.father;
                } else {
                    if (colorOf(bro.right) == Node.BLACK) {
                        //case 3: bro is black and bro's right color is black
                        bro.color = Node.RED;
                        bro.left.color = Node.BLACK;

                        rotateRight(bro);
                        bro = cur.father.right;
                    }
                    // case 4: bro is black and bro's right is red
                    bro.color = cur.father.color;
                    cur.father.color = Node.BLACK;
                    bro.right.color = Node.BLACK;
                    rotateLeft(cur.father);
                    cur = root;
                }
            } else {
                Node bro = cur.father.left;
                // case 1:
                if (colorOf(bro) == Node.RED) {
                    bro.color = Node.BLACK;
                    bro.father.color = Node.RED;
                    rotateRight(cur.father);
                    bro = cur.father.left;
                }

                // case 2
                if (colorOf(bro.left) == Node.BLACK && colorOf(bro.right) == Node.BLACK) {
                    bro.color = Node.RED;
                    cur = cur.father;
                } else {
                    if (colorOf(bro.left) == Node.BLACK) {
                        bro.right.color = Node.BLACK;
                        bro.color = Node.RED;
                        rotateLeft(bro);
                        bro = cur.father.left;
                    }
                    bro.color = cur.father.color;
                    cur.father.color = Node.BLACK;
                    bro.left.color = Node.BLACK;
                    rotateRight(cur.father);
                    cur = root;


                }
            }
        }
        cur.color = Node.BLACK;
    }
    private void fixUpAfterDelete1(Node cur){
        while(cur != root && colorOf(cur) == Node.BLACK){
            if(cur == cur.father.left){
                Node bro = cur.father.right;
                if(colorOf(bro) == Node.RED){
                    bro.color = Node.BLACK;
                    bro.father.color = Node.RED;
                    rotateLeft(bro.father);
                    bro = cur.father.right;
                }

                if(colorOf(bro.left) == Node.BLACK && colorOf(bro.right) == Node.BLACK){
                    bro.color = Node.RED;
                    cur = cur.father;
                }else{
                    if(colorOf(bro.right) == Node.BLACK){
                        bro.left.color = Node.BLACK;
                        bro.color = Node.RED;
                        rotateRight(bro);
                        bro = cur.father.right;
                    }

                    bro.color = cur.father.color;
                    cur.father.color = Node.BLACK;
                    bro.right.color = Node.BLACK;
                    rotateLeft(bro.father);
                    cur = root;
                }

            }else{
                Node bro  = cur.father.left;
                if(colorOf(bro) == Node.RED){
                    bro.color = Node.BLACK;
                    cur.father.color = Node.RED;
                    rotateRight(cur.father);
                    bro = cur.father.left;
                }

                if(colorOf(bro.left) == Node.BLACK && colorOf(bro.right) == Node.BLACK){
                    bro.color = Node.RED;
                    cur = cur.father;
                }else{
                    if(colorOf(bro.left) == Node.BLACK){
                        bro.right.color = Node.BLACK;
                        bro.color = Node.RED;
                        rotateLeft(bro);
                        bro = cur.father.left;
                    }

                    bro.color = cur.father.color;
                    bro.left.color = Node.BLACK;
                    bro.father.color = Node.BLACK;
                    rotateRight(bro.father);
                    cur = root;
                }
            }
        }
        cur.color = Node.BLACK;
    }

    private void fixUpAfterInsert(Node current) {
        current.color = Node.RED;
        while (current != null && current != root && colorOf(current.father) == Node.RED) {
            if (current.father == current.father.father.left) {
                Node uncle = current.father.father.right;
                if (colorOf(uncle) == Node.RED) {
                    current.father.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    current.father.father.color = Node.RED;
                    current = current.father.father;
                } else {
                    if (current == current.father.right) {
                        current = current.father;
                        rotateLeft(current);
                    }
                    current.father.color = Node.BLACK;
                    current.father.father.color = Node.RED;
                    rotateRight(current.father.father);
                }
            } else {
                if (current.father == current.father.father.right) {
                    Node uncle = current.father.father.left;
                    if (colorOf(uncle) == Node.RED) {
                        current.father.color = Node.BLACK;
                        current.father.father.color = Node.RED;
                        uncle.color = Node.BLACK;
                        current = current.father.father;
                    } else {
                        if (current == current.father.left) {
                            current = current.father;
                            rotateRight(current);
                        }
                        current.father.color = Node.BLACK;
                        current.father.father.color = Node.RED;
                        rotateLeft(current.father.father);
                    }
                }
            }
        }
        root.color = Node.BLACK;
    }


    private void fixUpAfterInsert1(Node cur) {
        cur.color = Node.RED;
        while (cur != null && cur != root && colorOf(cur.father) == Node.RED) {
            if (cur.father == cur.father.father.left) {
                Node uncle = cur.father.father.right;
                if (colorOf(uncle) == Node.RED) {
                    cur.father.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    cur.father.father.color = Node.RED;
                    cur = cur.father.father;
                }else {
                    if(cur == cur.father.right){
                        cur = cur.father;
                        rotateLeft(cur);
                    }
                    cur.father.color = Node.BLACK;
                    cur.father.father.color = Node.RED;
                    rotateRight(cur.father.father);
                }
            }else{
                Node uncle = cur.father.father.left;
                if(colorOf(uncle) == Node.RED){
                    cur.father.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    uncle.father.color = Node.RED;
                    cur = uncle.father;
                }else{
                    if(cur == cur.father.left){
                        cur = cur.father;
                        rotateRight(cur);
                    }
                    cur.father.color = Node.BLACK;
                    cur.father.father.color = Node.RED;
                    rotateLeft(cur.father.father);
                }
            }
        }
        root.color = Node.BLACK;
    }



    private void rotateLeft(Node x) {
        if (x != null) {
            Node r = x.right;

            // 把右树的左孩子接到自己的右数树上
            x.right = r.left;
            if (r.left != null) {
                r.left.father = x;
            }

            // 交换父亲
            r.father = x.father;
            if (r.father == null)
                root = r;
            else if (r.father.left == x)
                r.father.left = r;
            else
                r.father.right = r;

            r.left = x;
            x.father = r;
        }
    }


    private void rotateRight(Node x) {
        if (x != null) {
            Node l = x.left;

            x.left = l.right;
            if (l.right != null) {
                l.right.father = x;
            }

            l.father = x.father;
            if (l.father == null)
                root = l;
            else if (l.father.left == x)
                l.father.left = l;
            else
                l.father.right = l;

            x.father = l;
            l.right = x;
        }
    }

    private Node search(int data) {
        if (root == null) {
            return null;
        }
        Node cur = root;
        while (cur != null) {
            if (cur.data < data) {
                cur = cur.right;
            } else if (cur.data == data) {
                return cur;
            } else {
                cur = cur.left;
            }
        }
        return cur;
    }

    private boolean colorOf(Node x) {
        return x == null ? Node.BLACK : x.color;
    }

    public void midVisit() {
        Node cur = root;
        Stack<Node> stack = new Stack<>();
        while (!stack.empty() || cur != null) {

            while (cur != null) {
                stack.push(cur);
                System.out.print(cur.data + " " + "(" + (cur.color ? "BLACK" : "RED") + ")");
                cur = cur.left;
            }
            cur = stack.pop();

            cur = cur.right;
        }

        System.out.println();
    }

    static class Node {
        public static final boolean BLACK = true;
        public static final boolean RED = false;

        int data;
        boolean color;
        Node father;
        Node left;
        Node right;

        Node(int data, Node father) {
            this.data = data;
            this.father = father;
            this.left = null;
            this.right = null;
        }
    }


    public static void main(String[] args) {
        int[] testData = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        RedBlackTree tree = new RedBlackTree();
        for (int i : testData) {
            tree.insert(i);
        }

        tree.midVisit();
        tree.remove(4);
        tree.remove(8);
        tree.midVisit();
    }

}
