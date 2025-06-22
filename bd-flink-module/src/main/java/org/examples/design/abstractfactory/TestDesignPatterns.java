package org.examples.design.abstractfactory;

import org.examples.design.model.Computer;
import org.examples.design.model.ComputerSpecification;

public class TestDesignPatterns {

    public static void main(String[] args) {

        ComputerFactory desktopFactory = testAbstractFactory("Desktop");
        System.out.println("Computer Factory Type : " + desktopFactory.getFactoryType());
        Computer desktop = desktopFactory.createComputer();
        System.out.println("Computer Type : " + desktop.getType());
        desktop.assemble();
        ComputerSpecification desktopSpecification = desktopFactory.displaySpecification();
        desktopSpecification.display();

        //
        ComputerFactory serverFactory = testAbstractFactory("Server");
        System.out.println("Computer Factory Type : " + serverFactory.getFactoryType());
        Computer server = serverFactory.createComputer();
        System.out.println("Computer Type : " + server.getType());
        server.assemble();
        ComputerSpecification serverSpecification = serverFactory.displaySpecification();
        serverSpecification.display();

    }

    private static ComputerFactory testAbstractFactory(String type) {
        ComputerFactory factory = null;
        if("Desktop".equalsIgnoreCase(type)) {
            factory = new DesktopFactory();
        }else if("Server".equalsIgnoreCase(type)){
            factory = new ServerFactory();
        }
        return factory;
    }
}
