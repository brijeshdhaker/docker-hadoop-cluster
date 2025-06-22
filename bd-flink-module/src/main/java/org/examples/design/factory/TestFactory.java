package org.examples.design.factory;

import org.examples.design.model.Computer;

public class TestFactory {

    public static void main(String[] args) {

        Computer pc = ComputerFactory.getComputer("Desktop");
        System.out.println("Factory PC Config :: " + pc);

        Computer server = ComputerFactory.getComputer("Server");
        System.out.println("Factory Server Config :: " + server);

    }
}
