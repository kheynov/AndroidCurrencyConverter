package com.example.currencyconverter;

public class CurrencyListItem {
    private String fullName;
    private String shortName;
    private int nominal;
    private double value;

    public CurrencyListItem(String fullName, String shortName, int nominal, double value) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.nominal = nominal;
        this.value = value;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public int getNominal() {
        return nominal;
    }

    public double getValue() {
        return value;
    }

    public String getPrice() {
        return nominal + " " + shortName + " = " + value + " RUB";
    }
}
