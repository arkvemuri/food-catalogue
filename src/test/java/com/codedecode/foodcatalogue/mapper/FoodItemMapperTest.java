package com.codedecode.foodcatalogue.mapper;

import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.entity.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemMapperTest {

    private FoodItem foodItem;
    private FoodItemDTO foodItemDTO;

    @BeforeEach
    void setUp() {
        // Setup FoodItem entity
        foodItem = new FoodItem();
        foodItem.setId(1);
        foodItem.setItemName("Pizza");
        foodItem.setItemDescription("Delicious cheese pizza");
        foodItem.setVeg(true);
        foodItem.setPrice(new BigDecimal("15.99"));
        foodItem.setRestaurantId(1);
        foodItem.setQuantity(10);

        // Setup FoodItemDTO
        foodItemDTO = new FoodItemDTO();
        foodItemDTO.setId(1);
        foodItemDTO.setItemName("Pizza");
        foodItemDTO.setItemDescription("Delicious cheese pizza");
        foodItemDTO.setVeg(true);
        foodItemDTO.setPrice(new BigDecimal("15.99"));
        foodItemDTO.setRestaurantId(1);
        foodItemDTO.setQuantity(10);
    }

    @Test
    void testMapFoodItemToFoodItemDTO_Success() {
        // When
        FoodItemDTO result = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(foodItem);

        // Then
        assertNotNull(result);
        assertEquals(foodItem.getId(), result.getId());
        assertEquals(foodItem.getItemName(), result.getItemName());
        assertEquals(foodItem.getItemDescription(), result.getItemDescription());
        assertEquals(foodItem.isVeg(), result.isVeg());
        assertEquals(foodItem.getPrice(), result.getPrice());
        assertEquals(foodItem.getRestaurantId(), result.getRestaurantId());
        assertEquals(foodItem.getQuantity(), result.getQuantity());
    }

    @Test
    void testMapFoodItemDTOToFoodItem_Success() {
        // When
        FoodItem result = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO);

        // Then
        assertNotNull(result);
        assertEquals(foodItemDTO.getId(), result.getId());
        assertEquals(foodItemDTO.getItemName(), result.getItemName());
        assertEquals(foodItemDTO.getItemDescription(), result.getItemDescription());
        assertEquals(foodItemDTO.isVeg(), result.isVeg());
        assertEquals(foodItemDTO.getPrice(), result.getPrice());
        assertEquals(foodItemDTO.getRestaurantId(), result.getRestaurantId());
        assertEquals(foodItemDTO.getQuantity(), result.getQuantity());
    }

    @Test
    void testMapFoodItemToFoodItemDTO_WithNullInput() {
        // When
        FoodItemDTO result = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(null);

        // Then
        assertNull(result);
    }

    @Test
    void testMapFoodItemDTOToFoodItem_WithNullInput() {
        // When
        FoodItem result = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(null);

        // Then
        assertNull(result);
    }

    @Test
    void testMapFoodItemToFoodItemDTO_WithNullFields() {
        // Given
        FoodItem itemWithNulls = new FoodItem();
        itemWithNulls.setId(1);
        itemWithNulls.setItemName(null);
        itemWithNulls.setItemDescription(null);
        itemWithNulls.setVeg(false);
        itemWithNulls.setPrice(null);
        itemWithNulls.setRestaurantId(null);
        itemWithNulls.setQuantity(null);

        // When
        FoodItemDTO result = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(itemWithNulls);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getItemName());
        assertNull(result.getItemDescription());
        assertFalse(result.isVeg());
        assertNull(result.getPrice());
        assertNull(result.getRestaurantId());
        assertNull(result.getQuantity());
    }

    @Test
    void testMapFoodItemDTOToFoodItem_WithNullFields() {
        // Given
        FoodItemDTO dtoWithNulls = new FoodItemDTO();
        dtoWithNulls.setId(1);
        dtoWithNulls.setItemName(null);
        dtoWithNulls.setItemDescription(null);
        dtoWithNulls.setVeg(false);
        dtoWithNulls.setPrice(null);
        dtoWithNulls.setRestaurantId(null);
        dtoWithNulls.setQuantity(null);

        // When
        FoodItem result = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(dtoWithNulls);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getItemName());
        assertNull(result.getItemDescription());
        assertFalse(result.isVeg());
        assertNull(result.getPrice());
        assertNull(result.getRestaurantId());
        assertNull(result.getQuantity());
    }

    @Test
    void testBidirectionalMapping_Consistency() {
        // When - map entity to DTO and back to entity
        FoodItemDTO mappedDTO = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(foodItem);
        FoodItem mappedBackToEntity = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(mappedDTO);

        // Then - original entity and mapped back entity should be equal
        assertEquals(foodItem.getId(), mappedBackToEntity.getId());
        assertEquals(foodItem.getItemName(), mappedBackToEntity.getItemName());
        assertEquals(foodItem.getItemDescription(), mappedBackToEntity.getItemDescription());
        assertEquals(foodItem.isVeg(), mappedBackToEntity.isVeg());
        assertEquals(foodItem.getPrice(), mappedBackToEntity.getPrice());
        assertEquals(foodItem.getRestaurantId(), mappedBackToEntity.getRestaurantId());
        assertEquals(foodItem.getQuantity(), mappedBackToEntity.getQuantity());
    }

    @Test
    void testBidirectionalMapping_DTOFirst() {
        // When - map DTO to entity and back to DTO
        FoodItem mappedEntity = FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO);
        FoodItemDTO mappedBackToDTO = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(mappedEntity);

        // Then - original DTO and mapped back DTO should be equal
        assertEquals(foodItemDTO.getId(), mappedBackToDTO.getId());
        assertEquals(foodItemDTO.getItemName(), mappedBackToDTO.getItemName());
        assertEquals(foodItemDTO.getItemDescription(), mappedBackToDTO.getItemDescription());
        assertEquals(foodItemDTO.isVeg(), mappedBackToDTO.isVeg());
        assertEquals(foodItemDTO.getPrice(), mappedBackToDTO.getPrice());
        assertEquals(foodItemDTO.getRestaurantId(), mappedBackToDTO.getRestaurantId());
        assertEquals(foodItemDTO.getQuantity(), mappedBackToDTO.getQuantity());
    }

    @Test
    void testMapperInstance_NotNull() {
        // Then
        assertNotNull(FoodItemMapper.INSTANCE);
    }

    @Test
    void testMapperInstance_Singleton() {
        // When
        FoodItemMapper instance1 = FoodItemMapper.INSTANCE;
        FoodItemMapper instance2 = FoodItemMapper.INSTANCE;

        // Then
        assertSame(instance1, instance2);
    }

    @Test
    void testMapping_WithDifferentPriceTypes() {
        // Given
        FoodItem itemWithIntPrice = new FoodItem();
        itemWithIntPrice.setId(1);
        itemWithIntPrice.setItemName("Test Item");
        itemWithIntPrice.setPrice(15); // Integer price

        // When
        FoodItemDTO result = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(itemWithIntPrice);

        // Then
        assertNotNull(result);
        assertEquals(15, result.getPrice());
    }

    @Test
    void testMapping_WithZeroValues() {
        // Given
        FoodItem itemWithZeros = new FoodItem();
        itemWithZeros.setId(0);
        itemWithZeros.setItemName("");
        itemWithZeros.setItemDescription("");
        itemWithZeros.setVeg(false);
        itemWithZeros.setPrice(BigDecimal.ZERO);
        itemWithZeros.setRestaurantId(0);
        itemWithZeros.setQuantity(0);

        // When
        FoodItemDTO result = FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDTO(itemWithZeros);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getId());
        assertEquals("", result.getItemName());
        assertEquals("", result.getItemDescription());
        assertFalse(result.isVeg());
        assertEquals(BigDecimal.ZERO, result.getPrice());
        assertEquals(0, result.getRestaurantId());
        assertEquals(0, result.getQuantity());
    }
}