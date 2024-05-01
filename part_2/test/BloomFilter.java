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
            bitSet.set(hashIndex);
        }
    }

    public boolean contains(String word) {
        for (String algorithm : hashAlgorithms) {
            int hashIndex = getHashIndex(word, algorithm);
            if (!bitSet.get(hashIndex)) {
                return false;
            }
        }
        return true;
    }

    private int getHashIndex(String word, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(word.getBytes());
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.mod(BigInteger.valueOf(size)).intValue();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception
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

    public static void main(String[] args) {
        BloomFilter bf = new BloomFilter(256, "SHA-1", "MD5");
        bf.add("hello");
        bf.add("world");
        System.out.println(bf.contains("hello")); // true
        System.out.println(bf.contains("world")); // true
        System.out.println(bf.contains("openai")); // false
        System.out.println(bf); // Display the BitSet
    }
}
