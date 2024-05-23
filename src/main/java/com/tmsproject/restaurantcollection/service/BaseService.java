package com.tmsproject.restaurantcollection.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    T create(T t);
    T update(T t);
    void delete(Long id);
    void delete(T entity);

}
