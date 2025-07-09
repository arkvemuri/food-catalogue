package com.codedecode.foodcatalogue.util;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for building test data objects
 */
public class TestDataBuilder {

    public static FoodItem createFoodItem(int id, String name, String description, boolean isVeg, 
                                         String price, Integer restaurantId, Integer quantity) {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(id);
        foodItem.setItemName(name);
        foodItem.setItemDescription(description);
        foodItem.setVeg(isVeg);
        foodItem.setPrice(new BigDecimal(price));
        foodItem.setRestaurantId(restaurantId);
        foodItem.setQuantity(quantity);
        return foodItem;
    }

    public static FoodItemDTO createFoodItemDTO(int id, String name, String description, boolean isVeg, 
                                               String price, Integer restaurantId, Integer quantity) {
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(id);
        foodItemDTO.setItemName(name);
        foodItemDTO.setItemDescription(description);
        foodItemDTO.setVeg(isVeg);
        foodItemDTO.setPrice(new BigDecimal(price));
        foodItemDTO.setRestaurantId(restaurantId);
        foodItemDTO.setQuantity(quantity);
        return foodItemDTO;
    }

    public static Restaurant createRestaurant(int id, String name, String address, String city, String description) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setCity(city);
        restaurant.setRestaurantDescription(description);
        return restaurant;
    }

    public static FoodCataloguePage createFoodCataloguePage(List<FoodItem> foodItems, Restaurant restaurant) {
        FoodCataloguePage page = new FoodCataloguePage();
        page.setFoodItemsList(foodItems);
        page.setRestaurant(restaurant);
        return page;
    }

    // Predefined test data
    public static FoodItem getDefaultPizza() {
        return createFoodItem(1, "Pizza", "Delicious cheese pizza", true, "15.99", 1, 10);
    }

    public static FoodItem getDefaultBurger() {
        return createFoodItem(2, "Burger", "Tasty beef burger", false, "12.99", 1, 5);
    }

    public static FoodItemDTO getDefaultPizzaDTO() {
        return createFoodItemDTO(1, "Pizza", "Delicious cheese pizza", true, "15.99", 1, 10);
    }

    public static FoodItemDTO getDefaultBurgerDTO() {
        return createFoodItemDTO(2, "Burger", "Tasty beef burger", false, "12.99", 1, 5);
    }

    public static Restaurant getDefaultRestaurant() {
        return createRestaurant(1, "Test Restaurant", "123 Test St", "Test City", "A test restaurant");
    }

    public static FoodCataloguePage getDefaultFoodCataloguePage() {
        List<FoodItem> foodItems = Arrays.asList(getDefaultPizza(), getDefaultBurger());
        return createFoodCataloguePage(foodItems, getDefaultRestaurant());
    }

    // Builder pattern for more complex test data
    public static class FoodItemBuilder {
        private FoodItem foodItem = new FoodItem();

        public FoodItemBuilder withId(int id) {
            foodItem.setId(id);
            return this;
        }

        public FoodItemBuilder withName(String name) {
            foodItem.setItemName(name);
            return this;
        }

        public FoodItemBuilder withDescription(String description) {
            foodItem.setItemDescription(description);
            return this;
        }

        public FoodItemBuilder withVeg(boolean isVeg) {
            foodItem.setVeg(isVeg);
            return this;
        }

        public FoodItemBuilder withPrice(String price) {
            foodItem.setPrice(new BigDecimal(price));
            return this;
        }

        public FoodItemBuilder withRestaurantId(Integer restaurantId) {
            foodItem.setRestaurantId(restaurantId);
            return this;
        }

        public FoodItemBuilder withQuantity(Integer quantity) {
            foodItem.setQuantity(quantity);
            return this;
        }

        public FoodItem build() {
            return foodItem;
        }
    }

    public static class RestaurantBuilder {
        private Restaurant restaurant = new Restaurant();

        public RestaurantBuilder withId(int id) {
            restaurant.setId(id);
            return this;
        }

        public RestaurantBuilder withName(String name) {
            restaurant.setName(name);
            return this;
        }

        public RestaurantBuilder withAddress(String address) {
            restaurant.setAddress(address);
            return this;
        }

        public RestaurantBuilder withCity(String city) {
            restaurant.setCity(city);
            return this;
        }

        public RestaurantBuilder withDescription(String description) {
            restaurant.setRestaurantDescription(description);
            return this;
        }

        public Restaurant build() {
            return restaurant;
        }
    }

    public static FoodItemBuilder foodItem() {
        return new FoodItemBuilder();
    }

    public static RestaurantBuilder restaurant() {
        return new RestaurantBuilder();
    }
}