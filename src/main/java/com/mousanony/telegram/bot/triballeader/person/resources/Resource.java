package com.mousanony.telegram.bot.triballeader.person.resources;

/**
 * @author mousanonyad
 */
public class Resource {
    private int positiveValue;

    public Resource(int positiveValue) {
        this.positiveValue = positiveValue;
    }

    public void increaseWithPercent(int percent) {
        this.positiveValue += (Math.ceil((double) percent / 100 * positiveValue));
    }

    public void decreaseWithPercent(int percent) {
        this.positiveValue -= (Math.ceil((double) percent / 100 * positiveValue));
    }

    public int increase(int count) {
        return positiveValue = positiveValue + count;
    }

    public int decrease(int count) {
        return positiveValue = positiveValue - count;
    }


    public int getPositiveValue() {
        if (positiveValue < 0)
            positiveValue = 0;
        return positiveValue;
    }

    public void setPositiveValue(int positiveValue) {
        this.positiveValue = positiveValue;
    }

    public int getPercent(int count) {
        return positiveValue / 100 * count;
    }
}
