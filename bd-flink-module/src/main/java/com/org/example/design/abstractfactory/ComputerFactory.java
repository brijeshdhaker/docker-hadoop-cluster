package com.org.example.design.abstractfactory;

import com.org.example.design.model.Computer;
import com.org.example.design.model.ComputerSpecification;

public abstract class ComputerFactory implements ComputerAbstractFactory {

    protected String FACTORY_TYPE;

    public ComputerFactory(String type) {
        this.FACTORY_TYPE = type;
    }

    public String getFactoryType(){
        return this.FACTORY_TYPE;
    }

}
