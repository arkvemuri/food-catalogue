package com.codedecode.foodcatalogue.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemTest {

    private FoodItem foodItem;

    @BeforeEach
    void setUp() {
        foodItem = new FoodItem();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        FoodItem item = new FoodItem();

        // Then
        assertNotNull(item);
        assertEquals(0, item.getId());
        assertNull(item.getItemName());
        assertNull(item.getItemDescription());
        assertFalse(item.isVeg());
        assertNull(item.getPrice());
        assertNull(item.getRestaurantId());
        assertNull(item.getQuantity());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        int id = 1;
        String itemName = "Pizza";
        String itemDescription = "Delicious cheese pizza";
        boolean isVeg = true;
        BigDecimal price = new BigDecimal("15.99");
        Integer restaurantId = 1;
        Integer quantity = 10;

        // When
        FoodItem item = new FoodItem(id, itemName, itemDescription, isVeg, price, restaurantId, quantity);

        // Then
        assertEquals(id, item.getId());
        assertEquals(itemName, item.getItemName());
        assertEquals(itemDescription, item.getItemDescription());
        assertEquals(isVeg, item.isVeg());
        assertEquals(price, item.getPrice());
        assertEquals(restaurantId, item.getRestaurantId());
        assertEquals(quantity, item.getQuantity());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        int id = 1;
        String itemName = "Pizza";
        String itemDescription = "Delicious cheese pizza";
        boolean isVeg = true;
        BigDecimal price = new BigDecimal("15.99");
        Integer restaurantId = 1;
        Integer quantity = 10;

        // When
        foodItem.setId(id);
        foodItem.setItemName(itemName);
        foodItem.setItemDescription(itemDescription);
        foodItem.setVeg(isVeg);
        foodItem.setPrice(price);
        foodItem.setRestaurantId(restaurantId);
        foodItem.setQuantity(quantity);

        // Then
        assertEquals(id, foodItem.getId());
        assertEquals(itemName, foodItem.getItemName());
        assertEquals(itemDescription, foodItem.getItemDescription());
        assertEquals(isVeg, foodItem.isVeg());
        assertEquals(price, foodItem.getPrice());
        assertEquals(restaurantId, foodItem.getRestaurantId());
        assertEquals(quantity, foodItem.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        FoodItem item1 = new FoodItem(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItem item2 = new FoodItem(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItem item3 = new FoodItem(2, "Burger", "Tasty", false, new BigDecimal("12.99"), 1, 5);

        // Then
        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        foodItem.setId(1);
        foodItem.setItemName("Pizza");
        foodItem.setItemDescription("Delicious cheese pizza");
        foodItem.setVeg(true);
        foodItem.setPrice(new BigDecimal("15.99"));
        foodItem.setRestaurantId(1);
        foodItem.setQuantity(10);

        // When
        String toString = foodItem.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Pizza"));
        assertTrue(toString.contains("15.99"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void testSetNullValues() {
        // When
        foodItem.setItemName(null);
        foodItem.setItemDescription(null);
        foodItem.setPrice(null);
        foodItem.setRestaurantId(null);
        foodItem.setQuantity(null);

        // Then
        assertNull(foodItem.getItemName());
        assertNull(foodItem.getItemDescription());
        assertNull(foodItem.getPrice());
        assertNull(foodItem.getRestaurantId());
        assertNull(foodItem.getQuantity());
    }

    @Test
    void testSetEmptyStrings() {
        // When
        foodItem.setItemName("");
        foodItem.setItemDescription("");

        // Then
        assertEquals("", foodItem.getItemName());
        assertEquals("", foodItem.getItemDescription());
    }

    @Test
    void testVegFlag() {
        // When
        foodItem.setVeg(true);
        // Then
        assertTrue(foodItem.isVeg());

        // When
        foodItem.setVeg(false);
        // Then
        assertFalse(foodItem.isVeg());
    }

    @Test
    void testPriceWithDifferentTypes() {
        // Test with BigDecimal
        BigDecimal bigDecimalPrice = new BigDecimal("15.99");
        foodItem.setPrice(bigDecimalPrice);
        assertEquals(bigDecimalPrice, foodItem.getPrice());

        // Test with Integer
        Integer integerPrice = 15;
        foodItem.setPrice(integerPrice);
        assertEquals(integerPrice, foodItem.getPrice());

        // Test with Double
        Double doublePrice = 15.99;
        foodItem.setPrice(doublePrice);
        assertEquals(doublePrice, foodItem.getPrice());
    }

    @Test
    void testZeroValues() {
        // When
        foodItem.setId(0);
        foodItem.setPrice(BigDecimal.ZERO);
        foodItem.setRestaurantId(0);
        foodItem.setQuantity(0);

        // Then
        assertEquals(0, foodItem.getId());
        assertEquals(BigDecimal.ZERO, foodItem.getPrice());
        assertEquals(0, foodItem.getRestaurantId());
        assertEquals(0, foodItem.getQuantity());
    }

    @Test
    void testNegativeValues() {
        // When
        foodItem.setId(-1);
        foodItem.setPrice(new BigDecimal("-10.00"));
        foodItem.setRestaurantId(-1);
        foodItem.setQuantity(-5);

        // Then
        assertEquals(-1, foodItem.getId());
        assertEquals(new BigDecimal("-10.00"), foodItem.getPrice());
        assertEquals(-1, foodItem.getRestaurantId());
        assertEquals(-5, foodItem.getQuantity());
    }

    @Test
    void testLargeValues() {
        // When
        foodItem.setId(Integer.MAX_VALUE);
        foodItem.setPrice(new BigDecimal("999999.99"));
        foodItem.setRestaurantId(Integer.MAX_VALUE);
        foodItem.setQuantity(Integer.MAX_VALUE);

        // Then
        assertEquals(Integer.MAX_VALUE, foodItem.getId());
        assertEquals(new BigDecimal("999999.99"), foodItem.getPrice());
        assertEquals(Integer.MAX_VALUE, foodItem.getRestaurantId());
        assertEquals(Integer.MAX_VALUE, foodItem.getQuantity());
    }

    @Test
    void testLongStrings() {
        // Given
        String longName = "A".repeat(1000);
        String longDescription = "B".repeat(2000);

        // When
        foodItem.setItemName(longName);
        foodItem.setItemDescription(longDescription);

        // Then
        assertEquals(longName, foodItem.getItemName());
        assertEquals(longDescription, foodItem.getItemDescription());
    }
}