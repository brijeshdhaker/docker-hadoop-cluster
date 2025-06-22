package org.examples.design.model;

public class Server extends Computer {

    public Server() {
        super("SERVER");
    }

    @Override
    public void assemble() {
        System.out.println("Assembling Server .");
    }
}