package com.vlad.kuzhyr.driverservice.utility.mapper;

import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Gender gender) {
    return gender.getCode();
  }

  @Override
  public Gender convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Gender.fromCode(code);
  }
}
