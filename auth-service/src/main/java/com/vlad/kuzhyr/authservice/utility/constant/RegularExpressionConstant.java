package com.vlad.kuzhyr.authservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegularExpressionConstant {

    public static final String PHONE_REG_XP = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$";

}
