package com.encoder.entropy_encoder.model;

import com.encoder.entropy_encoder.model.audit.DateAudit;
import com.encoder.entropy_encoder.utils.ConverterHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Entity
@Table(name = "encode")
@DiscriminatorColumn(name="encoder_type",discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Encoder extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String plainSequence;

    @NotBlank
    private String encodedSequence;

    @NotNull
    private double entropy ;

    @NotEmpty
    @Convert(converter = ConverterHelper.class)
    private Map<String,Double> symbolProbabilityPairs ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private int encodedSequenceLength ;

    @Column(name = "encoder_type", insertable = false, updatable = false)
    private String encoderType;

    public Encoder(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlainSequence() {
        return plainSequence;
    }

    public void setPlainSequence(String plainSequence) {
        this.plainSequence = plainSequence;
    }

    public String getEncodedSequence() {
        return encodedSequence;
    }

    public void setEncodedSequence(String encodedSequence) {
        this.encodedSequence = encodedSequence;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public Map<String,Double> getSymbolProbabilityPairs() {
        return symbolProbabilityPairs;
    }

    public void setSymbolProbabilityPairs(Map<String,Double> symbolProbabilityPairs) {
        this.symbolProbabilityPairs = symbolProbabilityPairs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getEncodedSequenceLength() {
        return encodedSequenceLength;
    }

    public void setEncodedSequenceLength(int encodedSequenceLength) {
        this.encodedSequenceLength = encodedSequenceLength;
    }

    public String getEncoderType() {
        return encoderType;
    }

    public void setEncoderType(String encoderType) {
        this.encoderType = encoderType;
    }
}