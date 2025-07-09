package com.codedecode.foodcatalogue.repo;

import com.codedecode.foodcatalogue.entity.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FoodItemRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FoodItemRepo foodItemRepo;

    private FoodItem foodItem1;
    private FoodItem foodItem2;
    private FoodItem foodItem3;

    @BeforeEach
    void setUp() {
        // Setup test data for restaurant 1
        foodItem1 = new FoodItem();
        foodItem1.setItemName("Pizza");
        foodItem1.setItemDescription("Delicious cheese pizza");
        foodItem1.setVeg(true);
        foodItem1.setPrice(new BigDecimal("15.99"));
        foodItem1.setRestaurantId(1);
        foodItem1.setQuantity(10);

        foodItem2 = new FoodItem();
        foodItem2.setItemName("Burger");
        foodItem2.setItemDescription("Tasty beef burger");
        foodItem2.setVeg(false);
        foodItem2.setPrice(new BigDecimal("12.99"));
        foodItem2.setRestaurantId(1);
        foodItem2.setQuantity(5);

        // Setup test data for restaurant 2
        foodItem3 = new FoodItem();
        foodItem3.setItemName("Pasta");
        foodItem3.setItemDescription("Creamy pasta");
        foodItem3.setVeg(true);
        foodItem3.setPrice(new BigDecimal("13.99"));
        foodItem3.setRestaurantId(2);
        foodItem3.setQuantity(8);

        // Persist test data
        entityManager.persistAndFlush(foodItem1);
        entityManager.persistAndFlush(foodItem2);
        entityManager.persistAndFlush(foodItem3);
    }

    @Test
    void testFindByRestaurantId_ExistingRestaurant() {
        // When
        List<FoodItem> result = foodItemRepo.findByRestaurantId(1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify the items belong to restaurant 1
        result.forEach(item -> assertEquals(1, item.getRestaurantId()));
        
        // Verify specific items are present
        assertTrue(result.stream().anyMatch(item -> "Pizza".equals(item.getItemName())));
        assertTrue(result.stream().anyMatch(item -> "Burger".equals(item.getItemName())));
    }

    @Test
    void testFindByRestaurantId_SingleItem() {
        // When
        List<FoodItem> result = foodItemRepo.findByRestaurantId(2);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getItemName());
        assertEquals(2, result.get(0).getRestaurantId());
    }

    @Test
    void testFindByRestaurantId_NonExistingRestaurant() {
        // When
        List<FoodItem> result = foodItemRepo.findByRestaurantId(999);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByRestaurantId_NullRestaurantId() {
        // When
        List<FoodItem> result = foodItemRepo.findByRestaurantId(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveAndFindById() {
        // Given
        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setItemName("Salad");
        newFoodItem.setItemDescription("Fresh green salad");
        newFoodItem.setVeg(true);
        newFoodItem.setPrice(new BigDecimal("8.99"));
        newFoodItem.setRestaurantId(3);
        newFoodItem.setQuantity(15);

        // When
        FoodItem savedItem = foodItemRepo.save(newFoodItem);

        // Then
        assertNotNull(savedItem);
        assertNotNull(savedItem.getId());
        assertEquals("Salad", savedItem.getItemName());
        assertEquals("Fresh green salad", savedItem.getItemDescription());
        assertTrue(savedItem.isVeg());
        assertEquals(new BigDecimal("8.99"), savedItem.getPrice());
        assertEquals(3, savedItem.getRestaurantId());
        assertEquals(15, savedItem.getQuantity());

        // Verify it can be found
        FoodItem foundItem = foodItemRepo.findById(savedItem.getId()).orElse(null);
        assertNotNull(foundItem);
        assertEquals(savedItem.getItemName(), foundItem.getItemName());
    }

    @Test
    void testFindAll() {
        // When
        List<FoodItem> allItems = foodItemRepo.findAll();

        // Then
        assertNotNull(allItems);
        assertEquals(3, allItems.size());
    }

    @Test
    void testDeleteById() {
        // Given
        Integer itemId = foodItem1.getId();
        assertTrue(foodItemRepo.existsById(itemId));

        // When
        foodItemRepo.deleteById(itemId);

        // Then
        assertFalse(foodItemRepo.existsById(itemId));
        
        // Verify restaurant 1 now has only 1 item
        List<FoodItem> remainingItems = foodItemRepo.findByRestaurantId(1);
        assertEquals(1, remainingItems.size());
        assertEquals("Burger", remainingItems.get(0).getItemName());
    }

    @Test
    void testUpdateFoodItem() {
        // Given
        FoodItem existingItem = foodItemRepo.findById(foodItem1.getId()).orElse(null);
        assertNotNull(existingItem);

        // When
        existingItem.setItemName("Updated Pizza");
        existingItem.setPrice(new BigDecimal("18.99"));
        existingItem.setQuantity(20);
        FoodItem updatedItem = foodItemRepo.save(existingItem);

        // Then
        assertNotNull(updatedItem);
        assertEquals("Updated Pizza", updatedItem.getItemName());
        assertEquals(new BigDecimal("18.99"), updatedItem.getPrice());
        assertEquals(20, updatedItem.getQuantity());

        // Verify the update persisted
        FoodItem retrievedItem = foodItemRepo.findById(foodItem1.getId()).orElse(null);
        assertNotNull(retrievedItem);
        assertEquals("Updated Pizza", retrievedItem.getItemName());
        assertEquals(new BigDecimal("18.99"), retrievedItem.getPrice());
    }

    @Test
    void testFindByRestaurantId_OrderConsistency() {
        // When - call multiple times
        List<FoodItem> result1 = foodItemRepo.findByRestaurantId(1);
        List<FoodItem> result2 = foodItemRepo.findByRestaurantId(1);

        // Then - results should be consistent
        assertEquals(result1.size(), result2.size());
        for (int i = 0; i < result1.size(); i++) {
            assertEquals(result1.get(i).getId(), result2.get(i).getId());
        }
    }
}