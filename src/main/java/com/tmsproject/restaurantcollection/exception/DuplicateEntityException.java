package com.tmsproject.restaurantcollection.exception;

public class DuplicateEntityException extends RuntimeException{
    public DuplicateEntityException(String message) {
        super(message);
    }

}
