package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RideStatusConverter implements AttributeConverter<RideStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(RideStatus rideStatus) {
    if (rideStatus == null) {
      return null;
    }

    return rideStatus.getCode();
  }

  @Override
  public RideStatus convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return RideStatus.fromCode(code);
  }
}
