package com.vlad.kuzhyr.driverservice.utility.mapper;

import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(Integer code) {
        if (code == null) {
            log.error("Gender converter. Convert to entity. Code is null.");
            return null;
        }

        return Gender.fromCode(code);
    }

}
