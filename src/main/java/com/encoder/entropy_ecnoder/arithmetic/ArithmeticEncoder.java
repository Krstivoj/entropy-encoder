package com.encoder.entropy_ecnoder.arithmetic;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticEncoder {

    private char low;
    private char high;
    private char underflow_bits;
    private char current_output_char;
    private char output_mask;
    private char scale;

    private List<Symbol> codes;
    private CharArrayWriter output_buffer;

    public ArithmeticEncoder() {
        this.codes = new ArrayList<>();
        this.scale = 1 ;
    }

    public ArithmeticEncoder(ArrayList<Symbol> symbols, char scale) {
        this.codes = symbols ;
        this.scale = scale;
    }

    public CharArrayWriter compress(String input) {
        int i;
        char c;
        Symbol s = new Symbol();

        initializeOutputBitStream();
        initializeArithmeticEncoder();
        for (i = 0; i < input.toCharArray().length; i++) {
            c = input.toCharArray()[i];
            convertCharToSymbol(c, s);
            encodeSymbol(s);
            if (c == '\0')
                break;
        }
        flushArithmeticEncoder();
        flushOutputBitStream();
        return output_buffer;
    }

    private void initializeArithmeticEncoder() {
        low = (char) 0;
        high = (char) 0xffff;
        underflow_bits = (char) 0;
    }

    private void flushArithmeticEncoder() {
        outputBit((char) (low & (0x4000)));
        underflow_bits++;
        while (underflow_bits-- > 0)
            outputBit((char) (~low & (char) 0x4000));
    }

    private void initializeOutputBitStream() {
        output_buffer = new CharArrayWriter();
        current_output_char = (char) 0;
        output_mask = (char) 0x8000;
    }

    private void outputBit(char bit) {
        if (bit > (char) 0)
            current_output_char = (char) (current_output_char | output_mask);
        output_mask >>= (char) 1;
        if (output_mask == 0) {
            output_mask = (char) 0x8000;
            output_buffer.write(current_output_char);
            current_output_char = (char) 0;
        }
    }

    private void flushOutputBitStream() {
        output_buffer.write(current_output_char);
    }

    private void convertCharToSymbol(char c, Symbol s) {
        int i = 0;
        for (; ; ) {
            if (c == codes.get(i).symbol) {
                s.symbol = c;
                s.low = codes.get(i).low;
                s.high = codes.get(i).high;
                return;
            }
            if (i == (codes.size() - 1))
                System.out.println("Trying to encode a char not in the table");
            i++;
        }
    }

    private void encodeSymbol(Symbol s) {
        long range;

        range = (long) (high - low) + 1;

        high = (char) (low + (char) ((range * s.high) / scale) - (char) 1);

        low = (char) (low + (char) ((range * s.low) / scale));

        for (; ; ) {
            if (((char) (high & (char) 0x8000)) == ((char) (low & (char) 0x8000))) {
                outputBit((char) (high & (char) 0x8000));
                while (underflow_bits > 0) {
                    outputBit((char) (~high & (char) 0x8000));
                    underflow_bits--;
                }

            }
            else if ((((char) (low & (char) 0x4000) != (char) 0)) && (((char) (high & (char) 0x4000)) == (char) 0)) {
                underflow_bits++;
                low &= (char) 0x3fff;
                high |= (char) 0x4000;
            } else {
                return;
            }
            low = (char) (low << (char) 1);
            high = (char) (high << (char) 1);
            high = (char) (high | (char) 1);
        }
    }

    private static String getEffectiveBinary(String hex) {
        if (hex.length() > 2) {
            String firstPart = hex.substring(0, 2);
            String secondPart = hex.substring(2);
            return Integer.toBinaryString(Integer.parseInt(firstPart, 16)) + Integer.toBinaryString(Integer.parseInt(secondPart, 16));
        }else
            return Integer.toBinaryString(Integer.parseInt(hex, 16));
    }

    public String getCompressedAsBinary(String text) {
        CharArrayWriter baos = compress(text);
        StringBuilder binaryRepresentation = new StringBuilder();
        for (char b : baos.toCharArray()) {
            binaryRepresentation.append(getEffectiveBinary(Integer.toHexString((int)b)));
        }
        String retVal =  binaryRepresentation.toString() ;
        return retVal.substring(0,retVal.lastIndexOf('1')+1);
    }
}