package com.example.shoppingapp.Model;

public class Favorites
{
    private String pname,price,pid,discount;

    public Favorites() {
    }

    public Favorites(String pname, String price, String pid, String discount) {
        this.pname = pname;
        this.price = price;
        this.pid = pid;
        this.discount = discount;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
