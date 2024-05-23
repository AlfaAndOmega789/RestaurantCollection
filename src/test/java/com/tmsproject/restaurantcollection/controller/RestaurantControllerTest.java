package com.tmsproject.restaurantcollection.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmsproject.restaurantcollection.domain.RestaurantEntity;
import com.tmsproject.restaurantcollection.dto.RestaurantDto;
import com.tmsproject.restaurantcollection.dto.RestaurantShortDto;
import com.tmsproject.restaurantcollection.mapper.RestaurantMapper;
import com.tmsproject.restaurantcollection.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RestaurantControllerTest {

    private static final String ROOT_URL = "/restaurant";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantMapper restaurantMapper;

    @Test
    void createUpdateDelete() throws Exception {
        //crate
        RestaurantDto restaurantDto = RestaurantDto.builder()
                .name("Тest")
                .city("TestCity")
                .estimatedCost(250)
                .averageRating("4.2553")
                .votes(843)
                .build();

        var requestBuilder = post(ROOT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantDto));
        var response = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        String location = response.getHeader("Location");
        assertNotNull(location);
        Long createdRecordId = TestHttpUtils.getId(location);
        assertNotNull(createdRecordId);
        restaurantDto.setId(createdRecordId);
        RestaurantEntity restaurantExpected = restaurantMapper.fromDto(restaurantDto);

        RestaurantEntity restaurantEntity = restaurantService.findById(createdRecordId).orElseThrow();
        assertEquals(restaurantExpected, restaurantEntity);

        //update
        RestaurantShortDto restaurantShortDto = RestaurantShortDto.builder()
                .averageRating("2.5")
                .votes(200)
                .build();
        restaurantDto.setAverageRating(restaurantShortDto.getAverageRating());
        restaurantDto.setVotes(restaurantShortDto.getVotes());
        restaurantExpected = restaurantMapper.fromDto(restaurantDto);

        requestBuilder = put(ROOT_URL + "/" + createdRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantShortDto));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        restaurantEntity = restaurantService.findById(createdRecordId).orElseThrow();
        assertEquals(restaurantExpected, restaurantEntity);

        //updateByAverageRating
        restaurantShortDto = RestaurantShortDto.builder()
                .averageRating("3.55432536")
                .build();
        restaurantDto.setAverageRating(restaurantShortDto.getAverageRating());
        restaurantExpected = restaurantMapper.fromDto(restaurantDto);

        requestBuilder = put(ROOT_URL + "/" + createdRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantShortDto));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        restaurantEntity = restaurantService.findById(createdRecordId).orElseThrow();
        assertEquals(restaurantExpected, restaurantEntity);

        //update
        restaurantShortDto = RestaurantShortDto.builder()
                .votes(5325)
                .build();
        restaurantDto.setVotes(restaurantShortDto.getVotes());
        restaurantExpected = restaurantMapper.fromDto(restaurantDto);

        requestBuilder = put(ROOT_URL + "/" + createdRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantShortDto));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        restaurantEntity = restaurantService.findById(createdRecordId).orElseThrow();
        assertEquals(restaurantExpected, restaurantEntity);


        //delete
        requestBuilder = delete(ROOT_URL + "/" + createdRecordId)
                .content(objectMapper.writeValueAsString(restaurantShortDto));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

    }

    @Test
    void getById() throws Exception {
        var requestBuilder = get(ROOT_URL + "/query?id=1");
        var response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        RestaurantDto restaurantDto = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<RestaurantDto>() {
                });
        RestaurantDto expected = RestaurantDto.builder()
                .id(1L)
                .name("Dominos")
                .city("Minsk")
                .estimatedCost(30)
                .averageRating("4.8574")
                .votes(673)
                .build();

        assertEquals(expected, restaurantDto);
    }

    @Test
    void getByCity() throws Exception {
        var requestBuilder = get(ROOT_URL + "/query?city=brest");
        var response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<RestaurantDto> list = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<List<RestaurantDto>>() {
                });

        assertEquals(2, list.size());
        RestaurantDto expected1 = RestaurantDto.builder()
                .id(5L)
                .name("Dominos")
                .city("Brest")
                .estimatedCost(30)
                .averageRating("4.34312")
                .votes(34)
                .build();
        RestaurantDto expected2 = RestaurantDto.builder()
                .id(6L)
                .name("KFC")
                .city("Brest")
                .estimatedCost(40)
                .averageRating("4.51346")
                .votes(654)
                .build();
        Map<Long, RestaurantDto> expectedMap = Map.of(
                expected1.getId(), expected1,
                expected2.getId(), expected2
        );
        list.forEach(dto ->
                assertEquals(expectedMap.get(dto.getId()), dto));

    }

    @Test
    void checkSort() throws Exception {
        var requestBuilder = get(ROOT_URL + "/sort");
        var response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<RestaurantDto> list = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<List<RestaurantDto>>() {
                });

        assertEquals(9, list.size());
        RestaurantDto expected1 = RestaurantDto.builder()
                .id(1L)
                .name("Dominos")
                .city("Minsk")
                .estimatedCost(30)
                .averageRating("4.8574")
                .votes(673)
                .build();
        RestaurantDto expected2 = RestaurantDto.builder()
                .id(7L)
                .name("Lisica")
                .city("Minsk")
                .estimatedCost(45)
                .averageRating("4.6543")
                .votes(765)
                .build();
        RestaurantDto expected3 = RestaurantDto.builder()
                .id(5L)
                .name("Dominos")
                .city("Brest")
                .estimatedCost(30)
                .averageRating("4.34312")
                .votes(34)
                .build();

        assertEquals(expected1, list.get(0));
        assertEquals(expected2, list.get(1));
        assertEquals(expected3, list.get(8));
    }
}