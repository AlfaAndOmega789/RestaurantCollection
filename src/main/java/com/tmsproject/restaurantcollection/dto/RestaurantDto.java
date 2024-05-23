package com.tmsproject.restaurantcollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotNull;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Schema(title = "Restaurant", description = "Restaurant")
public class RestaurantDto extends BaseDto {

    @Schema(title = "Name of the restaurant.")
    @NotBlank
    private String name;

    @Schema(title = "City in which the restaurant is located.")
    @NotBlank
    private String city;

    @Schema(title = "The estimated cost for 2 people at the restaurant.")
    @NotBlank
    private Integer estimatedCost;

    @Schema(title = "Average rating of the restaurant.")
    @NotBlank
    private String averageRating;

    @Schema(title = "Total reviews.")
    @NotBlank
    private Integer votes;

}
