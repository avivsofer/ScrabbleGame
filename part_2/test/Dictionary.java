package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
   
CacheManager existWordsCacheLRU;
CacheManager notExistCacheLFU;
BloomFilter bloomFilter;
private final String[] existFileNames;

    //בנאי המחלקה לפי הדרישות
    public Dictionary (String...fileNames) {
        int cacheSizeLRU = 400;
        int cacheSizeLFU = 100;
        existFileNames = fileNames;

        existWordsCacheLRU = new CacheManager(cacheSizeLRU, new LRU());
        notExistCacheLFU = new CacheManager(cacheSizeLFU, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");

        for (String file : fileNames) {

            try (BufferedReader bufFile = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufFile.readLine()) != null) {
                    String[] words = line.split(" ");  
                    
                    for (String word : words) { //מעבר על כל מערך המילים
                        try {
                            if (!word.isEmpty())
                            bloomFilter.add(word); // הוספת המילה לבלום פילטר
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean query (String word) {
        if (existWordsCacheLRU.query(word)) {
            return true; 
        }

        if (notExistCacheLFU.query(word)) {
            return false;
        }

        if (bloomFilter.contains(word)) {
            existWordsCacheLRU.add(word);
             return true;
        } else {
            notExistCacheLFU.add(word);
            return false;
        }
    }

    public Boolean challenge(String word) {
        Boolean wordExs = IOSearcher.search(word, existFileNames);
        if (wordExs) {
            existWordsCacheLRU.add(word);
            bloomFilter.add(word);
        } else {
            notExistCacheLFU.add(word);
        }
        return wordExs;
    }

}


