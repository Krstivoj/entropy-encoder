package com.encoder.entropy_ecnoder.arithmetic;

public class Symbol {
    private char symbol ;
    private char low ;
    private char high ;

    public Symbol(char symbol , char low , char high){
        this.high = high ;
        this.low = low ;
        this.symbol = symbol ;
    }
    public Symbol(){
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getLow() {
        return low;
    }

    public void setLow(char low) {
        this.low = low;
    }

    public char getHigh() {
        return high;
    }

    public void setHigh(char high) {
        this.high = high;
    }
}
