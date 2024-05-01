package test;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;


public class LFU implements CacheReplacementPolicy {

    private final int capacity; //מאחסן את הקיבולת המקסימלית של המטמון
    private final HashMap<String, Node> cache; //מאחסן זוגות מפתח-ערך
    private final LinkedHashMap<String, Integer> freqMap; //מאחסן זוגות מפתח-ערך


    public LFU() {
        this.capacity = 10;
        this.cache = new HashMap<>(); //יצירת אובייקט חדש מסוג HashMap והשמתו
        this.freqMap = new LinkedHashMap<>(capacity, 0.75f, true); //אובייקט חדש מסוג LinkedHashMap עם קיבולת מוגדרת (capacity), גורם עומס (0.75f) וסדר גישה מופעל (true)
    }

    @Override
    public void add(String word) {
        if (cache.containsKey(word)) {
            cache.get(word).updateFreq();
            updateFreq(word);
            return;
        }

        if (cache.size() == capacity) {
            evictLeastFrequent();
        }

        Node node = new Node(word, 1);
        cache.put(word, node);
        freqMap.put(word, 1);
    }

    @Override
    public String remove() {
        int minFreq = Integer.MAX_VALUE;
        String keyToEvict = null;

    // Using iterator to avoid ConcurrentModificationException
        Iterator<Map.Entry<String, Integer>> iterator = freqMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            int freq = entry.getValue();

            if (freq < minFreq) {
                minFreq = freq;
                keyToEvict = key;
            }
        }

        cache.remove(keyToEvict);
        freqMap.remove(keyToEvict);
        return keyToEvict;
}


    
    private void updateFreq(String word) {
        int freq = freqMap.get(word);
        freqMap.put(word, freq + 1);

        if (freq * 1.0 / freqMap.size() > 0.5) {
            freqMap.remove(word);
            freqMap.put(word, freq + 1);
        }
    }

    private void evictLeastFrequent() {
        int minFreq = Integer.MAX_VALUE;
        String keyToEvict = null;

        for (String key : freqMap.keySet()) {
            int freq = freqMap.get(key);
            if (freq < minFreq) {
                minFreq = freq;
                keyToEvict = key;
            }
        }

        cache.remove(keyToEvict);
        freqMap.remove(keyToEvict);
    }

    // **Static** Node class to make it accessible outside LFU
    public static class Node {
        private final String word;
        private int freq;

        public Node(String word, int freq) {
            this.word = word;
            this.freq = freq;
        }

        public void updateFreq() {
            this.freq++; 
        }
    }
}
