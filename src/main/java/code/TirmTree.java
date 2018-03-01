package code;

public class TirmTree {

    private Node root = new Node(false);

    public void insert(String ip){
        String[] strings = ip.split("\\.");
        Node currentNode = root;
        for(int i = 0; i < strings.length; i++){
            int pos = Integer.parseInt(strings[i]);
            if(currentNode.next == null){
                currentNode.next = new Node[256];
            }
            if(currentNode.next[pos] == null){
                currentNode.next[pos] = new Node(false);
            }
            currentNode = currentNode.next[pos];
        }

        currentNode.isExists = true;
    }

    public boolean isExists(String ip){
        String [] strings = ip.split("\\.");
        Node current = root;
        for(int i=0; i< 4;i++){
            int pos = Integer.parseInt(strings[i]);
            if(current.next == null || current.next[pos] == null){
                return false;
            }
            current = current.next[pos];
        }
        return current.isExists;
    }


    static class Node{
        boolean isExists;
        Node []next;

        Node(boolean isExists){
            this.isExists = isExists;
        }

        Node find(int number){
            if(number <= 255 && number >= 0){
                return next[number];
            }
            return null;
        }

        void insert(int number){
            if(number <= 255 && number >= 0){
                next[number] = new Node(true);
            }
        }

    }

    public static void main(String[] args) {
        TirmTree tree = new TirmTree();
        tree.insert("127.0.0.1");
        tree.insert("192.168.0.1");
        System.out.println(tree.isExists("0.0.0.0"));
        System.out.println(tree.isExists("127.0.0.1"));
    }

}
