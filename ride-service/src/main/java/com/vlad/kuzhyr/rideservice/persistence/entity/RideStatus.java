package com.vlad.kuzhyr.rideservice.persistence.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum RideStatus {
  CREATED(1),
  ON_THE_WAY(2),
  ACCEPTED(3),
  CANCELLED(4),
  COMPLETED(5);

  private final int code;

  private static final Map<Integer, RideStatus> CODE_MAP = new HashMap<>();

  static {
    for (RideStatus rideStatus : RideStatus.values()) {
      CODE_MAP.put(rideStatus.code, rideStatus);
    }
  }

  RideStatus(int code) {
    this.code = code;
  }

  public static RideStatus fromCode(int code) {
    return CODE_MAP.getOrDefault(code, CREATED);
  }

}
