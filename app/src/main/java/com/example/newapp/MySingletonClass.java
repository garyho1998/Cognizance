package com.example.newapp;

public class MySingletonClass {

    private static MySingletonClass instance;

    public static MySingletonClass getInstance() {
        if (instance == null)
            instance = new MySingletonClass();
        return instance;
    }

    private MySingletonClass() {
    }

    private String username;

    public String getValue() {
        return username;
    }

    public void setValue(String value) {
        this.username = value;
    }
}