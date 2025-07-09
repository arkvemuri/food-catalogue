package com.codedecode.foodcatalogue.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemDTOTest {

    private FoodItemDTO foodItemDTO;

    @BeforeEach
    void setUp() {
        foodItemDTO = new FoodItemDTO();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        FoodItemDTO dto = new FoodItemDTO();

        // Then
        assertNotNull(dto);
        assertEquals(0, dto.getId());
        assertNull(dto.getItemName());
        assertNull(dto.getItemDescription());
        assertFalse(dto.isVeg());
        assertNull(dto.getPrice());
        assertNull(dto.getRestaurantId());
        assertNull(dto.getQuantity());
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
        FoodItemDTO dto = new FoodItemDTO(id, itemName, itemDescription, isVeg, price, restaurantId, quantity);

        // Then
        assertEquals(id, dto.getId());
        assertEquals(itemName, dto.getItemName());
        assertEquals(itemDescription, dto.getItemDescription());
        assertEquals(isVeg, dto.isVeg());
        assertEquals(price, dto.getPrice());
        assertEquals(restaurantId, dto.getRestaurantId());
        assertEquals(quantity, dto.getQuantity());
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
        foodItemDTO.setId(id);
        foodItemDTO.setItemName(itemName);
        foodItemDTO.setItemDescription(itemDescription);
        foodItemDTO.setVeg(isVeg);
        foodItemDTO.setPrice(price);
        foodItemDTO.setRestaurantId(restaurantId);
        foodItemDTO.setQuantity(quantity);

        // Then
        assertEquals(id, foodItemDTO.getId());
        assertEquals(itemName, foodItemDTO.getItemName());
        assertEquals(itemDescription, foodItemDTO.getItemDescription());
        assertEquals(isVeg, foodItemDTO.isVeg());
        assertEquals(price, foodItemDTO.getPrice());
        assertEquals(restaurantId, foodItemDTO.getRestaurantId());
        assertEquals(quantity, foodItemDTO.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        FoodItemDTO dto1 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItemDTO dto2 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItemDTO dto3 = new FoodItemDTO(2, "Burger", "Tasty", false, new BigDecimal("12.99"), 1, 5);

        // Then
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        foodItemDTO.setId(1);
        foodItemDTO.setItemName("Pizza");
        foodItemDTO.setItemDescription("Delicious cheese pizza");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("15.99"));
        foodItemDTO.setRestaurantId(1);
        foodItemDTO.setQuantity(10);

        // When
        String toString = foodItemDTO.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Pizza"));
        assertTrue(toString.contains("15.99"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void testSetNullValues() {
        // When
        foodItemDTO.setItemName(null);
        foodItemDTO.setItemDescription(null);
        foodItemDTO.setPrice(null);
        foodItemDTO.setRestaurantId(null);
        foodItemDTO.setQuantity(null);

        // Then
        assertNull(foodItemDTO.getItemName());
        assertNull(foodItemDTO.getItemDescription());
        assertNull(foodItemDTO.getPrice());
        assertNull(foodItemDTO.getRestaurantId());
        assertNull(foodItemDTO.getQuantity());
    }

    @Test
    void testVegFlag() {
        // When
        foodItemDTO.setVeg(true);
        // Then
        assertTrue(foodItemDTO.isVeg());

        // When
        foodItemDTO.setVeg(false);
        // Then
        assertFalse(foodItemDTO.isVeg());
    }

    @Test
    void testPriceWithDifferentTypes() {
        // Test with BigDecimal
        BigDecimal bigDecimalPrice = new BigDecimal("15.99");
        foodItemDTO.setPrice(bigDecimalPrice);
        assertEquals(bigDecimalPrice, foodItemDTO.getPrice());

        // Test with Integer
        Integer integerPrice = 15;
        foodItemDTO.setPrice(integerPrice);
        assertEquals(integerPrice, foodItemDTO.getPrice());

        // Test with Double
        Double doublePrice = 15.99;
        foodItemDTO.setPrice(doublePrice);
        assertEquals(doublePrice, foodItemDTO.getPrice());
    }

    @Test
    void testBuilderPattern() {
        // Given - simulate builder pattern usage
        FoodItemDTO dto = new FoodItemDTO();
        
        // When - chain setters
        dto.setId(1);
        dto.setItemName("Pizza");
        dto.setItemDescription("Delicious");
        dto.setVeg(true);
        dto.setPrice(new BigDecimal("15.99"));
        dto.setRestaurantId(1);
        dto.setQuantity(10);

        // Then
        assertEquals(1, dto.getId());
        assertEquals("Pizza", dto.getItemName());
        assertEquals("Delicious", dto.getItemDescription());
        assertTrue(dto.isVeg());
        assertEquals(new BigDecimal("15.99"), dto.getPrice());
        assertEquals(1, dto.getRestaurantId());
        assertEquals(10, dto.getQuantity());
    }

    @Test
    void testCopyConstructor() {
        // Given
        FoodItemDTO original = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);

        // When - manual copy
        FoodItemDTO copy = new FoodItemDTO(
            original.getId(),
            original.getItemName(),
            original.getItemDescription(),
            original.isVeg(),
            original.getPrice(),
            original.getRestaurantId(),
            original.getQuantity()
        );

        // Then
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testEqualsWithNull() {
        // Given
        FoodItemDTO dto = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);

        // Then
        assertNotEquals(dto, null);
        assertNotEquals(null, dto);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        FoodItemDTO dto = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        String notADTO = "Not a DTO";

        // Then
        assertNotEquals(dto, notADTO);
    }

    @Test
    void testEqualsReflexive() {
        // Given
        FoodItemDTO dto = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);

        // Then
        assertEquals(dto, dto);
    }

    @Test
    void testEqualsSymmetric() {
        // Given
        FoodItemDTO dto1 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItemDTO dto2 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void testEqualsTransitive() {
        // Given
        FoodItemDTO dto1 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItemDTO dto2 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);
        FoodItemDTO dto3 = new FoodItemDTO(1, "Pizza", "Delicious", true, new BigDecimal("15.99"), 1, 10);

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto2, dto3);
        assertEquals(dto1, dto3);
    }
}