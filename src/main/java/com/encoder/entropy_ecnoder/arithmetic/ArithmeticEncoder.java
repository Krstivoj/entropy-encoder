package com.encoder.entropy_ecnoder.arithmetic;

import com.encoder.entropy_ecnoder.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class ArithmeticEncoder {

    private double accumulative;
    private ArrayList<Node> nodes;
    private ArrayList<NodeProperty> nodeProperties;
    private Interval interval;

    public ArithmeticEncoder(ArrayList<NodeProperty> charactersProbabilities) {
        super();
        this.nodeProperties = charactersProbabilities;
        nodes = new ArrayList<>();
        accumulative = 1.0;
        this.interval = new Interval();
        Collections.sort(nodeProperties);
        init();
    }

    private void init() {
        for (NodeProperty nodeProperty : nodeProperties) {
            Node node = new Node();
            node.setNodeProperty(nodeProperty);
            node.setHigh(accumulative);
            node.setLow(accumulative - nodeProperty.getProbability());
            nodes.add(node);
            accumulative -= nodeProperty.getProbability();
        }
    }

    public String encode(final String plainText) {

        char[] chars = plainText.toCharArray();
        Node node;

        int precision = 0;
        if(chars.length == 1 ){
            node = getNodeByName(String.valueOf(chars[0])) ;
            String str = "" ;
            if (node.getHigh() == 1.0)
                str = "1.0";
            else if(node.getLow() == 0.0)
                str = "0.0" ;
            else str= "" ;

            if (!str.isEmpty())
                return str;
        }

        for (char ch : chars) {
            node = getNodeByName(String.valueOf(ch));
            precision += (int) Math.ceil(-Utils.log2(node.getNodeProperty().getProbability()));
            this.interval.setHighRange(node.getHigh());
            this.interval.setLowRange(node.getLow());

            this.accumulative = this.interval.getHighRange();
            refreshNodesState();
        }

        return generateBinaryString(interval.getLowRange(),interval.getHighRange(), precision);
    }

    private String generateBinaryString(double low,double high,int precision) {

        double delta1 = 0.0;
        double delta2 = 0.0;
        String current_string = "0.";
        String current1 ;
        String current2 ;
        double currentDec1;
        double currentDec2;

        while(current_string.length() <= precision * 2) {

            current1 = current_string + "1";
            current2 = current_string + "0" ;

            currentDec1 = Utils.convertToDecimal(current1);
            currentDec2 = Utils.convertToDecimal(current2);

            delta1 = high - currentDec1 ;
            delta2 = low - currentDec2;

            current_string = (delta1 >= 0 && delta1 < delta2) || (delta2 >= 0 && delta2 < delta1) ? current1 : current2;

        }
        System.out.println("[ " + low + " , " + high + " ]");
        return current_string.substring(0,current_string.lastIndexOf('1')+1);
    }

    private void refreshNodesState() {

        Node prevNode = null;

        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node currentNode = nodes.get(i);
            if (i == nodes.size() - 1)
                currentNode.setLow(this.interval.getLowRange());
            else
                currentNode.setLow(prevNode.getHigh());
            if (i == 0)
                currentNode.setHigh(this.interval.getHighRange());
            else
                currentNode.setHigh(currentNode.getLow() + this.interval.getRange() * currentNode.getNodeProperty().getProbability());
            prevNode = currentNode;

        }
    }

    private Node getNodeByName(String valueOf) {
        Node node = new Node();
        node.getNodeProperty().setSymbol(valueOf);

        for (Node node1 : nodes) {
            if (node1.getNodeProperty().getSymbol().equals(valueOf)) {
                node.setHigh(node1.getHigh());
                node.setLow(node1.getLow());
                node.getNodeProperty().setProbability(node1.getNodeProperty().getProbability());
            }
        }
        return node;
    }
}