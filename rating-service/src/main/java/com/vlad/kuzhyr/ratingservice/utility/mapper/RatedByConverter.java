package com.vlad.kuzhyr.ratingservice.utility.mapper;

import com.vlad.kuzhyr.ratingservice.exception.RatedByNullException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RatedByConverter implements AttributeConverter<RatedBy, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RatedBy ratedBy) {
        if (ratedBy == null) {
            throw new RatedByNullException(ExceptionMessageConstant.RATED_BY_CAN_NOT_NULL_MESSAGE);
        }

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
