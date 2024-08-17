package com.ganugapati.mohammed.Banking.Application;

public class User {
    private int id;
    private String name;
    private double balance;
    public User(String name, int id) {

        this.id = id;
        this.name = name;
        this.balance = 0;
    }
    public User(){

    }

    public int getId()
    {
        return this.id;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
