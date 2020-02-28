package com.encoder.entropy_encoder.arithmetic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArithmeticDecoder {

    private char low;
    private char high;
    private char code;
    private char current_input_byte;
    private char scale ;

    private List<Symbol> codes;
    private ByteArrayInputStream input_buffer;

    private char input_bits_left = (char) 0;
    private char input_bytes_left;

    public ArithmeticDecoder(List<Symbol> symbols,String encodedSequence, char scale) {

        byte[] bytes = getByteFromBitArray(encodedSequence);
        input_buffer = new ByteArrayInputStream(bytes);
        input_bytes_left = (char) bytes.length ;
        this.codes = new ArrayList<>();
        this.codes.addAll(symbols);
        this.scale = scale;
    }

    private void initializeInputBitStream(){
        input_bits_left = 0;
        current_input_byte = 0;
        input_buffer.reset();
    }

    private char inputBit(){
        if (input_bits_left == (char)0)
        {
            if (input_bytes_left > 0)
                current_input_byte = (char)(input_buffer.read());

            input_bytes_left--;
            input_bits_left = (char) 8;
        }
        input_bits_left--;
        return (char)((current_input_byte >> input_bits_left) & (char)1);
    }

    private void removeSymbolFromStream(Symbol s){
        long range;
        range = (long)(high - low + 1);

        high = (char)(low + (char)((range * s.getHigh()) / scale - 1));
        low = (char)(low + (char)((range * s.getLow()) / scale));

        for (; ; )
        {

            if ( (high & (char)0x8000) == (low & (char)0x8000)){
            }
            else if ((low & (char)0x4000) == (char)0x4000 && (high & (char)0x4000) == (char)0)
            {
                code ^= (char)0x4000;
                low &= (char)0x3fff;
                high |= (char)0x4000;
            }
            else return;

            low <<= (char) 1;
            high <<= (char) 1;
            high |= (char) 1;
            code <<= (char) 1;
            code += inputBit();
        }
    }

    private void initializeArithmeticDecoder(){
        char i;

        initializeInputBitStream();

        code = (char)0;
        for (i = 0; i < 16; i++)
        {
            code <<= (char) 1;
            code += inputBit();
        }
        low = (char) 0;
        high = (char) 0xffff;
    }

    private char getCurrentCount()
    {
        long range;
        char count;
        range = (long) (( high - low ) + 1);
        count = (char) ((long)(( code - low + 1 ) * scale - 1 ) / range );
        return count;
    }

    private char convertToSymbol(char count, Symbol s){
        char returnValue = ' ';

        for (Symbol symbol : codes){
            if (count >= symbol.getLow() && count < symbol.getHigh())
            {
                s.setLow(symbol.getLow());
                s.setHigh(symbol.getHigh());
                s.setSymbol(symbol.getSymbol());
                returnValue = symbol.getSymbol();
            }
        }
        return returnValue;
    }

    public String decode(char size){
        char c;
        char count;
        StringBuilder stringBuilder = new StringBuilder();

        Symbol s = new Symbol();

        initializeArithmeticDecoder();

        for (int i = 0; i < size; i ++)
        {
            count = getCurrentCount();
            c = convertToSymbol(count, s);
            stringBuilder.append(c);
            removeSymbolFromStream(s);

        }
        return stringBuilder.toString();
    }

    private String fillRemainingPositions(String input, char character, int length){
        StringBuilder stringBuilder = new StringBuilder();
        char[] pad = new char[length - input.length()];
        Arrays.fill(pad, character);
        stringBuilder.append(input).append(pad);
        return stringBuilder.toString();
    }

    private byte[] getByteFromBitArray(String bitArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String tmp ;
        for (int i = 0, k ; i < bitArray.length() ; i += 8){
            k = i + 8 ;
            if(k >= bitArray.length()){
                k = bitArray.length();
            }
            tmp = fillRemainingPositions(bitArray.substring(i,k),'0',8);
            byteArrayOutputStream.write((char)Integer.parseInt(tmp,2));
        }
        return byteArrayOutputStream.toByteArray();
    }

}
