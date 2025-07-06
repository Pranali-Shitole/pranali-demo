import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

class Block {
    public String previousHash;
    public String data;
    public String hash;
    public int nonce;

    public Block(String data, String previousHash) {
        this.previousHash = previousHash;
        this.data = data;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = previousHash + data + nonce;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = "0".repeat(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    @Override
    public String toString() {
        return String.format("Block[data=%s, hash=%s, prevHash=%s]", data, hash, previousHash);
    }
}

class BlockchainNode {
    private List<Block> blockchain = new ArrayList<>();
    private final int difficulty;

    public BlockchainNode(int difficulty) {
        this.difficulty = difficulty;
        Block genesis = new Block("Genesis", "0");
        genesis.mineBlock(difficulty);
        blockchain.add(genesis);
    }

    public void mineBlock(String data) {
        Block previousBlock = blockchain.get(blockchain.size() - 1);
        Block newBlock = new Block(data, previousBlock.hash);
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public List<Block> getChain() {
        return blockchain;
    }

    public boolean isValidChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block current = blockchain.get(i);
            Block previous = blockchain.get(i - 1);
            if (!current.hash.equals(current.calculateHash()) || !current.previousHash.equals(previous.hash)) {
                return false;
            }
        }
        return true;
    }

    public void replaceChain(List<Block> newChain) {
        if (newChain.size() > this.blockchain.size()) {
            this.blockchain = new ArrayList<>(newChain);
            System.out.println("Chain updated to longer chain.");
        }
    }

    public void printChain() {
        blockchain.forEach(System.out::println);
    }
}

public class BlockchainConsensus {

    public static void main(String[] args) {
        final int difficulty = 3;
        BlockchainNode nodeA = new BlockchainNode(difficulty);
        BlockchainNode nodeB = new BlockchainNode(difficulty);

        System.out.println("=== Node A mining ===");
        nodeA.mineBlock("A: Block 1");
        nodeA.mineBlock("A: Block 2");

        System.out.println("\n=== Node B mining ===");
        nodeB.mineBlock("B: Block 1");
        System.out.println("\n=== Node B synchronizes with Node A ===");
        if (nodeA.getChain().size() > nodeB.getChain().size() && nodeA.isValidChain()) {
            nodeB.replaceChain(nodeA.getChain());
        }

        System.out.println("\n=== Final Chain at Node B ===");
        nodeB.printChain();
    }
}

