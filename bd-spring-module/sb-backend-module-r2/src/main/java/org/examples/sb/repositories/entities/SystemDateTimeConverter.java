/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.repositories.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author brijeshdhaker
 */
@Converter(autoApply = true)
public class SystemDateTimeConverter implements AttributeConverter<Long, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Long x) {
        return x == null ? null : new Timestamp(x);
    }

    @Override
    public Long convertToEntityAttribute(Timestamp y) {
        return y == null ? null : y.getTime();
    }

}
