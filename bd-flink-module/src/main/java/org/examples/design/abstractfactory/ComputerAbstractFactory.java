package org.examples.design.abstractfactory;

import org.examples.design.model.Computer;
import org.examples.design.model.ComputerSpecification;

public interface ComputerAbstractFactory {

    public Computer createComputer();
    public ComputerSpecification displaySpecification();

}
