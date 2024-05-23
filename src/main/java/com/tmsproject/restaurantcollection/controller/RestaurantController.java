package com.tmsproject.restaurantcollection.controller;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.dto.BaseDto;
import com.tmsproject.restaurantcollection.dto.NewEntityDto;
import com.tmsproject.restaurantcollection.dto.RestaurantDto;
import com.tmsproject.restaurantcollection.dto.RestaurantShortDto;
import com.tmsproject.restaurantcollection.mapper.RestaurantMapper;
import com.tmsproject.restaurantcollection.service.RestaurantService;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Restaurant Info API", description = "Restaurant Info API.")
@RestController
@RequestMapping("/restaurant")
public class RestaurantController extends AbstractEntityController<RestaurantDto, RestaurantEntity, RestaurantService> {

    protected RestaurantController(RestaurantService service, RestaurantMapper mapper) {
        super(service, mapper);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated.",
                    content = @Content(schema = @Schema(type = "object", implementation = BaseDto.class))),
            @ApiResponse(responseCode = "400", description = "Payload validation failed."),
            @ApiResponse(responseCode = "405", description = "Operation is not allowed.")})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Validated @RequestBody RestaurantShortDto shortDto) {
        RestaurantDto restaurantDto = RestaurantDto.builder()
                .id(id)
                .averageRating(shortDto.getAverageRating())
                .votes(shortDto.getVotes())
                .build();
        RestaurantEntity entity = mapper.fromDto(restaurantDto);
        entity = service.update(entity);

        if (id == null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(entity.getId()).toUri();
            return ResponseEntity.created(location).body(new NewEntityDto(entity.getId()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new NewEntityDto(entity.getId()));
    }


    @GetMapping(path = "/query")
    public ResponseEntity<?> filterByCity(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "city", required = false) String city) {
        if (id != null) {
            return ResponseEntity.ok()
                    .body(mapper.toDto(findByIdOrThrowNotFound(id)));
        }
        if (StringUtils.isNotBlank(city)) {
            return ResponseEntity.ok()
                    .body(service.findAllByCity(city).stream()
                            .map(mapper::toDto)
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.ok()
                .body(Collections.emptyList());
    }

    @GetMapping(path = "/sort")
    public List<RestaurantDto> findByRatingSort() {
        return service.findAllOrderByAverageRating().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
