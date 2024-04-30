package test;
import java.util.HashMap;
import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy {

    private final int capacity;
    private final HashMap<String, Node> cache;
    private final LinkedList<Node> keys;

    public LRU(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.keys = new LinkedList<>();
    }

    @Override
    public void add(String word) {
        if (cache.containsKey(word)) {
            cache.get(word).updateTimestamp();
            moveToHead(word);
            return;
        }

        if (cache.size() == capacity) {
            evictLeastRecentlyUsed();
        }

        Node node = new Node(word);
        cache.put(word, node);
        keys.addFirst(node);
    }

    @Override
    public String remove() {
        Node node = keys.removeLast();
        cache.remove(node.word);
        return node.word;
    }

    private void moveToHead(String word) {
        Node node = cache.get(word);
        keys.remove(node);
        keys.addFirst(node);
    }

    private void evictLeastRecentlyUsed() {
        Node node = keys.removeLast();
        cache.remove(node.word);
    }

    private static class Node {
        private final String word;
        private long timestamp;

        public Node(String word) {
            this.word = word;
            this.timestamp = System.currentTimeMillis();
        }

        public void updateTimestamp() {
            this.timestamp = System.currentTimeMillis();
        }
    }
}

