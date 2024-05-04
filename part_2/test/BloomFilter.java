package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet;
    private final int size;
    private final String[] hashAlgorithms;

    public BloomFilter(int size, String... hashAlgorithms) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashAlgorithms = hashAlgorithms;
    }

    public void add(String word) {
        for (String algorithm : hashAlgorithms) {
            int hashIndex = getHashIndex(word, algorithm);
            if (hashIndex >= 0 && hashIndex < size) { // Check if index is within bounds
                bitSet.set(hashIndex);
            }
        }
    }

    public boolean contains(String word) {
        for (String algorithm : hashAlgorithms) {
            int hashIndex = getHashIndex(word, algorithm);
            if (hashIndex >= 0 && hashIndex < size) { // Check if index is within bounds
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
            int hashIndex = Math.abs(bigInt.intValue()) % size;
            return hashIndex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
    
}
