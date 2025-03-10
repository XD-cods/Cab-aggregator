package com.vlad.kuzhyr.ratingservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestContainerConstant {

    public static final String POSTGRES_IMAGE = "postgres:15-alpine";
    public static final String POSTGRES_DATABASE_NAME = "testdb";
    public static final String POSTGRES_USERNAME = "postgres";
    public static final String POSTGRES_PASSWORD = "1111";

}
