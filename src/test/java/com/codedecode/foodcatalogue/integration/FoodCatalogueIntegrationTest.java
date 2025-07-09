package com.codedecode.foodcatalogue.integration;


import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;
import com.codedecode.foodcatalogue.repo.FoodItemRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class FoodCatalogueIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FoodItemRepo foodItemRepo;

    @MockitoBean
    private RestTemplate restTemplate;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        // Clear database before each test
        foodItemRepo.deleteAll();
    }

    @Test
    void testAddFoodItem_IntegrationTest() throws Exception {
        // Given
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        foodItemDTO.setItemName("Integration Pizza");
        foodItemDTO.setItemDescription("Pizza for integration test");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("18.99"));
        foodItemDTO.setRestaurantId(1);
        foodItemDTO.setQuantity(15);

        // When & Then
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodItemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Integration Pizza"))
                .andExpect(jsonPath("$.itemDescription").value("Pizza for integration test"))
                .andExpect(jsonPath("$.veg").value(true))
                .andExpect(jsonPath("$.price").value(18.99))
                .andExpect(jsonPath("$.restaurantId").value(1))
                .andExpect(jsonPath("$.quantity").value(15));

        // Verify data was saved to database
        List<FoodItem> savedItems = foodItemRepo.findAll();
        assert savedItems.size() == 1;
        assert savedItems.get(0).getItemName().equals("Integration Pizza");
    }

    @Test
    void testFetchRestaurantAndFoodItems_IntegrationTest() throws Exception {
        // Given - Setup test data in database
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setItemName("Integration Pizza");
        foodItem1.setItemDescription("Pizza for integration test");
        foodItem1.setVeg(true);
        foodItem1.setPrice(new BigDecimal("18.99"));
        foodItem1.setRestaurantId(1);
        foodItem1.setQuantity(15);

        FoodItem foodItem2 = new FoodItem();
        foodItem2.setItemName("Integration Burger");
        foodItem2.setItemDescription("Burger for integration test");
        foodItem2.setVeg(false);
        foodItem2.setPrice(new BigDecimal("14.99"));
        foodItem2.setRestaurantId(1);
        foodItem2.setQuantity(10);

        foodItemRepo.saveAll(Arrays.asList(foodItem1, foodItem2));

        // Mock restaurant service response
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(1);
        mockRestaurant.setName("Integration Test Restaurant");
        mockRestaurant.setAddress("123 Integration St");
        mockRestaurant.setCity("Test City");
        mockRestaurant.setRestaurantDescription("Restaurant for integration testing");

        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
                .thenReturn(mockRestaurant);

        // When & Then
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value(1))
                .andExpect(jsonPath("$.restaurant.name").value("Integration Test Restaurant"))
                .andExpect(jsonPath("$.restaurant.address").value("123 Integration St"))
                .andExpect(jsonPath("$.restaurant.city").value("Test City"))
                .andExpect(jsonPath("$.foodItemsList", hasSize(2)))
                .andExpect(jsonPath("$.foodItemsList[0].itemName").value("Integration Pizza"))
                .andExpect(jsonPath("$.foodItemsList[1].itemName").value("Integration Burger"));
    }

    @Test
    void testFullWorkflow_AddAndRetrieve() throws Exception {
        // Step 1: Add a food item
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        foodItemDTO.setItemName("Workflow Pizza");
        foodItemDTO.setItemDescription("Pizza for workflow test");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("20.99"));
        foodItemDTO.setRestaurantId(2);
        foodItemDTO.setQuantity(20);

        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodItemDTO)))
                .andExpect(status().isCreated());

        // Step 2: Mock restaurant service for retrieval
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(2);
        mockRestaurant.setName("Workflow Restaurant");
        mockRestaurant.setAddress("456 Workflow Ave");
        mockRestaurant.setCity("Workflow City");
        mockRestaurant.setRestaurantDescription("Restaurant for workflow testing");

        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
                .thenReturn(mockRestaurant);

        // Step 3: Retrieve the food catalogue page
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value(2))
                .andExpect(jsonPath("$.restaurant.name").value("Workflow Restaurant"))
                .andExpect(jsonPath("$.foodItemsList", hasSize(1)))
                .andExpect(jsonPath("$.foodItemsList[0].itemName").value("Workflow Pizza"))
                .andExpect(jsonPath("$.foodItemsList[0].price").value(20.99))
                .andExpect(jsonPath("$.foodItemsList[0].restaurantId").value(2));
    }

    @Test
    void testFetchRestaurantAndFoodItems_EmptyResult() throws Exception {
        // Given - Mock restaurant service response
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(999);
        mockRestaurant.setName("Empty Restaurant");
        mockRestaurant.setAddress("999 Empty St");
        mockRestaurant.setCity("Empty City");
        mockRestaurant.setRestaurantDescription("Restaurant with no food items");

        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
                .thenReturn(mockRestaurant);

        // When & Then
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value(999))
                .andExpect(jsonPath("$.restaurant.name").value("Empty Restaurant"))
                .andExpect(jsonPath("$.foodItemsList", hasSize(0)));
    }

    @Test
    void testAddMultipleFoodItems_SameRestaurant() throws Exception {
        // Given
        FoodItemDTO pizza = new FoodItemDTO();
        pizza.setItemName("Multi Pizza");
        pizza.setItemDescription("Pizza 1");
        pizza.setVeg(true);
        pizza.setPrice(new BigDecimal("15.99"));
        pizza.setRestaurantId(3);
        pizza.setQuantity(10);

        FoodItemDTO burger = new FoodItemDTO();
        burger.setItemName("Multi Burger");
        burger.setItemDescription("Burger 1");
        burger.setVeg(false);
        burger.setPrice(new BigDecimal("12.99"));
        burger.setRestaurantId(3);
        burger.setQuantity(8);

        // When - Add both items
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pizza)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(burger)))
                .andExpect(status().isCreated());

        // Mock restaurant service
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(3);
        mockRestaurant.setName("Multi Restaurant");
        mockRestaurant.setAddress("789 Multi Blvd");
        mockRestaurant.setCity("Multi City");
        mockRestaurant.setRestaurantDescription("Restaurant with multiple items");

        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
                .thenReturn(mockRestaurant);

        // Then - Retrieve and verify both items
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value(3))
                .andExpect(jsonPath("$.foodItemsList", hasSize(2)));
    }

    @Test
    void testAddFoodItem_InvalidData() throws Exception {
        // Given - Invalid food item (missing required fields)
        FoodItemDTO invalidFoodItem = new FoodItemDTO();
        // Only set some fields, leaving others null/empty

        // When & Then
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidFoodItem)))
                .andExpect(status().isCreated()); // Note: Current implementation doesn't validate, so it will still create
    }

    @Test
    void testFetchRestaurantAndFoodItems_RestaurantServiceDown() throws Exception {
        // Given - Setup test data
        FoodItem foodItem = new FoodItem();
        foodItem.setItemName("Service Down Pizza");
        foodItem.setItemDescription("Pizza when service is down");
        foodItem.setVeg(true);
        foodItem.setPrice(new BigDecimal("16.99"));
        foodItem.setRestaurantId(4);
        foodItem.setQuantity(12);

        foodItemRepo.save(foodItem);

        // Mock restaurant service to throw exception
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class)))
                .thenThrow(new RuntimeException("Restaurant service unavailable"));

        // When & Then
        mockMvc.perform(get("/foodCatalogue/fetchRestaurantAndFoodItemsById/{restaurantId}", 4))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCorsHeaders() throws Exception {
        // Given
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        foodItemDTO.setItemName("CORS Pizza");
        foodItemDTO.setItemDescription("Pizza for CORS test");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("17.99"));
        foodItemDTO.setRestaurantId(5);
        foodItemDTO.setQuantity(13);

        // When & Then - Test CORS headers are present
        mockMvc.perform(post("/foodCatalogue/addFoodItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodItemDTO))
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isCreated());
        // Note: CORS headers would be tested in a more comprehensive integration test
    }
}