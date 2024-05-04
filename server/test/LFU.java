package test;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

public class LFU implements CacheReplacementPolicy { // מחזירה את המילה שאותה ביקשו הכי מעט פעמים

    int maxSizeOfCache = CacheManager.maxSize; // קיבולת המטמון המקסימלית 
    private final HashMap<String, Node> cache = new HashMap<>(); // מאחסן זוגות מפתח(שם המילה במחרוזת טקסט) - ערך(אובייקט מסוג צומת המכיל את שם המילה ותדירות השימוש בה)
    private final LinkedHashMap<String, Integer> freqMap= new LinkedHashMap<>(maxSizeOfCache, 0.90f, true); // מאחסן זוגות מפתח(שם המילה) - ערך(תדירות השימוש בה) 



    @Override
    public void add(String word) {
        if (cache.containsKey(word)) { // בדיק אם המילה קיימת במטמון 
            cache.get(word).updateFreq(); // עדכון תדירות המילה במטמון
            updateFreq(word); //  עדכון תדירות המילה במבני נתונים על ידי פונקציית עזר
            return;
        }

        if (cache.size() == maxSizeOfCache) {
            evictLeastFrequent();
        }

        Node node = new Node(word, 1); // שם המילה ותדירות השימוש בה
        cache.put(word, node);
        freqMap.put(word, 1);
    }

    @Override
public String remove() {
    int minFreq = Integer.MAX_VALUE;
    String keyToEvict = null;

    // איטרטור לעבר המפתחות והערכים במפת התדירויות
    Iterator<Map.Entry<String, Integer>> iterator = freqMap.entrySet().iterator();
    while (iterator.hasNext()) {
        Map.Entry<String, Integer> entry = iterator.next();
        int freq = entry.getValue();
        if (freq < minFreq) {
            minFreq = freq;
            keyToEvict = entry.getKey();
        }
    }

    // ביצוע המחיקה מהמפה ומהמטמון באופן בטוח באמצעות האיטרטור
    if (keyToEvict != null) {
        iterator.remove(); // הסרת הפריט מהאיטרטור
        cache.remove(keyToEvict);
    }

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
