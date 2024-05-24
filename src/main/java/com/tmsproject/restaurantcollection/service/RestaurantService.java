package com.tmsproject.restaurantcollection.service;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.exception.DuplicateEntityException;
import com.tmsproject.restaurantcollection.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления сущностями ресторанов.
 */
@Slf4j
@Service
public class RestaurantService implements BaseService<RestaurantEntity> {

    private final RestaurantRepository repository;

    /**
     * Конструктор, принимающий репозиторий ресторанов.
     *
     * @param repository Репозиторий для управления сущностями ресторанов.
     */
    protected RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    /**
     * Находит ресторан по его ID.
     *
     * @param id ID ресторана.
     * @return Optional с сущностью ресторана, если найден.
     */
    @Override
    @Transactional
    public Optional<RestaurantEntity> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Возвращает список всех ресторанов.
     *
     * @return Список всех сущностей ресторанов.
     */
    @Override
    @Transactional
    public List<RestaurantEntity> findAll() {
        return repository.findAll();
    }

    /**
     * Находит все рестораны в указанном городе.
     *
     * @param city Город для фильтрации ресторанов.
     * @return Список сущностей ресторанов в указанном городе.
     */
    @Transactional
    public List<RestaurantEntity> findAllByCity(String city) {
        return repository.findAllByCityIgnoreCase(city.toLowerCase());
    }

    /**
     * Возвращает список ресторанов, отсортированных по среднему рейтингу.
     *
     * @return Список сущностей ресторанов, отсортированных по среднему рейтингу.
     */
    @Transactional
    public List<RestaurantEntity> findAllOrderByAverageRating() {
        return repository.findByOrderByAverageRatingDesc();
    }

    /**
     * Создает новый ресторан.
     *
     * @param entity Сущность ресторана для создания.
     * @return Созданная сущность ресторана.
     */
    @Override
    @Transactional
    public RestaurantEntity create(RestaurantEntity entity) {
        log.trace("Create Entity.; class: {}", entity.getClass());
        ensureUniqueOrThrow(entity); // Проверка уникальности ресторана
        RestaurantEntity restaurant = repository.save(entity); // Сохранение ресторана в репозитории
        log.info("Entity created.; id: {}; class: {}", restaurant.getId(), restaurant.getClass());
        return restaurant;
    }

    /**
     * Обновляет существующий ресторан.
     *
     * @param input Сущность ресторана с обновленными данными.
     * @return Обновленная сущность ресторана.
     */
    @Override
    @Transactional
    public RestaurantEntity update(RestaurantEntity input) {
        log.trace("Update Entity.; id: {}; class: {}", input.getId(), input.getClass());
        Optional<RestaurantEntity> entityOpt = input.getId() == null ? Optional.empty() : findById(input.getId());
        if (entityOpt.isEmpty()) {
            return create(input); // Если ресторан не найден, создать новый
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

    /**
     * Удаляет ресторан по его ID.
     *
     * @param id ID ресторана для удаления.
     */
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

    /**
     * Удаляет указанную сущность ресторана.
     *
     * @param entity Сущность ресторана для удаления.
     */
    @Override
    @Transactional
    public void delete(RestaurantEntity entity) {
        log.trace("Delete entity.; id: {}", entity.getId());
        repository.delete(entity);
        log.info("Entity deleted.; id: {}; class: {}", entity.getId(), entity.getClass());
    }

    /**
     * Проверяет уникальность ресторана по имени и городу.
     *
     * @param input Сущность ресторана для проверки.
     * @throws DuplicateEntityException Если ресторан с таким именем и городом уже существует.
     */
    protected void ensureUniqueOrThrow(RestaurantEntity input) {
        RestaurantEntity existing = repository.findByNameAndCity(input.getName(), input.getCity());
        if (existing != null) {
            throw new DuplicateEntityException(
                    String.format("Restaurant already exist with name %s and city %s", input.getName(), input.getCity())
            );
        }
    }
}
