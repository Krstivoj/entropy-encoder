package com.encoder.entropy_ecnoder.model;

import com.encoder.entropy_ecnoder.utils.ConverterHelper;

import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Map;

@Entity
@DiscriminatorValue("huffman_encoder")
public class Huffman extends Encoder {

    @NotBlank
    @Size(max = 10)
    private String sufficient;

    @NotBlank
    @Size(max = 10)
    private String efficient;

    private int averageCodeLength;

    @NotEmpty
    @Convert(converter = ConverterHelper.class)
    private Map<String,String> symbolCodePairs;

    public String getSufficient() {
        return sufficient;
    }

    public void setSufficient(String sufficient) {
        this.sufficient = sufficient;
    }

    public String getEfficient() {
        return efficient;
    }

    public void setEfficient(String efficient) {
        this.efficient = efficient;
    }

    public int getAverageCodeLength() {
        return averageCodeLength;
    }

    public void setAverageCodeLength(int averageCodeLength) {
        this.averageCodeLength = averageCodeLength;
    }

    public Map<String, String> getSymbolCodePairs() {
        return symbolCodePairs;
    }

    public void setSymbolCodePairs(Map<String, String> symbolCodePairs) {
        this.symbolCodePairs = symbolCodePairs;
    }
}
