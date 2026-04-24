package org.voting.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTransparencyLog {
    private final List<Block> chain = new ArrayList<>();

    public MerkleTransparencyLog() {
        chain.add(new Block("0", "GENESIS"));
    }

    public void addBlock(List<String> votes) {
        String previousHash = chain.get(chain.size() - 1).hash;
        String content = String.join(",", votes);
        chain.add(new Block(previousHash, content));
    }

    public boolean verifyIntegrity() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);
            
            if (!current.previousHash.equals(previous.hash)) return false;
            if (!current.hash.equals(calculateHashStatic(current.previousHash + current.content))) return false;
        }
        return true;
    }

    public void corruptBlock(int index, String newContent) {
        if (index < chain.size()) {
            chain.get(index).content = newContent;
        }
    }

    private static String calculateHashStatic(String input) {
        try {
            MessageDigest d = MessageDigest.getInstance("SHA-256");
            byte[] h = d.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * h.length);
            for (byte b : h) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) { return ""; }
    }

    public static class Block {
        public String previousHash;
        public String content;
        public String hash;

        public Block(String previousHash, String content) {
            this.previousHash = previousHash;
            this.content = content;
            this.hash = calculateHashStatic(previousHash + content);
        }
    }
}
