package com.example.administrator.analysisxml;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class Book {
    private int id;
    private String name;
    private float price;

    public Book() {
    }

    public Book(float price, int id, String name) {
        this.price = price;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String toString(){
        return "id:"+id+", name:"+name+", price:"+price;
    }
}
