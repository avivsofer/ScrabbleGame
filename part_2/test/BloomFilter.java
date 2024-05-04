package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet; //מבני נתונים לאכסון סדרות של ביטים
    private final int size;
    private final String[] hashAlgorithms;

    public BloomFilter(int size, String... hashAlgorithms) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashAlgorithms = hashAlgorithms; //מערך של האלגוריתמים שיתקבלו
    }

    public void add(String word) {
        for (String algorithm : hashAlgorithms) {
            int hashIndex = getHashIndex(word, algorithm);
            bitSet.set(hashIndex);
        }
    }

    //בהינתן מחרוזת היא תחזיר בוליאני האם היא נמצאת ב bloom filter .
    public boolean contains(String word) {
        for (String algorithm : hashAlgorithms) {
            int hashIndex = getHashIndex(word, algorithm);
            if (hashIndex < size) {
                if (!bitSet.get(hashIndex)) {
                    return false;
                }
            }
        }
        return true;
    }
    

    private int getHashIndex(String word, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(word.getBytes());
            BigInteger bigInt = new BigInteger(1, digest);
            int index = bigInt.mod(BigInteger.valueOf(size)).intValue();
            if (index < 0) { // בדיקה שהערך שיחזור לא שלילי
                index += size; // Ensure index is non-negative
            }
            return index;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return -1;
        }
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
    
}
