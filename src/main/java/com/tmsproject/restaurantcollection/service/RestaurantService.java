package com.tmsproject.restaurantcollection.service;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.exception.DuplicateEntityException;
import com.tmsproject.restaurantcollection.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RestaurantService implements BaseService<RestaurantEntity> {

    private final RestaurantRepository repository;

    protected RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Optional<RestaurantEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public List<RestaurantEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<RestaurantEntity> findAllByCity(String city) {
        return repository.findAllByCityIgnoreCase(city.toLowerCase());
    }

    @Transactional
    public List<RestaurantEntity> findAllOrderByAverageRating() {
        return repository.findByOrderByAverageRatingDesc();
    }

    @Override
    @Transactional
    public RestaurantEntity create(RestaurantEntity entity) {
        log.trace("Create Entity.; class: {}", entity.getClass());
        ensureUniqueOrThrow(entity);
        RestaurantEntity restaurant = repository.save(entity);
        log.info("Entity created.; id: {}; class: {}", restaurant.getId(), restaurant.getClass());
        return restaurant;
    }

    @Override
    @Transactional
    public RestaurantEntity update(RestaurantEntity input) {
        log.trace("Update Entity.; id: {}; class: {}", input.getId(), input.getClass());
        Optional<RestaurantEntity> entityOpt = input.getId() == null ? Optional.empty() : findById(input.getId());
        if (entityOpt.isEmpty()) {
            return create(input);
        }
        RestaurantEntity entity = entityOpt.get();
        if (input.getAverageRating() != null) {
            entity.setAverageRating(input.getAverageRating());
        }
        if (input.getVotes() != null) {
            entity.setVotes(input.getVotes());
        }
        log.info("Entity updated.; id: {}; class: {}", entity.getId(), entity.getClass());
        return entity;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("Delete entity by id.; id: {}", id);
        delete(
                findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(String.format("Entity with id '%s' can't be found.", id)))
        );
    }

    @Override
    @Transactional
    public void delete(RestaurantEntity entity) {
        log.trace("Delete entity.; id: {}", entity.getId());
        repository.delete(entity);
        log.info("Entity deleted.; id: {}; class: {}", entity.getId(), entity.getClass());
    }

    protected void ensureUniqueOrThrow(RestaurantEntity input) {
        RestaurantEntity existing = repository.findByNameAndCity(input.getName(), input.getCity());
        if (existing != null) {
            throw new DuplicateEntityException(
                    String.format("Restaurant already exist wtih name %s and city %s", input.getName(), input.getCity())
            );
        }
    }
}