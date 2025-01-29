package com.vlad.kuzhyr.driverservice.persistence.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Gender {

  MALE(1),
  FEMALE(2),
  OTHER(3),
  UNKNOWN(4);

  private final int code;

  private static final Map<Integer, Gender> CODE_MAP = new HashMap<>();

  static {
    for (Gender gender : Gender.values()) {
      CODE_MAP.put(gender.code, gender);
    }
  }

  Gender(Integer code) {
    this.code = code;
  }

  public static Gender fromCode(int code) {
    return CODE_MAP.getOrDefault(code, UNKNOWN);
  }

}
