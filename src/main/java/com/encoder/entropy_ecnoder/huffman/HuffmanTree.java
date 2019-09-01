package com.encoder.entropy_ecnoder.huffman;

import java.io.Serializable;

abstract class HuffmanTree implements Comparable<HuffmanTree>, Serializable {

	private static final long serialVersionUID = 1L;
	public final int frequency;

    public HuffmanTree(int freq) {
        frequency = freq;
    }

    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}
