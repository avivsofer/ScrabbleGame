package test;
import java.util.HashSet;

public class CacheManager {

    static int maxSize; // גדול ה- cache המקסימלי
    private final HashSet<String> cache;
    private final CacheReplacementPolicy crp;


    //מקבל את הגודל המקסימלי של cache 
    public CacheManager(int maxSize, CacheReplacementPolicy crp) {
        CacheManager.maxSize = maxSize; 
        this.cache = new HashSet<>();
        this.crp = crp;
    }

    //בודקת האם המילה word קיימת ב- cache
    public boolean query(String word) {
        return cache.contains(word); 
    }

    public void add(String word) { 
        
        if (cache.size() >= maxSize) {
            String wordToRemove = crp.remove(); //  מציאת המילה שיש להסיר בעזרת LRU או LFU
            cache.remove(wordToRemove); //הסרת המילה שמצאנו
        }

        crp.add(word); //הוספת המילה ל crp
        cache.add(word); // הוספת המילה החדשה לזכרון
    }

    public int getMaxSize() {
        return maxSize;
    }

    public HashSet<String> getCache() {
        return cache;
    }

    public CacheReplacementPolicy getCrp() {
        return crp;
    }
}
