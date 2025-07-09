package com.codedecode.foodcatalogue.dto;

import com.codedecode.foodcatalogue.entity.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FoodCataloguePageTest {

    private FoodCataloguePage foodCataloguePage;
    private List<FoodItem> foodItemsList;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        foodCataloguePage = new FoodCataloguePage();

        // Setup food items
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setId(1);
        foodItem1.setItemName("Pizza");
        foodItem1.setItemDescription("Delicious cheese pizza");
        foodItem1.setVeg(true);
        foodItem1.setPrice(new BigDecimal("15.99"));
        foodItem1.setRestaurantId(1);
        foodItem1.setQuantity(10);

        FoodItem foodItem2 = new FoodItem();
        foodItem2.setId(2);
        foodItem2.setItemName("Burger");
        foodItem2.setItemDescription("Tasty beef burger");
        foodItem2.setVeg(false);
        foodItem2.setPrice(new BigDecimal("12.99"));
        foodItem2.setRestaurantId(1);
        foodItem2.setQuantity(5);

        foodItemsList = Arrays.asList(foodItem1, foodItem2);

        // Setup restaurant
        restaurant = new Restaurant();
        restaurant.setId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setCity("Test City");
        restaurant.setRestaurantDescription("A test restaurant");
    }

    @Test
    void testNoArgsConstructor() {
        // When
        FoodCataloguePage page = new FoodCataloguePage();

        // Then
        assertNotNull(page);
        assertNull(page.getFoodItemsList());
        assertNull(page.getRestaurant());
    }

    @Test
    void testAllArgsConstructor() {
        // When
        FoodCataloguePage page = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(foodItemsList, page.getFoodItemsList());
        assertEquals(restaurant, page.getRestaurant());
        assertEquals(2, page.getFoodItemsList().size());
        assertEquals("Test Restaurant", page.getRestaurant().getName());
    }

    @Test
    void testSettersAndGetters() {
        // When
        foodCataloguePage.setFoodItemsList(foodItemsList);
        foodCataloguePage.setRestaurant(restaurant);

        // Then
        assertEquals(foodItemsList, foodCataloguePage.getFoodItemsList());
        assertEquals(restaurant, foodCataloguePage.getRestaurant());
        assertEquals(2, foodCataloguePage.getFoodItemsList().size());
        assertEquals("Pizza", foodCataloguePage.getFoodItemsList().get(0).getItemName());
        assertEquals("Burger", foodCataloguePage.getFoodItemsList().get(1).getItemName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(foodItemsList, restaurant);
        FoodCataloguePage page2 = new FoodCataloguePage(foodItemsList, restaurant);
        
        Restaurant differentRestaurant = new Restaurant(2, "Different", "456 Different St", "Different City", "Different");
        FoodCataloguePage page3 = new FoodCataloguePage(foodItemsList, differentRestaurant);

        // Then
        assertEquals(page1, page2);
        assertNotEquals(page1, page3);
        assertEquals(page1.hashCode(), page2.hashCode());
        assertNotEquals(page1.hashCode(), page3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        foodCataloguePage.setFoodItemsList(foodItemsList);
        foodCataloguePage.setRestaurant(restaurant);

        // When
        String toString = foodCataloguePage.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Restaurant"));
        assertTrue(toString.contains("Pizza") || toString.contains("Burger"));
    }

    @Test
    void testSetNullValues() {
        // When
        foodCataloguePage.setFoodItemsList(null);
        foodCataloguePage.setRestaurant(null);

        // Then
        assertNull(foodCataloguePage.getFoodItemsList());
        assertNull(foodCataloguePage.getRestaurant());
    }

    @Test
    void testEmptyFoodItemsList() {
        // Given
        List<FoodItem> emptyList = Collections.emptyList();

        // When
        foodCataloguePage.setFoodItemsList(emptyList);
        foodCataloguePage.setRestaurant(restaurant);

        // Then
        assertNotNull(foodCataloguePage.getFoodItemsList());
        assertTrue(foodCataloguePage.getFoodItemsList().isEmpty());
        assertEquals(restaurant, foodCataloguePage.getRestaurant());
    }

    @Test
    void testSingleFoodItem() {
        // Given
        List<FoodItem> singleItemList = Arrays.asList(foodItemsList.get(0));

        // When
        foodCataloguePage.setFoodItemsList(singleItemList);
        foodCataloguePage.setRestaurant(restaurant);

        // Then
        assertEquals(1, foodCataloguePage.getFoodItemsList().size());
        assertEquals("Pizza", foodCataloguePage.getFoodItemsList().get(0).getItemName());
        assertEquals(restaurant, foodCataloguePage.getRestaurant());
    }

    @Test
    void testMutableList() {
        // Given
        List<FoodItem> mutableList = new ArrayList<>(foodItemsList);

        // When
        foodCataloguePage.setFoodItemsList(mutableList);
        
        // Add item to the original list
        FoodItem newItem = new FoodItem();
        newItem.setId(3);
        newItem.setItemName("Salad");
        mutableList.add(newItem);

        // Then
        assertEquals(3, foodCataloguePage.getFoodItemsList().size());
        assertEquals("Salad", foodCataloguePage.getFoodItemsList().get(2).getItemName());
    }

    @Test
    void testEqualsWithNull() {
        // Given
        FoodCataloguePage page = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertNotEquals(page, null);
        assertNotEquals(null, page);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        FoodCataloguePage page = new FoodCataloguePage(foodItemsList, restaurant);
        String notAPage = "Not a FoodCataloguePage";

        // Then
        assertNotEquals(page, notAPage);
    }

    @Test
    void testEqualsReflexive() {
        // Given
        FoodCataloguePage page = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(page, page);
    }

    @Test
    void testEqualsSymmetric() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(foodItemsList, restaurant);
        FoodCataloguePage page2 = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(page1, page2);
        assertEquals(page2, page1);
    }

    @Test
    void testEqualsTransitive() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(foodItemsList, restaurant);
        FoodCataloguePage page2 = new FoodCataloguePage(foodItemsList, restaurant);
        FoodCataloguePage page3 = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(page1, page2);
        assertEquals(page2, page3);
        assertEquals(page1, page3);
    }

    @Test
    void testCopyConstructor() {
        // Given
        FoodCataloguePage original = new FoodCataloguePage(foodItemsList, restaurant);

        // When - manual copy
        FoodCataloguePage copy = new FoodCataloguePage(
            original.getFoodItemsList(),
            original.getRestaurant()
        );

        // Then
        assertEquals(original, copy);
        assertNotSame(original, copy);
        // Note: The lists and restaurant objects are the same references
        assertSame(original.getFoodItemsList(), copy.getFoodItemsList());
        assertSame(original.getRestaurant(), copy.getRestaurant());
    }

    @Test
    void testEqualsWithDifferentFoodItemsList() {
        // Given
        FoodItem differentItem = new FoodItem();
        differentItem.setId(3);
        differentItem.setItemName("Pasta");
        List<FoodItem> differentList = Arrays.asList(differentItem);

        FoodCataloguePage page1 = new FoodCataloguePage(foodItemsList, restaurant);
        FoodCataloguePage page2 = new FoodCataloguePage(differentList, restaurant);

        // Then
        assertNotEquals(page1, page2);
    }

    @Test
    void testEqualsWithNullFoodItemsList() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(null, restaurant);
        FoodCataloguePage page2 = new FoodCataloguePage(null, restaurant);
        FoodCataloguePage page3 = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(page1, page2);
        assertNotEquals(page1, page3);
        assertNotEquals(page3, page1);
    }

    @Test
    void testEqualsWithNullRestaurant() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(foodItemsList, null);
        FoodCataloguePage page2 = new FoodCataloguePage(foodItemsList, null);
        FoodCataloguePage page3 = new FoodCataloguePage(foodItemsList, restaurant);

        // Then
        assertEquals(page1, page2);
        assertNotEquals(page1, page3);
        assertNotEquals(page3, page1);
    }

    @Test
    void testEqualsWithBothNull() {
        // Given
        FoodCataloguePage page1 = new FoodCataloguePage(null, null);
        FoodCataloguePage page2 = new FoodCataloguePage(null, null);

        // Then
        assertEquals(page1, page2);
    }
}