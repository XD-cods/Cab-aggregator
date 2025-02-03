package com.vlad.kuzhyr.rideservice.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum RideStatus {
  CREATED(1),
  WAITING_FOR_DRIVER(2),
  DRIVER_ASSIGNED(3),
  ACCEPTED(4),
  ON_THE_WAY_TO_PASSENGER(5),
  PASSENGER_PICKED_UP(6),
  ON_THE_WAY(7),
  COMPLETED(8),
  PAYMENT_PENDING(9),
  PAID(10),
  CANCELLED(11);

  private static final Map<Integer, RideStatus> CODE_MAP = new HashMap<>();

  static {
    for (RideStatus rideStatus : RideStatus.values()) {
      CODE_MAP.put(rideStatus.code, rideStatus);
    }
  }

  private final int code;

  public static RideStatus fromCode(int code) {
    return CODE_MAP.getOrDefault(code, CREATED);
  }

  public boolean isTransitionAllowed(RideStatus newStatus) {
    return switch (this) {
      case CREATED -> newStatus == WAITING_FOR_DRIVER || newStatus == CANCELLED;
      case WAITING_FOR_DRIVER -> newStatus == DRIVER_ASSIGNED || newStatus == CANCELLED;
      case DRIVER_ASSIGNED -> newStatus == ACCEPTED || newStatus == CANCELLED;
      case ACCEPTED -> newStatus == ON_THE_WAY_TO_PASSENGER || newStatus == CANCELLED;
      case ON_THE_WAY_TO_PASSENGER -> newStatus == PASSENGER_PICKED_UP || newStatus == CANCELLED;
      case PASSENGER_PICKED_UP -> newStatus == ON_THE_WAY || newStatus == CANCELLED;
      case ON_THE_WAY -> newStatus == COMPLETED || newStatus == CANCELLED;
      case COMPLETED -> newStatus == PAYMENT_PENDING;
      case PAYMENT_PENDING -> newStatus == PAID;
      case PAID, CANCELLED -> false;
    };
  }

  public boolean isUpdatable() {
    return this == CREATED || this == WAITING_FOR_DRIVER || this == DRIVER_ASSIGNED;
  }

}
