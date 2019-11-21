package com.encoder.entropy_ecnoder.utils;

import com.encoder.entropy_ecnoder.arithmetic.ArithmeticEncoder;
import com.encoder.entropy_ecnoder.arithmetic.Symbol;
import com.encoder.entropy_ecnoder.huffman.HuffmanCoding;
import com.encoder.entropy_ecnoder.model.Arithmetic;
import com.encoder.entropy_ecnoder.model.Huffman;
import com.encoder.entropy_ecnoder.model.User;
import com.encoder.entropy_ecnoder.payload.EncoderRequest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class Utils {

    public static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    public static double getEntropy(Map<String, Double> charProbabilityPairs) {
        final Double[] retVal = {0.0};
        charProbabilityPairs.forEach((symbol, probability) -> {
            retVal[0] += (probability * log2(probability));
        });
        return (-1) * retVal[0];
    }

    private static String getEntropyAsString(Map<String, Double> charProbabilityPairs) {
        return getFormattedDouble(getEntropy(charProbabilityPairs), 4);
    }

    public static double getAverageCodeLength(Map<String, String> codeTable, Map<String, Double> symbolProbabilityPairs) {
        final Double[] averageCodeLength = {0.0};
        for (Map.Entry<String, Double> entry : symbolProbabilityPairs.entrySet()) {
            String symbol = entry.getKey();
            Double probability = entry.getValue();
            averageCodeLength[0] += probability * codeTable.get(symbol).length();
        }
        return averageCodeLength[0];
    }

    public static String getFormattedDouble(double number, int fractionDigits) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(fractionDigits);
        return decimalFormat.format(number);
    }

    public static double convertToDecimal(String binaryNumber) {

        double decimalNumber;

        if (binaryNumber.contains(".")) {
            String[] digits = binaryNumber.split("\\.");

            int beforeDecimalDot = Integer.parseInt(digits[0], 2);
            double afterDecimalDot = latterPart(digits[1]);
            decimalNumber = beforeDecimalDot + afterDecimalDot;

        } else {
            decimalNumber = Integer.parseInt(binaryNumber, 2);
        }
        return decimalNumber;
    }

    private static double latterPart(String number) {

        double sum = 0;
        int length = number.length();

        for (int i = 0; i < length; i++) {
            int e = -i - 1;
            char multiplication = number.charAt(i);
            int num = Integer.parseInt(String.valueOf(multiplication));
            double num1 = num * Math.pow(2, e);

            sum = sum + num1;
        }
        return sum;
    }

    public static Huffman generateHuffmanModel(User user, EncoderRequest encoderRequest) {
        Huffman huffman = new Huffman();

        HuffmanCoding huffmanCoding = new HuffmanCoding();

        Map<String, String> codeTable = huffmanCoding.createHuffmanCodes(
                huffmanCoding.buildTree(encoderRequest.getSymbolProbabilityPairs()));

        double averageCodeLength = getAverageCodeLength(codeTable, encoderRequest.getSymbolProbabilityPairs());

        String encodedSequence = "";

        for (char letter : encoderRequest.getPlainSequence().toCharArray())
            encodedSequence += codeTable.get(String.valueOf(letter));

        final String entropy = getEntropyAsString(encoderRequest.getSymbolProbabilityPairs());

        huffman.setEfficient(getFormattedDouble(getEntropy(encoderRequest.getSymbolProbabilityPairs()) / averageCodeLength, 4));
        huffman.setEncodedSequence(encodedSequence);
        huffman.setEntropy(Double.parseDouble(entropy));
        huffman.setSufficient(getFormattedDouble(1 - getEntropy(encoderRequest.getSymbolProbabilityPairs()) / averageCodeLength, 4));
        huffman.setPlainSequence(encoderRequest.getPlainSequence());
        huffman.setUser(user);
        huffman.setSymbolCodePairs(codeTable);
        huffman.setSymbolProbabilityPairs(encoderRequest.getSymbolProbabilityPairs());
        huffman.setAverageCodeLength((int) Math.round(averageCodeLength));
        huffman.setEncodedSequenceLength(encodedSequence.length());
        huffman.setEncoderType("huffman");

        return huffman;
    }

    public static Arithmetic generateArithmeticModel(User user, EncoderRequest encoderRequest) {
        Arithmetic arithmetic = new Arithmetic();

        Map<String, Double> codeTable = encoderRequest.getSymbolProbabilityPairs();

        ArithmeticEncoder arithmeticEncoder = new ArithmeticEncoder(createSymbolList(codeTable),(char)findProbabilityFactor(codeTable));
        String encodedSequence = arithmeticEncoder.getCompressedAsBinary(encoderRequest.getPlainSequence()) ;
        int encodedSequenceLength = encodedSequence.length();

        final String entropy = getEntropyAsString(encoderRequest.getSymbolProbabilityPairs());

        arithmetic.setEncodedSequence(encodedSequence);
        arithmetic.setEntropy(Double.parseDouble(entropy));
        arithmetic.setPlainSequence(encoderRequest.getPlainSequence());
        arithmetic.setUser(user);
        arithmetic.setSymbolProbabilityPairs(encoderRequest.getSymbolProbabilityPairs());
        arithmetic.setEncodedSequenceLength(encodedSequenceLength);
        arithmetic.setEncoderType("arithmetic");

        return arithmetic;
    }

    public static int isEncoderRequestValid(EncoderRequest encoderRequest) {
        final BigDecimal[] step = {new BigDecimal("0.0")};
        final Integer[] maxFractionDigits = {0};
        encoderRequest.getSymbolProbabilityPairs().forEach((symbol, probability) -> {
            if (String.valueOf(probability).substring(String.valueOf(probability).lastIndexOf('.')).length() > maxFractionDigits[0])
                maxFractionDigits[0] = String.valueOf(probability).substring(String.valueOf(probability).lastIndexOf('.')).length();
            step[0] = step[0].add(new BigDecimal(probability));
        });
        step[0] = new BigDecimal(getFormattedDouble(step[0].doubleValue(),maxFractionDigits[0]));
        return step[0].compareTo(BigDecimal.ONE);
    }

    public static ArrayList<Symbol> createSymbolList(Map<String, Double> codeTable) {
        final ArrayList<Symbol> symbols = new ArrayList<>() ;
        int coeficient = findProbabilityFactor(codeTable);
        int step = 0 ;
        int stepValue ;
        for (Map.Entry<String, Double> stringDoubleEntry : codeTable.entrySet()) {
            stepValue = step + (int)(stringDoubleEntry.getValue()*coeficient) ;
            symbols.add(new Symbol(stringDoubleEntry.getKey().charAt(0),(char)step,(char)stepValue));
            step = stepValue ;
        }
        return symbols;
    }

    public static int findProbabilityFactor (Map<String, Double> codeTable) {
        int i ;
        double minValue = 1;
        for (Map.Entry<String, Double> stringDoubleEntry : codeTable.entrySet()) {
            if (stringDoubleEntry.getValue() < minValue) {
                minValue = stringDoubleEntry.getValue();
            }
        }
        String stringMinValue = String.valueOf(minValue);
        i = (int) Math.pow(10,stringMinValue.substring(1).length());
        return i;
    }
}