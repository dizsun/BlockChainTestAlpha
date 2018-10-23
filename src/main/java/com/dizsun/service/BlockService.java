package com.dizsun.service;

import com.dizsun.component.Block;
import com.dizsun.util.CryptoUtil;
import com.dizsun.util.RSAUtil;
import com.dizsun.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;


public class BlockService {
    private List<Block> blockChain;
    private SQLUtil sqlUtil;
    private static BlockService blockService=null;
    public BlockService(int nonce) {
        this.sqlUtil=new SQLUtil(nonce);
        this.blockChain = new ArrayList<Block>();

        List<Block> dbBlocks = sqlUtil.queryBlocks();
        if(dbBlocks==null){
            blockChain.add(this.getFirstBlock());
            sqlUtil.initBlocks(blockChain);
        }else{
            blockChain=dbBlocks;
        }
    }

    /**
     * 计算区块hash
     * 将(索引+前一个区块hash+时间戳+数据)进行hash
     * @param index
     * @param previousHash
     * @param timestamp
     * @param data
     * @return
     */
    private String calculateHash(int index, String previousHash, long timestamp, String data) {
        StringBuilder builder = new StringBuilder(index);
        builder.append(previousHash).append(timestamp).append(data);
        return CryptoUtil.getSHA256(builder.toString());
    }

    public Block getLatestBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    private Block getFirstBlock() {
        return new Block(1, "0", 0,
                "Hello Block", "1db6aa3c81dc4b05a49eaed6feba99ed4ef07aa418d10bfbbc12af68cab6fb2a","init pk",0,"init signature");
    }

    /**
     * 生成新区块
     * @param blockData
     * @return
     */
    public Block generateNextBlock(String blockData,int VN) {
        Block previousBlock = this.getLatestBlock();
        int nextIndex = previousBlock.getIndex() + 1;
        long nextTimestamp = System.currentTimeMillis();
        String nextHash = calculateHash(nextIndex, previousBlock.getHash(), nextTimestamp, blockData);
        //int proof=createProofOfWork(previousBlock.getProof(),previousBlock.getHash());
        RSAUtil rsaUtil = RSAUtil.getInstance();
        return new Block(nextIndex, previousBlock.getHash(), nextTimestamp, blockData, nextHash,rsaUtil.getPublicKeyBase64(),VN,rsaUtil.encrypt(blockData));
    }

    public void addBlock(Block newBlock) {
        if (isValidNewBlock(newBlock, getLatestBlock())) {
            sqlUtil.addBlock(newBlock);
            blockChain.add(newBlock);
        }
    }

    /**
     * 验证新区块是否合法
     * @param newBlock
     * @param previousBlock
     * @return
     */
    private boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
            System.out.println("无效的 index");
            return false;
        } else if (!previousBlock.getHash().equals(newBlock.getPreviousHash())) {
            System.out.println("无效的 previoushash");
            return false;
        } else {
            String hash = calculateHash(newBlock.getIndex(), newBlock.getPreviousHash(), newBlock.getTimestamp(),
                    newBlock.getData());
            if (!hash.equals(newBlock.getHash())) {
                System.out.println("无效的 hash: " + hash + " " + newBlock.getHash());
                return false;
            }
            RSAUtil rsaUtil = RSAUtil.getInstance();
            if(!rsaUtil.decrypt(newBlock.getPk(),newBlock.getSignature()).equals(newBlock.getData())){
                System.out.println("【BlockService】无效的区块数据:"+newBlock.getData()+",解密数据："+rsaUtil.decrypt(newBlock.getPk(),newBlock.getSignature()));
                return false;
            }
//            if(!isValidProof(previousBlock.getProof(),newBlock.getProof(),previousBlock.getHash())) {
//                System.out.println("无效的证明:"+newBlock.getProof());
//                return false;
//            }
        }
        return true;
    }

    /**
     * 用新链替换旧链
     * @param newBlocks
     */
    public void replaceChain(List<Block> newBlocks) {
        if (isValidBlocks(newBlocks) && newBlocks.size() > blockChain.size()) {
            sqlUtil.replaceChain(newBlocks);
            blockChain = newBlocks;
        } else {
            System.out.println("收到的区块链为无效链");
        }
    }

    /**
     * 验证区块链是否合法
     * @param newBlocks
     * @return
     */
    private boolean isValidBlocks(List<Block> newBlocks) {
        Block firstBlock = newBlocks.get(0);
        if (!firstBlock.equals(getFirstBlock())) {
            return false;
        }

        for (int i = 1; i < newBlocks.size(); i++) {
            if (!isValidNewBlock(newBlocks.get(i), newBlocks.get(i-1))){
                return false;
            }
        }
        return true;
    }

//    /**
//     * 验证工作量是否正确
//     * @param lastProof
//     * @param proof
//     * @param previousHash
//     * @return
//     */
//    private boolean isValidProof(int lastProof,int proof,String previousHash){
//        String guess=""+lastProof+proof+previousHash;
//        String result = CryptoUtil.getSHA256(guess);
//        return result.startsWith("0000");
//    }

//    /**
//     * 创建工作量证明
//     * @param lastProof
//     * @param previousHash
//     * @return
//     */
//    private int createProofOfWork(int lastProof,String previousHash){
//        int proof=0;
//        while (!isValidProof(lastProof,proof,previousHash))
//            proof++;
//        return proof;
//    }

    public List<Block> getBlockChain() {
        return blockChain;
    }
}
