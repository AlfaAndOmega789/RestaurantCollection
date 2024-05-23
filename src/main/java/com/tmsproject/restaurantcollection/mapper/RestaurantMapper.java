package com.tmsproject.restaurantcollection.mapper;

import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.dto.RestaurantDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RestaurantMapper implements EntityMapper<RestaurantEntity, RestaurantDto> {

    public RestaurantMapper() {
    }

    @Override
    public RestaurantDto toDto(RestaurantEntity entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .estimatedCost(entity.getEstimatedCost())
                .averageRating(String.valueOf(entity.getAverageRating()))
                .votes(entity.getVotes())
                .build();
    }

    @Override
    public RestaurantEntity fromDto(RestaurantDto dto) {
        if (dto.getAverageRating() != null && !NumberUtils.isCreatable(dto.getAverageRating())) {
            throw new IllegalArgumentException("Parameter 'averageRating' should be Decimal number format.");
        }
        return RestaurantEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .city(dto.getCity())
                .estimatedCost(dto.getEstimatedCost())
                .averageRating(dto.getAverageRating() == null ? null : new BigDecimal(dto.getAverageRating()))
                .votes(dto.getVotes())
                .build();
    }

}

