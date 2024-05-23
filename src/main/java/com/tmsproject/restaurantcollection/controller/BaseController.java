package com.tmsproject.restaurantcollection.controller;

import com.tmsproject.restaurantcollection.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BaseController<T extends BaseDto> {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource was created.",
                    content = @Content(schema = @Schema(type = "object", implementation = BaseDto.class))),
            @ApiResponse(responseCode = "400", description = "Payload validation failed."),
            @ApiResponse(responseCode = "422", description = "Resource already created.")})
    @PostMapping
    ResponseEntity<?> create(@RequestBody T dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource was deleted."),
            @ApiResponse(responseCode = "404", description = "Resource not found."),
            @ApiResponse(responseCode = "405", description = "Operation is not allowed."),
            @ApiResponse(responseCode = "409", description = "The delete can't be done.")})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @GetMapping
    List<T> findAll();
}