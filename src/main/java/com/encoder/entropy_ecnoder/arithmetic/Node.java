package com.encoder.entropy_ecnoder.arithmetic;

import java.util.Objects;

public class Node {

    private double high;
    private double low;
    private NodeProperty nodeProperty;

    public Node() {
        super();
        nodeProperty = new NodeProperty();
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public NodeProperty getNodeProperty() {
        return nodeProperty;
    }

    public void setNodeProperty(NodeProperty nodeProperty) {
        this.nodeProperty = nodeProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Double.compare(node.getHigh(), getHigh()) == 0 &&
                Double.compare(node.getLow(), getLow()) == 0 &&
                Objects.equals(getNodeProperty(), node.getNodeProperty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHigh(), getLow(), getNodeProperty());
    }
}
