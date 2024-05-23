package com.tmsproject.restaurantcollection.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID("invalid"),
    CONFLICT("conflict"),
    DUPLICATE("duplicate"),
    NOT_SUPPORTED("not-supported"),
    EXCEPTION("exception");

    private final String code;
}
