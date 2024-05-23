package com.tmsproject.restaurantcollection.mapper;

public interface EntityMapper<E, T> {
    T toDto(E entity);

    E fromDto(T dto);
}
