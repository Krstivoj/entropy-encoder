package com.encoder.entropy_encoder.huffman;

public class HuffmanNode {
    String ch;
    double frequency;
    HuffmanNode left;
    HuffmanNode right;

    HuffmanNode(String ch, double frequency,  HuffmanNode left,  HuffmanNode right) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}
