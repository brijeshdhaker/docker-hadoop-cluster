package org.examples.design.model;

public class DesktopSpecification extends ComputerSpecification {


    public DesktopSpecification(String ram, String cpu, String hdd) {
        super(ram, cpu, hdd);
    }

    @Override
    public void display() {
        System.out.println(":: Desktop Configuration ::");
        System.out.println("RAM= "+this.getRAM()+", HDD="+this.getHDD()+", CPU="+this.getCPU());
    }
}
