package demo;

import java.util.HashMap;
import java.util.Map;

public class LruCache<K,V> {

    static class Node<K,V>{
        Node pre;
        Node next;
        K key;
        V value;

        Node(K key,V value){
            this.key = key;
            this.value = value;
        }

    }

    Node head = new Node(null,null);
    Node tail = new Node(null,null);
    int size;
    int maxsize;
    private Map<K,Node> map = new HashMap<>();

    LruCache(int size){
        this.maxsize = size;
    }

    public void put(K key, Node val){
        if(map.containsKey(key)){
           Node node = map.get(key);
           node.value = val;
            node.pre.next = node.next;
            node.next.pre = node.pre;
            addToTail(node);
            return;
        }
        if(size+1 >= maxsize){
            head.next = head.next.next;
            Node del = head.next;
            del.pre = null;
            del.next.pre = head;
            del.next = null;
            map.remove(del.key);
            return;
        }
        Node node = new Node(key,val);
        addToTail(node);
        size++;
    }

    private void addToTail(Node node){
        node.next = tail;
        node.pre = tail.pre;
        node.pre.next = node;
        tail.pre = node;
    }

    public Node remove(K key){
        if(!map.containsKey(key)){
            return null;
        }
        Node node = map.get(key);
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.next = null;
        node.pre = null;
        return node;
    }


}
