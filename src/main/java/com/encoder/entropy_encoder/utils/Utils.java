package com.encoder.entropy_encoder.utils;

import com.encoder.entropy_encoder.arithmetic.ArithmeticEncoder;
import com.encoder.entropy_encoder.arithmetic.Symbol;
import com.encoder.entropy_encoder.huffman.HuffmanCoding;
import com.encoder.entropy_encoder.model.Arithmetic;
import com.encoder.entropy_encoder.model.Huffman;
import com.encoder.entropy_encoder.model.User;
import com.encoder.entropy_encoder.payload.EncoderDecoderRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

@Component
public class Utils {

    public double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    public double getEntropy(Map<String, Double> charProbabilityPairs) {
        final Double[] retVal = {0.0};
        charProbabilityPairs.forEach((symbol, probability) -> {
            retVal[0] += (probability * log2(probability));
        });
        return (-1) * retVal[0];
    }

    private String getEntropyAsString(Map<String, Double> charProbabilityPairs) {
        return getFormattedDouble(getEntropy(charProbabilityPairs), 4);
    }

    public double getAverageCodeLength(Map<String, String> codeTable, Map<String, Double> symbolProbabilityPairs) {
        final Double[] averageCodeLength = {0.0};
        for (Map.Entry<String, Double> entry : symbolProbabilityPairs.entrySet()) {
            String symbol = entry.getKey();
            Double probability = entry.getValue();
            averageCodeLength[0] += probability * codeTable.get(symbol).length();
        }
        return averageCodeLength[0];
    }

    public String getFormattedDouble(double number, int fractionDigits) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(fractionDigits);
        return decimalFormat.format(number);
    }

    public Huffman generateHuffmanModel(User user, EncoderDecoderRequest encoderDecoderRequest) {
        Huffman huffman = new Huffman();

        HuffmanCoding huffmanCoding = new HuffmanCoding();

        Map<String, String> codeTable = huffmanCoding.createHuffmanCodes(
                huffmanCoding.buildTree(encoderDecoderRequest.getSymbolProbabilityPairs()));

        double averageCodeLength = getAverageCodeLength(codeTable, encoderDecoderRequest.getSymbolProbabilityPairs());

        StringBuilder encodedSequence = new StringBuilder();

        for (char letter : encoderDecoderRequest.getSequence().toCharArray())
            encodedSequence.append(codeTable.get(String.valueOf(letter)));

        final String entropy = getEntropyAsString(encoderDecoderRequest.getSymbolProbabilityPairs());

        huffman.setEfficient(getFormattedDouble(getEntropy(encoderDecoderRequest.getSymbolProbabilityPairs()) / averageCodeLength, 4));
        huffman.setEncodedSequence(encodedSequence.toString());
        huffman.setEntropy(Double.parseDouble(entropy));
        huffman.setSufficient(getFormattedDouble(1 - getEntropy(encoderDecoderRequest.getSymbolProbabilityPairs()) / averageCodeLength, 4));
        huffman.setPlainSequence(encoderDecoderRequest.getSequence());
        huffman.setUser(user);
        huffman.setSymbolCodePairs(codeTable);
        huffman.setSymbolProbabilityPairs(encoderDecoderRequest.getSymbolProbabilityPairs());
        huffman.setAverageCodeLength((int) Math.round(averageCodeLength));
        huffman.setEncodedSequenceLength(encodedSequence.length());
        huffman.setEncoderType("huffman");

        return huffman;
    }

    public Arithmetic generateArithmeticModel(User user, EncoderDecoderRequest encoderDecoderRequest) {
        Arithmetic arithmetic = new Arithmetic();

        Map<String, Double> codeTable = encoderDecoderRequest.getSymbolProbabilityPairs();

        ArithmeticEncoder arithmeticEncoder = new ArithmeticEncoder(createSymbolList(codeTable),(char)findProbabilityFactor(codeTable));
        String encodedSequence = arithmeticEncoder.getCompressedAsBinary(encoderDecoderRequest.getSequence()) ;
        int encodedSequenceLength = encodedSequence.length();

        final String entropy = getEntropyAsString(encoderDecoderRequest.getSymbolProbabilityPairs());

        arithmetic.setEncodedSequence(encodedSequence);
        arithmetic.setEntropy(Double.parseDouble(entropy));
        arithmetic.setPlainSequence(encoderDecoderRequest.getSequence());
        arithmetic.setUser(user);
        arithmetic.setSymbolProbabilityPairs(encoderDecoderRequest.getSymbolProbabilityPairs());
        arithmetic.setEncodedSequenceLength(encodedSequenceLength);
        arithmetic.setEncoderType("arithmetic");

        return arithmetic;
    }

    public int validateRequest(EncoderDecoderRequest encoderDecoderRequest) {
        final BigDecimal[] step = {new BigDecimal("0.0")};
        final Integer[] maxFractionDigits = {0};
        encoderDecoderRequest.getSymbolProbabilityPairs().forEach((symbol, probability) -> {
            if (String.valueOf(probability).substring(String.valueOf(probability).lastIndexOf('.')).length() > maxFractionDigits[0])
                maxFractionDigits[0] = String.valueOf(probability).substring(String.valueOf(probability).lastIndexOf('.')).length();
            step[0] = step[0].add(new BigDecimal(probability));
        });
        step[0] = new BigDecimal(getFormattedDouble(step[0].doubleValue(),maxFractionDigits[0]));
        return step[0].compareTo(BigDecimal.ONE);
    }

    public ArrayList<Symbol> createSymbolList(Map<String, Double> codeTable) {
        final ArrayList<Symbol> symbols = new ArrayList<>() ;
        int coefficient = findProbabilityFactor(codeTable);
        int step = 0 ;
        int stepValue ;
        for (Map.Entry<String, Double> stringDoubleEntry : codeTable.entrySet()) {
            stepValue = step + (int)(stringDoubleEntry.getValue()*coefficient) ;
            symbols.add(new Symbol(stringDoubleEntry.getKey().charAt(0),(char)step,(char)stepValue));
            step = stepValue ;
        }
        return symbols;
    }

    private int findProbabilityFactor(Map<String, Double> codeTable) {
        int i ;
        double minValue = 1;
        for (Map.Entry<String, Double> stringDoubleEntry : codeTable.entrySet()) {
            if (stringDoubleEntry.getValue() < minValue) {
                minValue = stringDoubleEntry.getValue();
            }
        }
        String stringMinValue = String.valueOf(minValue);
        i = (int) Math.pow(10,stringMinValue.substring(1).length()-1);
        return i;
    }
}