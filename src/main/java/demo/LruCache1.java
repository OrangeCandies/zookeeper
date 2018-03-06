package demo;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache1<K, V> {

    private int maxSize;
    private Map<K, V> map = new LinkedHashMap<K, V>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return map.size() > maxSize;
        }
    };

    public void put(K key, V val) {
        map.put(key, val);
    }

    public V get(K key) {
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return null;
        }
    }

}
