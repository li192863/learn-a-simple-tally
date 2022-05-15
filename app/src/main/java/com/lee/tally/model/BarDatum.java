package com.lee.tally.model;

public class BarDatum {
    private int year, month, day;
    private float money;

    public BarDatum() {
    }

    public BarDatum(int year, int month, int day, float money) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.money = money;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "BarData{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", money=" + money +
                '}';
    }
}
