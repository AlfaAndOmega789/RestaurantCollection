package com.tmsproject.restaurantcollection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEntityDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
