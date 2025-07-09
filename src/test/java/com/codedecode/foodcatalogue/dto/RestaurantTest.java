package com.codedecode.foodcatalogue.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Restaurant rest = new Restaurant();

        // Then
        assertNotNull(rest);
        assertEquals(0, rest.getId());
        assertNull(rest.getName());
        assertNull(rest.getAddress());
        assertNull(rest.getCity());
        assertNull(rest.getRestaurantDescription());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        int id = 1;
        String name = "Test Restaurant";
        String address = "123 Test St";
        String city = "Test City";
        String description = "A test restaurant";

        // When
        Restaurant rest = new Restaurant(id, name, address, city, description);

        // Then
        assertEquals(id, rest.getId());
        assertEquals(name, rest.getName());
        assertEquals(address, rest.getAddress());
        assertEquals(city, rest.getCity());
        assertEquals(description, rest.getRestaurantDescription());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        int id = 1;
        String name = "Test Restaurant";
        String address = "123 Test St";
        String city = "Test City";
        String description = "A test restaurant";

        // When
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setCity(city);
        restaurant.setRestaurantDescription(description);

        // Then
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(city, restaurant.getCity());
        assertEquals(description, restaurant.getRestaurantDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Restaurant rest1 = new Restaurant(1, "Test Restaurant", "123 Test St", "Test City", "A test restaurant");
        Restaurant rest2 = new Restaurant(1, "Test Restaurant", "123 Test St", "Test City", "A test restaurant");
        Restaurant rest3 = new Restaurant(2, "Another Restaurant", "456 Another St", "Another City", "Another restaurant");

        // Then
        assertEquals(rest1, rest2);
        assertNotEquals(rest1, rest3);
        assertEquals(rest1.hashCode(), rest2.hashCode());
        assertNotEquals(rest1.hashCode(), rest3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        restaurant.setId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setCity("Test City");
        restaurant.setRestaurantDescription("A test restaurant");

        // When
        String toString = restaurant.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Restaurant"));
        assertTrue(toString.contains("123 Test St"));
        assertTrue(toString.contains("Test City"));
        assertTrue(toString.contains("A test restaurant"));
    }

    @Test
    void testSetNullValues() {
        // When
        restaurant.setName(null);
        restaurant.setAddress(null);
        restaurant.setCity(null);
        restaurant.setRestaurantDescription(null);

        // Then
        assertNull(restaurant.getName());
        assertNull(restaurant.getAddress());
        assertNull(restaurant.getCity());
        assertNull(restaurant.getRestaurantDescription());
    }

    @Test
    void testSetEmptyStrings() {
        // When
        restaurant.setName("");
        restaurant.setAddress("");
        restaurant.setCity("");
        restaurant.setRestaurantDescription("");

        // Then
        assertEquals("", restaurant.getName());
        assertEquals("", restaurant.getAddress());
        assertEquals("", restaurant.getCity());
        assertEquals("", restaurant.getRestaurantDescription());
    }

    @Test
    void testEqualsWithNull() {
        // Given
        Restaurant rest = new Restaurant(1, "Test", "Address", "City", "Description");

        // Then
        assertNotEquals(rest, null);
        assertNotEquals(null, rest);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        Restaurant rest = new Restaurant(1, "Test", "Address", "City", "Description");
        String notARestaurant = "Not a Restaurant";

        // Then
        assertNotEquals(rest, notARestaurant);
    }

    @Test
    void testEqualsReflexive() {
        // Given
        Restaurant rest = new Restaurant(1, "Test", "Address", "City", "Description");

        // Then
        assertEquals(rest, rest);
    }

    @Test
    void testEqualsSymmetric() {
        // Given
        Restaurant rest1 = new Restaurant(1, "Test", "Address", "City", "Description");
        Restaurant rest2 = new Restaurant(1, "Test", "Address", "City", "Description");

        // Then
        assertEquals(rest1, rest2);
        assertEquals(rest2, rest1);
    }

    @Test
    void testEqualsTransitive() {
        // Given
        Restaurant rest1 = new Restaurant(1, "Test", "Address", "City", "Description");
        Restaurant rest2 = new Restaurant(1, "Test", "Address", "City", "Description");
        Restaurant rest3 = new Restaurant(1, "Test", "Address", "City", "Description");

        // Then
        assertEquals(rest1, rest2);
        assertEquals(rest2, rest3);
        assertEquals(rest1, rest3);
    }

    @Test
    void testCopyConstructor() {
        // Given
        Restaurant original = new Restaurant(1, "Test", "Address", "City", "Description");

        // When - manual copy
        Restaurant copy = new Restaurant(
            original.getId(),
            original.getName(),
            original.getAddress(),
            original.getCity(),
            original.getRestaurantDescription()
        );

        // Then
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void testZeroId() {
        // When
        restaurant.setId(0);

        // Then
        assertEquals(0, restaurant.getId());
    }

    @Test
    void testNegativeId() {
        // When
        restaurant.setId(-1);

        // Then
        assertEquals(-1, restaurant.getId());
    }

    @Test
    void testLargeId() {
        // When
        restaurant.setId(Integer.MAX_VALUE);

        // Then
        assertEquals(Integer.MAX_VALUE, restaurant.getId());
    }

    @Test
    void testLongStrings() {
        // Given
        String longName = "A".repeat(1000);
        String longAddress = "B".repeat(1000);
        String longCity = "C".repeat(1000);
        String longDescription = "D".repeat(2000);

        // When
        restaurant.setName(longName);
        restaurant.setAddress(longAddress);
        restaurant.setCity(longCity);
        restaurant.setRestaurantDescription(longDescription);

        // Then
        assertEquals(longName, restaurant.getName());
        assertEquals(longAddress, restaurant.getAddress());
        assertEquals(longCity, restaurant.getCity());
        assertEquals(longDescription, restaurant.getRestaurantDescription());
    }

    @Test
    void testSpecialCharacters() {
        // Given
        String specialName = "Café & Restaurant™";
        String specialAddress = "123 Main St. #456";
        String specialCity = "São Paulo";
        String specialDescription = "A restaurant with special chars: @#$%^&*()";

        // When
        restaurant.setName(specialName);
        restaurant.setAddress(specialAddress);
        restaurant.setCity(specialCity);
        restaurant.setRestaurantDescription(specialDescription);

        // Then
        assertEquals(specialName, restaurant.getName());
        assertEquals(specialAddress, restaurant.getAddress());
        assertEquals(specialCity, restaurant.getCity());
        assertEquals(specialDescription, restaurant.getRestaurantDescription());
    }
}