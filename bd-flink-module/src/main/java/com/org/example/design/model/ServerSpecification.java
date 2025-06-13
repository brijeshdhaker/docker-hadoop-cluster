package com.org.example.design.model;

public class ServerSpecification extends ComputerSpecification {

    public ServerSpecification(String ram, String cpu, String hdd) {
        super(ram, cpu, hdd);
    }

    @Override
    public void display() {
        System.out.println("Server Configuration");
        System.out.println("RAM= "+this.getRAM()+", HDD="+this.getHDD()+", CPU="+this.getCPU());
    }

    /*
    @Override
    public ComputerSpecification setupComputerSpecification() {
        return new ServerSpecification("16GB","8 Core","1 TiB");
    }
    */
}
