package com.org.example.design.model;

public abstract class Computer {

    protected String TYPE;

    public Computer(String type) {
        this.TYPE = type;
    }

    public String getType(){
        return this.TYPE;
    }

    public abstract void assemble();


    @Override
    public String toString(){
        return "Computer = "+this.getType();
    }

}
