package com.tmsproject.restaurantcollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantShortDto {
    @Schema(title = "Average rating of the restaurant.")
    @NotBlank
    private String averageRating;

    @Schema(title = "Total reviews.")
    @NotBlank
    private Integer votes;
}
