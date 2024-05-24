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

/**
 * Контроллер для управления информацией о ресторанах.
 */
@Tag(name = "Restaurant Info API", description = "Restaurant Info API.")
@RestController
@RequestMapping("/restaurant")
public class RestaurantController extends AbstractEntityController<RestaurantDto, RestaurantEntity, RestaurantService> {

    /**
     * Конструктор, принимающий сервис и маппер.
     *
     * @param service Сервис для управления сущностями ресторанов.
     * @param mapper Маппер для преобразования между сущностями и DTO ресторанов.
     */
    protected RestaurantController(RestaurantService service, RestaurantMapper mapper) {
        super(service, mapper);
    }

    /**
     * Обновление информации о ресторане по его ID.
     *
     * @param id ID ресторана.
     * @param shortDto DTO с краткой информацией для обновления.
     * @return Ответ с HTTP статусом и информацией о созданной/обновленной сущности.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated.",
                    content = @Content(schema = @Schema(type = "object", implementation = BaseDto.class))),
            @ApiResponse(responseCode = "400", description = "Payload validation failed."),
            @ApiResponse(responseCode = "405", description = "Operation is not allowed.")})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Validated @RequestBody RestaurantShortDto shortDto) {
        // Создание полного DTO на основе краткого DTO
        RestaurantDto restaurantDto = RestaurantDto.builder()
                .id(id)
                .averageRating(shortDto.getAverageRating())
                .votes(shortDto.getVotes())
                .build();

        // Преобразование DTO в сущность и обновление её через сервис
        RestaurantEntity entity = mapper.fromDto(restaurantDto);
        entity = service.update(entity);

        // Если ID отсутствует, создание нового ресурса
        if (id == null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(entity.getId()).toUri();
            return ResponseEntity.created(location).body(new NewEntityDto(entity.getId()));
        }

        // Возвращение ответа с обновлённой сущностью и её ID
        return ResponseEntity.status(HttpStatus.OK).body(new NewEntityDto(entity.getId()));
    }

    /**
     * Фильтрация ресторанов по городу или ID.
     *
     * @param id ID ресторана (необязательный).
     * @param city Город ресторана (необязательный).
     * @return Ответ с фильтрованным списком ресторанов или пустым списком.
     */
    @GetMapping(path = "/query")
    public ResponseEntity<?> filterByCity(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "city", required = false) String city) {
        // Если указан ID, поиск ресторана по ID
        if (id != null) {
            return ResponseEntity.ok()
                    .body(mapper.toDto(findByIdOrThrowNotFound(id)));
        }

        // Если указан город, фильтрация ресторанов по городу
        if (StringUtils.isNotBlank(city)) {
            return ResponseEntity.ok()
                    .body(service.findAllByCity(city).stream()
                            .map(mapper::toDto)
                            .collect(Collectors.toList()));
        }

        // Если параметры не указаны, возвращение пустого списка
        return ResponseEntity.ok()
                .body(Collections.emptyList());
    }

    /**
     * Поиск ресторанов с сортировкой по рейтингу.
     *
     * @return Список ресторанов, отсортированных по среднему рейтингу.
     */
    @GetMapping(path = "/sort")
    public List<RestaurantDto> findByRatingSort() {
        return service.findAllOrderByAverageRating().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
