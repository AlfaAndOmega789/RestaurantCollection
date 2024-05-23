package com.tmsproject.restaurantcollection.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDescription {
    private final ErrorSeverity severity;
    private final ErrorCode code;
    private final String property;
    private final String message;
}
