package com.vlad.kuzhyr.ratingservice.utility.mapper;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RatedByConverter implements AttributeConverter<RatedBy, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RatedBy ratedBy) {
        return ratedBy.getCode();
    }

    @Override
    public RatedBy convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return RatedBy.fromCode(code);
    }

}
