package com.bacuti.repository;

import com.bacuti.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Find an item by its name.
     *
     * @param itemName - Item to search.
     * @return - Returns list of Item.
     */
    Item findByItemNameIgnoreCase(String itemName);

    // Query to fetch distinct item categories from the item table
    String ITEM_CATEGORY_QUERY = "SELECT DISTINCT item_category FROM item";
    @Modifying
    @Query(value = ITEM_CATEGORY_QUERY, nativeQuery = true)
    List<String> fetchDistinctItemCategory();

    /**
     * Finds all items by their category with pagination support.
     *
     * @param itemCategory - Category of item to find.
     * @param pageable - Pagination info.
     * @return - Returns item in the specific category
     */
    Page<Item> findAllByItemCategory(String itemCategory, Pageable pageable);

    /**
     * Finds items by their name with pagination support.
     */
    String ITEM_NAME_QUERY = "SELECT * FROM item WHERE item_name LIKE %:name%";
    @Query(value = ITEM_NAME_QUERY, nativeQuery = true)
    Page<Item> findItemByItemName(@Param("name") String name, Pageable pageable);

    /**
     * Finds whether item exists with the given name
     * @param itemName
     * @return if exists , true else false
     */
    boolean existsByItemName(String itemName);

    /**
     * Find item by name in case insensitive mode
     * @param name search string
     * @param pageable sorting and paging variables
     * @return List 0f items returned based on the name is returnd
     */
    @Query("SELECT u FROM Item u WHERE LOWER(u.itemName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Item> findByItemNameLikeIgnoreCase(@Param("name") String name, Pageable pageable);

    /**
     * Find items by itemname and itemcategroy
     * @param itemName item name
     * @param itemCategories item categories in list
     * @param pageable sorting and paging variables
     * @return List of items returned based on the item name and item category.
     */
    @Query("SELECT i FROM Item i WHERE LOWER(i.itemName) LIKE LOWER(CONCAT('%', :itemName, '%')) AND i.itemCategory IN :itemCategories")
    Page<Item> findByItemNameContainingIgnoreCaseAndItemCategoryIn(@Param("itemName") String itemName, @Param("itemCategories") List<String> itemCategories, Pageable pageable);

    /**
     * Find item based on category list
     * @param itemCategories List of item categories to be used as filter condition
     * @param pageable sorting and paging variables
     * @return List of items filtered with category condition.
     */
    Page<Item> findByItemCategoryIn(List<String> itemCategories, Pageable pageable);

    /**
     * Method to get the id of an item by its name.
     */
    String GET_ITEM_ID_BY_NAME_QUERY = "SELECT id FROM item WHERE item_name = :itemName";
    @Query(value = GET_ITEM_ID_BY_NAME_QUERY, nativeQuery = true)
    Long getIdByItemName(@Param("itemName") String itemName);

    @Query("SELECT s.itemName FROM Item s")
    List<String> getAllItemName();

}
