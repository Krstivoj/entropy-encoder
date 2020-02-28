package com.encoder.entropy_encoder.huffman;

import java.util.*;

class HuffManComparator implements Comparator<HuffmanNode> {
    @Override
    public int compare(HuffmanNode node1, HuffmanNode node2) {
        return Double.compare(node1.frequency,node2.frequency);
    }
}

public class HuffmanCoding {
    public HuffmanNode buildTree(Map<String, Double> map) {
        Queue<HuffmanNode> nodeQueue = createQueue(map);
        while (nodeQueue.size() > 1) {
            HuffmanNode node1 = nodeQueue.remove();
            HuffmanNode node2 = nodeQueue.remove();
            HuffmanNode node = new HuffmanNode("\0", node1.frequency + node2.frequency, node1, node2);
            nodeQueue.add(node);
        }

        return nodeQueue.remove();
    }

    private static Queue<HuffmanNode> createQueue(Map<String, Double> map) {
        Queue<HuffmanNode> pq = new PriorityQueue<>( new HuffManComparator());

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }
        return pq;
    }

    public Map<String, String> createHuffmanCodes(HuffmanNode node) {
        Map<String, String> map = new HashMap<>();
        createCodeRec(node, map, "");
        return map;
    }

    private void createCodeRec(HuffmanNode node, Map<String, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.ch, s);
            return;
        }
        createCodeRec(node.left, map, s + '0');
        createCodeRec(node.right, map, s + '1' );
    }

}