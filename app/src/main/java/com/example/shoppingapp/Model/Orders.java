package com.example.shoppingapp.Model;

public class Orders
{
    private String date, number, time, totalAmount;

    public Orders() {
    }

    public Orders(String date, String number, String time, String totalAmount) {
        this.date = date;
        this.number = number;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
