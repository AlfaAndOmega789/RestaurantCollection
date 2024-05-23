package com.tmsproject.restaurantcollection.controller;

import com.tmsproject.restaurantcollection.domain.BaseEntity;
import com.tmsproject.restaurantcollection.dto.BaseDto;
import com.tmsproject.restaurantcollection.dto.NewEntityDto;
import com.tmsproject.restaurantcollection.mapper.EntityMapper;
import com.tmsproject.restaurantcollection.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractEntityController <T extends BaseDto, E extends BaseEntity, S extends BaseService<E>> implements BaseController<T> {

    protected final S service;
    protected final EntityMapper<E, T> mapper;

    protected AbstractEntityController(S service, EntityMapper<E, T> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<?> create(@Valid @RequestBody T dto) {
        E entity = service.create(mapper.fromDto(dto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(location).body(new NewEntityDto(entity.getId()));
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public List<T> findAll() {
        return service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public E findByIdOrThrowNotFound(Long id) {
        return service.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
