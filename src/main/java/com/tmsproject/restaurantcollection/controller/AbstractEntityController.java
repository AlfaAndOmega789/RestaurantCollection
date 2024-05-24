package com.tmsproject.restaurantcollection.controller;

import com.tmsproject.restaurantcollection.domain.BaseEntity;
import com.tmsproject.restaurantcollection.dto.BaseDto;
import com.tmsproject.restaurantcollection.dto.NewEntityDto;
import com.tmsproject.restaurantcollection.mapper.EntityMapper;
import com.tmsproject.restaurantcollection.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Абстрактный контроллер для управления базовыми сущностями.
 *
 * @param <T> Тип DTO (Data Transfer Object).
 * @param <E> Тип сущности.
 * @param <S> Тип сервиса, управляющего сущностью.
 */
public abstract class AbstractEntityController<T extends BaseDto, E extends BaseEntity, S extends BaseService<E>> implements BaseController<T> {

    // Сервис для управления сущностями
    protected final S service;

    // Маппер для преобразования между сущностями и DTO
    protected final EntityMapper<E, T> mapper;

    /**
     * Конструктор, принимающий сервис и маппер.
     *
     * @param service Сервис для управления сущностями.
     * @param mapper Маппер для преобразования между сущностями и DTO.
     */
    protected AbstractEntityController(S service, EntityMapper<E, T> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Метод для создания новой сущности.
     *
     * @param dto DTO для создания новой сущности.
     * @return Ответ с HTTP статусом и информацией о созданной сущности.
     */
    @Override
    public ResponseEntity<?> create(@Validated @RequestBody T dto) {
        // Преобразование DTO в сущность и сохранение её через сервис
        E entity = service.create(mapper.fromDto(dto));

        // Создание URI для новой сущности
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();

        // Возвращение ответа с созданной сущностью и её URI
        return ResponseEntity.created(location).body(new NewEntityDto(entity.getId()));
    }

    /**
     * Метод для удаления сущности по её ID.
     *
     * @param id ID удаляемой сущности.
     * @return Ответ с HTTP статусом NO_CONTENT.
     */
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Метод для получения списка всех сущностей.
     *
     * @return Список всех DTO.
     */
    @Override
    public List<T> findAll() {
        return service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод для поиска сущности по её ID или выбрасывания исключения, если она не найдена.
     *
     * @param id ID искомой сущности.
     * @return Найденная сущность.
     */
    public E findByIdOrThrowNotFound(Long id) {
        return service.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
