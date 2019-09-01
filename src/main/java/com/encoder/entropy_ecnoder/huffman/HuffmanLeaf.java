package com.encoder.entropy_ecnoder.huffman;

public class HuffmanLeaf extends HuffmanTree {

	private static final long serialVersionUID = 1L;
	public final char value; // the character this leaf represents

    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}