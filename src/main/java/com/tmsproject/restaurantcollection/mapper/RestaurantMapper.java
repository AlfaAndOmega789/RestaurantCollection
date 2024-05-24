package com.tmsproject.restaurantcollection.mapper;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.dto.RestaurantDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Маппер для преобразования между сущностью RestaurantEntity и DTO RestaurantDto.
 */
@Repository
public class RestaurantMapper implements EntityMapper<RestaurantEntity, RestaurantDto> {

    /**
     * Конструктор по умолчанию.
     */
    public RestaurantMapper() {
    }

    /**
     * Преобразует сущность RestaurantEntity в DTO RestaurantDto.
     *
     * @param entity Сущность ресторана.
     * @return DTO ресторана.
     */
    @Override
    public RestaurantDto toDto(RestaurantEntity entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .estimatedCost(entity.getEstimatedCost())
                .averageRating(String.valueOf(entity.getAverageRating())) // Преобразует BigDecimal в строку
                .votes(entity.getVotes())
                .build();
    }

    /**
     * Преобразует DTO RestaurantDto в сущность RestaurantEntity.
     *
     * @param dto DTO ресторана.
     * @return Сущность ресторана.
     * @throws IllegalArgumentException Если значение averageRating не может быть преобразовано в BigDecimal.
     */
    @Override
    public RestaurantEntity fromDto(RestaurantDto dto) {
        // Проверка, является ли значение averageRating числовым
        if (dto.getAverageRating() != null && !NumberUtils.isCreatable(dto.getAverageRating())) {
            throw new IllegalArgumentException("Parameter 'averageRating' should be Decimal number format.");
        }
        return RestaurantEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .city(dto.getCity())
                .estimatedCost(dto.getEstimatedCost())
                .averageRating(dto.getAverageRating() == null ? null : new BigDecimal(dto.getAverageRating())) // Преобразует строку в BigDecimal
                .votes(dto.getVotes())
                .build();
    }
}
