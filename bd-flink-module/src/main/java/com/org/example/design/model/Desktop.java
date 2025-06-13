package com.org.example.design.model;

public class Desktop extends Computer {

    public Desktop() {
        super("DESKTOP");
    }

    @Override
    public void assemble() {
        System.out.println("Assembling Desktop Computer.");
    }

}
