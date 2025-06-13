package com.org.example.design.abstractfactory;

import com.org.example.design.model.Computer;
import com.org.example.design.model.ComputerSpecification;

public interface ComputerAbstractFactory {

    public Computer createComputer();
    public ComputerSpecification displaySpecification();

}
