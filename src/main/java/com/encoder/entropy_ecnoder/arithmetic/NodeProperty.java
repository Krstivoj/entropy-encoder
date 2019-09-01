package com.encoder.entropy_ecnoder.arithmetic;

public class NodeProperty implements Comparable<NodeProperty> {

    private String symbol;
    private Double probability;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    @Override
    public int compareTo(NodeProperty o) {
        return this.getSymbol().compareToIgnoreCase(o.getSymbol());
    }
}
