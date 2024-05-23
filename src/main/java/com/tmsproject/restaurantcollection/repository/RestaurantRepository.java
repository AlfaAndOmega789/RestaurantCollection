package com.tmsproject.restaurantcollection.repository;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<RestaurantEntity, Long> {

    @Override
    List<RestaurantEntity> findAll();
    List<RestaurantEntity> findAllByCityIgnoreCase(String city);
    List<RestaurantEntity> findByOrderByAverageRatingDesc();
    RestaurantEntity findByNameAndCity(String name, String city);

}
