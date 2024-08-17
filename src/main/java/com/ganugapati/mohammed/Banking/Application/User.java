package com.ganugapati.mohammed.Banking.Application;

public class User {
    private int id;
    private String name;
    private double checkingBalance;
    private double savingBalance;
    public User(String name, int id) {

        this.id = id;
        this.name = name;
        this.checkingBalance = 0.0;
        this.savingBalance = 0.0;
    }
    public User(){

    }

    public int getId()
    {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getCheckingBalance() {
        return this.checkingBalance;
    }
    public double getSavingBalance() {
        return this.savingBalance;
    }
    public void setCheckingBalance(double checkingBalance) {
        this.checkingBalance = checkingBalance;
    }
    public void setSavingBalance(double savingBalance) {
        this.savingBalance = savingBalance;
    }
    public void depositCheckingBalance(double checkingBalance) {
        this.checkingBalance += checkingBalance;
    }

    public void depositSavingBalance(double savingBalance) {
        this.savingBalance += savingBalance;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
