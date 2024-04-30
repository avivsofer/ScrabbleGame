package test;
import java.util.HashSet;

public class CacheManager {

    private final int maxSize; // גדול ה- cache המקסימלי
    private final HashSet<String> cache;
    private final CacheReplacementPolicy crp;

    //מקבל את הגודל המקסימלי של cache 
    public CacheManager(int maxSize, CacheReplacementPolicy crp) {
        this.maxSize = maxSize; 
        this.cache = new HashSet<>();
        this.crp = crp;
    }

    //בודקת האם המילה word קיימת ב- cache
    public boolean query(String word) {
        return cache.contains(word); 
    }

    public void add(String word) { 
        
        if (cache.size() >= maxSize) {
            String wordToRemove = crp.remove(); // מציאת המילה שיש להסיר
            cache.remove(wordToRemove); //הסרת המילה שמצאנו
        }

        crp.add(word); //הוספת המילה ל crp
        cache.add(word); // הוספת המילה החדשה לזכרון
    }
}
