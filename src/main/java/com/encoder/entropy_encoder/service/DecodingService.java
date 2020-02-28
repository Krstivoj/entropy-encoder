package com.encoder.entropy_encoder.service;

import com.encoder.entropy_encoder.arithmetic.ArithmeticDecoder;
import com.encoder.entropy_encoder.arithmetic.Symbol;
import com.encoder.entropy_encoder.payload.EncoderDecoderRequest;
import com.encoder.entropy_encoder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DecodingService {

    @Autowired
    private Utils utils;

    public DecodingService(){
    }

    public String decodeArithmetic(EncoderDecoderRequest encoderDecoderRequest){
        ArrayList<Symbol> symbols = this.utils.createSymbolList(encoderDecoderRequest.getSymbolProbabilityPairs());
        String encodedSequence = encoderDecoderRequest.getSequence();
        Symbol symbol = symbols.get(symbols.size()-1) ;
        ArithmeticDecoder arithmeticDecoder = new ArithmeticDecoder(symbols, encodedSequence, symbol.getHigh());
        return arithmeticDecoder.decode((char)encoderDecoderRequest.getLength());
    }
}
