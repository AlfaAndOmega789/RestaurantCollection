package com.tmsproject.restaurantcollection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = RestaurantEntity.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldNameConstants
public class RestaurantEntity {

    public static final String TABLE_NAME = "restaurants";

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(name = "estimated_cost", nullable = false)
    private Integer estimatedCost;

    @Column(name = "average_rating", nullable = false)
    private BigDecimal averageRating;

    @Column(nullable = false)
    private Integer votes;

}
