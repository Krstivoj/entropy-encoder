package com.encoder.entropy_encoder.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

public class EncoderDecoderRequest {

    @NotBlank
    private String sequence;

    @NotEmpty
    private Map<String,Double> symbolProbabilityPairs;

    private int length;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Map<String, Double> getSymbolProbabilityPairs() {
        return symbolProbabilityPairs;
    }

    public void setSymbolProbabilityPairs(Map<String, Double> symbolProbabilityPairs) {
        this.symbolProbabilityPairs = symbolProbabilityPairs;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
