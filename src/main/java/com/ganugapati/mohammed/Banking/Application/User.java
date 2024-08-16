package com.ganugapati.mohammed.Banking.Application;

public class User {
    private int id;
    private String name;
    public User(String name, int id) {

        this.id = id;
        this.name = name;
    }
    public User(){

    }
    public int getId() {
        return this.id;
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
}
