package com.example.cafepaymentreader;

public class CafeMenu{
    private String name;
    private int value;
    private int count = 0;

    public CafeMenu(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return this.name; }

    public int getValue() { return this.value; }

    public int getCount() { return this.count; }

    public void setCount(int count) { this.count = count; }

    public void countUp () { this.count++; }
}
