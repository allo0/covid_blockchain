package protal.bcserver.clockchain;

import java.util.List;

public class ChainValidator {
    public static Boolean isChainValid(int prefix, List<block> blockChain) {
        block currentBlock;
        block previousBlock;
        String hashTarget = new String(new char[prefix]).replace('\0', '0');
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hash not valid");
                return false;
            }
            if (!currentBlock.getHash().substring(0, prefix).equals(hashTarget)) {
                System.out.println("Block is not properly mined");
                return false;
            }
        }
        return true;
    }
}
