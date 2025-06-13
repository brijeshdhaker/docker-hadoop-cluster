package com.org.example.design.model;

public class Server extends Computer {

    public Server() {
        super("SERVER");
    }

    @Override
    public void assemble() {
        System.out.println("Assembling Server .");
    }
}