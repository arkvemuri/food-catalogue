package com.codedecode.foodcatalogue.service;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;
import com.codedecode.foodcatalogue.repo.FoodItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FoodCatalogueServiceTest {

    @MockitoBean
    private FoodItemRepo foodItemRepo;

    @MockitoBean
    private RestTemplate restTemplate;

    @Autowired
    private FoodCatalogueService foodCatalogueService;

    private FoodItem foodItem;
    private FoodItemDTO foodItemDTO;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // Setup test data
        foodItem = new FoodItem();
        foodItem.setId(1);
        foodItem.setItemName("Pizza");
        foodItem.setItemDescription("Delicious cheese pizza");
        foodItem.setVeg(true);
        foodItem.setPrice(new BigDecimal("15.99"));
        foodItem.setRestaurantId(1);
        foodItem.setQuantity(10);

        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setItemName("Pizza");
        foodItemDTO.setItemDescription("Delicious cheese pizza");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("15.99"));
        foodItemDTO.setRestaurantId(1);
        foodItemDTO.setQuantity(10);

        restaurant = new Restaurant();
        restaurant.setId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setCity("Test City");
        restaurant.setRestaurantDescription("A test restaurant");
    }

    @Test
    void testAddFoodItem_Success() {
        // Given
        when(foodItemRepo.save(any(FoodItem.class))).thenReturn(foodItem);

        // When
        FoodItemDTO result = foodCatalogueService.addFoodItem(foodItemDTO);

        // Then
        assertNotNull(result);
        assertEquals(foodItemDTO.getItemName(), result.getItemName());
        assertEquals(foodItemDTO.getItemDescription(), result.getItemDescription());
        assertEquals(foodItemDTO.isVeg(), result.isVeg());
        assertEquals(foodItemDTO.getPrice(), result.getPrice());
        assertEquals(foodItemDTO.getRestaurantId(), result.getRestaurantId());
        assertEquals(foodItemDTO.getQuantity(), result.getQuantity());

        verify(foodItemRepo, times(1)).save(any(FoodItem.class));
    }

    @Test
    void testFetchFoodCataloguePageDetails_Success() {
        // Given
        Integer restaurantId = 1;
        List<FoodItem> foodItemList = Arrays.asList(foodItem);
        
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class))).thenReturn(restaurant);

        // When
        FoodCataloguePage result = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);

        // Then
        assertNotNull(result);
        assertEquals(foodItemList, result.getFoodItemsList());
        assertEquals(restaurant, result.getRestaurant());
        assertEquals(1, result.getFoodItemsList().size());
        assertEquals("Pizza", result.getFoodItemsList().get(0).getItemName());
        assertEquals("Test Restaurant", result.getRestaurant().getName());

        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(
            "http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, 
            Restaurant.class
        );
    }

    @Test
    void testFetchFoodCataloguePageDetails_EmptyFoodList() {
        // Given
        Integer restaurantId = 1;
        List<FoodItem> emptyFoodItemList = Collections.emptyList();
        
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(emptyFoodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class))).thenReturn(restaurant);

        // When
        FoodCataloguePage result = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);

        // Then
        assertNotNull(result);
        assertTrue(result.getFoodItemsList().isEmpty());
        assertEquals(restaurant, result.getRestaurant());

        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(
            "http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, 
            Restaurant.class
        );
    }

    @Test
    void testFetchFoodCataloguePageDetails_RestaurantServiceFailure() {
        // Given
        Integer restaurantId = 1;
        List<FoodItem> foodItemList = Arrays.asList(foodItem);
        
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
            .thenThrow(new RuntimeException("Restaurant service unavailable"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);
        });

        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(
            "http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, 
            Restaurant.class
        );
    }

    @Test
    void testFetchFoodCataloguePageDetails_NullRestaurant() {
        // Given
        Integer restaurantId = 1;
        List<FoodItem> foodItemList = Arrays.asList(foodItem);
        
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class))).thenReturn(null);

        // When
        FoodCataloguePage result = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);

        // Then
        assertNotNull(result);
        assertEquals(foodItemList, result.getFoodItemsList());
        assertNull(result.getRestaurant());

        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(
            "http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, 
            Restaurant.class
        );
    }

    @Test
    void testAddFoodItem_RepositoryFailure() {
        // Given
        when(foodItemRepo.save(any(FoodItem.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            foodCatalogueService.addFoodItem(foodItemDTO);
        });

        verify(foodItemRepo, times(1)).save(any(FoodItem.class));
    }

    @Test
    void testFetchFoodCataloguePageDetails_MultipleItems() {
        // Given
        Integer restaurantId = 1;
        
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setId(2);
        foodItem2.setItemName("Burger");
        foodItem2.setItemDescription("Tasty burger");
        foodItem2.setVeg(false);
        foodItem2.setPrice(new BigDecimal("12.99"));
        foodItem2.setRestaurantId(1);
        foodItem2.setQuantity(5);
        
        List<FoodItem> foodItemList = Arrays.asList(foodItem, foodItem2);
        
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class))).thenReturn(restaurant);

        // When
        FoodCataloguePage result = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getFoodItemsList().size());
        assertEquals("Pizza", result.getFoodItemsList().get(0).getItemName());
        assertEquals("Burger", result.getFoodItemsList().get(1).getItemName());
        assertEquals(restaurant, result.getRestaurant());

        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(
            "http://RESTAURANT-SERVICE/restaurant/fetchById/" + restaurantId, 
            Restaurant.class
        );
    }
}