package com.codedecode.foodcatalogue.controller;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;
import com.codedecode.foodcatalogue.service.FoodCatalogueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FoodCatalogueControllerTest {

    @Mock
    private FoodCatalogueService foodCatalogueService;

    @InjectMocks
    private FoodCatalogueController foodCatalogueController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FoodItemDTO foodItemDTO;
    private FoodCataloguePage foodCataloguePage;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodCatalogueController).build();
        objectMapper = new ObjectMapper();
        
        // Setup test data
        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setItemName("Pizza");
        foodItemDTO.setItemDescription("Delicious cheese pizza");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("15.99"));
        foodItemDTO.setRestaurantId(1);
        foodItemDTO.setQuantity(10);

        // Setup food catalogue page
        FoodItem foodItem = new FoodItem();
        foodItem.setId(1);
        foodItem.setItemName("Pizza");
        foodItem.setItemDescription("Delicious cheese pizza");
        foodItem.setVeg(true);
        foodItem.setPrice(new BigDecimal("15.99"));
        foodItem.setRestaurantId(1);
        foodItem.setQuantity(10);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setCity("Test City");
        restaurant.setRestaurantDescription("A test restaurant");

        List<FoodItem> foodItems = Arrays.asList(foodItem);
        foodCataloguePage = new FoodCataloguePage(foodItems, restaurant);
    }

    @Test
    void testAddFoodItem_Success() throws Exception {
        // Given
        when(foodCatalogueService.addFoodItem(any(FoodItemDTO.class))).thenReturn(foodItemDTO);

        // When & Then
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodItemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemName").value("Pizza"))
                .andExpect(jsonPath("$.itemDescription").value("Delicious cheese pizza"))
                .andExpect(jsonPath("$.veg").value(true))
                .andExpect(jsonPath("$.price").value(15.99))
                .andExpect(jsonPath("$.restaurantId").value(1))
                .andExpect(jsonPath("$.quantity").value(10));

        verify(foodCatalogueService, times(1)).addFoodItem(any(FoodItemDTO.class));
    }

    @Test
    void testAddFoodItem_DirectCall() {
        // Given
        when(foodCatalogueService.addFoodItem(any(FoodItemDTO.class))).thenReturn(foodItemDTO);

        // When
        ResponseEntity<FoodItemDTO> response = foodCatalogueController.addFoodItem(foodItemDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(foodItemDTO, response.getBody());
        verify(foodCatalogueService, times(1)).addFoodItem(foodItemDTO);
    }

    @Test
    void testFetchRestauDetailsWithFoodMenu_Success() throws Exception {
        // Given
        Integer restaurantId = 1;
        when(foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId)).thenReturn(foodCataloguePage);

        // When & Then
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value(1))
                .andExpect(jsonPath("$.restaurant.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.restaurant.address").value("123 Test St"))
                .andExpect(jsonPath("$.restaurant.city").value("Test City"))
                .andExpect(jsonPath("$.foodItemsList").isArray())
                .andExpect(jsonPath("$.foodItemsList[0].id").value(1))
                .andExpect(jsonPath("$.foodItemsList[0].itemName").value("Pizza"));

        verify(foodCatalogueService, times(1)).fetchFoodCataloguePageDetails(restaurantId);
    }

    @Test
    void testFetchRestauDetailsWithFoodMenu_DirectCall() {
        // Given
        Integer restaurantId = 1;
        when(foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId)).thenReturn(foodCataloguePage);

        // When
        ResponseEntity<FoodCataloguePage> response = foodCatalogueController.fetchRestauDetailsWithFoodMenu(restaurantId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foodCataloguePage, response.getBody());
        verify(foodCatalogueService, times(1)).fetchFoodCataloguePageDetails(restaurantId);
    }

    @Test
    void testAddFoodItem_WithNullInput() throws Exception {
        // When & Then
        // Spring automatically handles null JSON input and returns 400 Bad Request
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFetchRestauDetailsWithFoodMenu_WithInvalidId() throws Exception {
        // Given
        Integer invalidRestaurantId = -1;
        when(foodCatalogueService.fetchFoodCataloguePageDetails(invalidRestaurantId))
                .thenThrow(new RuntimeException("Restaurant not found"));

        // When & Then
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", invalidRestaurantId))
                .andExpect(status().isInternalServerError());

        verify(foodCatalogueService, times(1)).fetchFoodCataloguePageDetails(invalidRestaurantId);
    }
}