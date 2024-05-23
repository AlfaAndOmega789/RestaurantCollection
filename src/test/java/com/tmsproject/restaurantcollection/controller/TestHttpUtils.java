package com.tmsproject.restaurantcollection.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public class TestHttpUtils {

    @Nullable
    public static Long getId(String location) {
        String[] parts = StringUtils.split(location, "/");
        return parts.length > 1 ? Long.valueOf(parts[parts.length - 1]) : null;
    }

}
