package com.encoder.entropy_ecnoder.arithmetic;

public class Interval {

    private double lowRange;
    private double highRange;

    public double getRange(){
        return this.highRange - this.lowRange;
    }

    public double getLowRange() {
        return lowRange;
    }
    public void setLowRange(double lowRange) {
        this.lowRange = lowRange;
    }

    public double getHighRange() {
        return highRange;
    }
    public void setHighRange(double highRange) {
        this.highRange = highRange;
    }

    @Override
    public String toString(){
        return "[ " + String.valueOf(this.lowRange) + " , " + String.valueOf(this.highRange) + " ]" ;
    }
}