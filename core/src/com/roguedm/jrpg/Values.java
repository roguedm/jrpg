package com.roguedm.jrpg;

public class Values {

    private int min;
    private int max;

    public Values() {
        this.set(0, 0);
    }

    public Values(Values values) {
        if (values != null) {
            this.set(values.min, values.max);
        }
    }

    public Values(int min, int max) {
        this.set(min, max);
    }

    public Values(int value) {
        this(value, value);
    }

    public void set(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}
