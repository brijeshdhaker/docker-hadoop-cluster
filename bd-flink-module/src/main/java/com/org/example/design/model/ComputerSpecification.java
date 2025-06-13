package com.org.example.design.model;

public abstract class ComputerSpecification {

    protected String ram;
    protected String hdd;
    protected String cpu;

    public ComputerSpecification(String ram, String cpu, String hdd) {
        this.ram = ram;
        this.cpu = cpu;
        this.hdd = hdd;
    }

    public String getRAM(){
        return this.ram;
    }
    public String getHDD(){
        return this.hdd;
    }
    public String getCPU(){
        return this.cpu;
    }

    public abstract void display();
}
