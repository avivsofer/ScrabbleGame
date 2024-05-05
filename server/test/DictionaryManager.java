package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {

    private Map<String, DictionaryProxy> mapDictionaries;
    private static DictionaryManager instance; // סינגלטון

    private DictionaryManager() {
        mapDictionaries = new HashMap<>();
    }

    public static synchronized DictionaryManager get() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    private ArrayList<DictionaryProxy> getDictionaryProxies(String[] fileNames) {
        ArrayList<DictionaryProxy> proxies = new ArrayList<>();
        for (String file : fileNames) {
            if (!mapDictionaries.containsKey(file)) {
                
                DictionaryProxy pDictionary = new DictionaryProxy(new Dictionary(file));
                mapDictionaries.put(file, pDictionary);
            }
            proxies.add(mapDictionaries.get(file));
        }
        return proxies;
    }

    public synchronized boolean query(String... args) {
        boolean result = false;
        String[] fileNames = new String[args.length - 1];
        String searchWord = args[args.length - 1];  
        System.arraycopy(args, 0, fileNames, 0, fileNames.length);
        ArrayList<DictionaryProxy> proxies = getDictionaryProxies(fileNames);

        for (DictionaryProxy proxy : proxies) {
            if (proxy.query(searchWord)) {
                result = true;
                break; 
            }
        }

        return result;
    }

    public synchronized boolean challenge(String... args) {
        boolean result = false;
        String[] fileNames = new String[args.length - 1];
        String searchWord = args[args.length - 1];  
        System.arraycopy(args, 0, fileNames, 0, fileNames.length);
        ArrayList<DictionaryProxy> proxies = getDictionaryProxies(fileNames);

        for (DictionaryProxy proxy : proxies) {
            if (proxy.challenge(searchWord)) {
                result = true;
                break; 
            }
        }

        return result;
    }

    public synchronized int getSize() {
        return mapDictionaries.size();
    }

    public class DictionaryProxy extends Dictionary {
        private final Dictionary dictionaryForProxy;

        public DictionaryProxy(Dictionary dictionary) {
            this.dictionaryForProxy = dictionary;
        }

        @Override
        public boolean query(String word) {
            return dictionaryForProxy.query(word);
        }

        @Override
        public boolean challenge(String word) {
            return dictionaryForProxy.challenge(word);
        }
    }
}
