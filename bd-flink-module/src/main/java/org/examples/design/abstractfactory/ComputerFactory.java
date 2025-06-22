package org.examples.design.abstractfactory;

import org.examples.design.model.Computer;
import org.examples.design.model.ComputerSpecification;

public abstract class ComputerFactory implements ComputerAbstractFactory {

    protected String FACTORY_TYPE;

    public ComputerFactory(String type) {
        this.FACTORY_TYPE = type;
    }

    public String getFactoryType(){
        return this.FACTORY_TYPE;
    }

}
