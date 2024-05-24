package com.tmsproject.restaurantcollection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

import static com.tmsproject.restaurantcollection.domain.ResIdSeq.RES_SEQUENCE_NAME;
import static com.tmsproject.restaurantcollection.domain.ResIdSeq.RES_SEQ_GENERATOR;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class BaseEntity {
    @Id
    @SequenceGenerator(name = RES_SEQ_GENERATOR, sequenceName = RES_SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RES_SEQ_GENERATOR)
    @Column(nullable = false, updatable = false)
    private Long id;
}
