package code;

import java.util.Stack;

public class RedBlackTree {

    static class Node{
        public static final boolean BLACK = true;
        public static final boolean RED = false;

        int data;
        boolean color;
        Node father;
        Node left;
        Node right;

        Node(int data,Node father){
            this.data = data;
            this.father = father;
            this.left = null;
            this.right = null;
        }
    }



    private Node root = null;

    RedBlackTree(){
    }

    public void insert(int data) {
        if (root == null) {
            root = new Node(data, null);
            root.color = Node.BLACK;
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
            }else{
                return;
            }
        }
        makeBalancedAfterInsert(current);
    }

    private void makeBalancedAfterInsert(Node current) {
        current.color = Node.RED;
        while(current != null && current != root && current.father.color == Node.RED){
            if(current.father == current.father.father.left){
                Node uncle = current.father.father.right;
                if(colorOf(uncle) == Node.RED){
                    current.father.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    current.father.father.color = Node.RED;
                    current = current.father.father;
                }else{
                    if(current == current.father.right){
                        current = current.father;
                        rotateLeft(current);
                    }
                    current.father.color = Node.BLACK;
                    current.father.father.color = Node.RED;
                    rotateRight(current.father.father);
                }
            }else{
                if(current.father == current.father.father.right){
                    Node uncle = current.father.father.left;
                    if(colorOf(uncle) == Node.RED){
                        current.father.color = Node.BLACK;
                        current.father.father.color = Node.RED;
                        uncle.color = Node.BLACK;
                        current = current.father.father;
                    }else{
                        if(current == current.father.left){
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


    private void rotateLeft(Node x){
        if(x != null){
            Node r = x.right;

            // 把右树的左孩子接到自己的右数树上
            x.right = r.left;
            if(r.left != null){
                r.left.father = x;
            }

            // 交换父亲
            r.father = x.father;
            if(r.father == null)
                root = r;
            else if(r.father.left == x)
                r.father.left = r;
            else
                r.father.right = r;

            r.left = x;
            x.father = r;
        }
    }


    private void rotateRight(Node x){
        if(x != null){
            Node l = x.left;

            x.left = l.right;
            if(l.right != null){
                l.right.father = x;
            }

            l.father = x.father;
            if(l.father == null)
                root = l;
            else if(l.father.left == x)
                l.father.left = l;
            else
                l.father.right = l;

            x.father = l;
            l.right = x;
        }
    }


    private boolean colorOf(Node x){
        return x == null ? Node.BLACK : x.color;
    }
    public void midVisit(){
        Node cur = root;
        Stack<Node> stack = new Stack<>();
        while(!stack.empty()||cur != null){

            while(cur != null ){
                stack.push(cur);
                System.out.print(cur.data + " "+"("+(cur.color?"BLACK":"RED")+")");
                cur = cur.left;
            }
            cur = stack.pop();

            cur = cur.right;
        }

        System.out.println();
    }


    public static void main(String[] args) {
        int[] testData = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
        RedBlackTree tree = new RedBlackTree();
        for(int i:testData){
            tree.insert(i);
        }
        tree.midVisit();
    }

}
