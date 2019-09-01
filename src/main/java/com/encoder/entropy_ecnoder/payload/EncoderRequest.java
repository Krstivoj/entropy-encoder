package com.encoder.entropy_ecnoder.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

public class EncoderRequest {

    @NotBlank
    private String plainSequence ;

    @NotEmpty
    private Map<String,Double> symbolProbabilityPairs;

    public String getPlainSequence() {
        return plainSequence;
    }

    public void setPlainSequence(String plainSequence) {
        this.plainSequence = plainSequence;
    }

    public Map<String, Double> getSymbolProbabilityPairs() {
        return symbolProbabilityPairs;
    }

    public void setSymbolProbabilityPairs(Map<String, Double> symbolProbabilityPairs) {
        this.symbolProbabilityPairs = symbolProbabilityPairs;
    }
}
