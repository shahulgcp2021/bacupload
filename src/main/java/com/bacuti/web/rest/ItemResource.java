package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.domain.enumeration.ItemType;
import com.bacuti.service.ItemService;
import com.bacuti.service.dto.ItemDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bacuti.domain.Item}.
 */
@RestController
@RequestMapping("/api/${app.api.version}/items")
@Transactional
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "uploadServiceItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemService itemService;

    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * {@code POST  /items} : Create or update an item.
     *
     * @param item the item to create or update.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new item, or with status {@code 400 (Bad Request)} if the item has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO item) throws URISyntaxException {
        log.debug("REST request to save Item");
        if (item.getId() != null) {
            throw new BusinessException("A new site cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        item = itemService.saveItem(item);
        return ResponseEntity.created(new URI("/api/items/" + item.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, item.getId().toString()))
            .body(item);
    }

    /**
     * {@code PUT  /items/:id} : Updates an existing item.
     *
     * @param item the item to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated item,
     * or with status {@code 400 (Bad Request)} if the item is not valid,
     * or with status {@code 500 (Internal Server Error)} if the item couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable(value = "id") final Long id, @Valid @RequestBody ItemDTO item) throws URISyntaxException {
        log.debug("REST request to update Item");
        if (item.getId() == null || !Objects.equals(id, item.getId())) {
            throw new BusinessException("Invalid id", HttpStatus.NOT_FOUND.value());
        }
        item = itemService.saveItem(item);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, item.getId().toString()))
            .body(item);
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" item.
     *
     * @param id the id of the item to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete Item");
        itemService.deleteItemById(id);
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString());
        return ResponseEntity.ok().body(true);
    }

    /**
     * {@code GET  /item-category} : get all the distinct item categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/item-category")
    public ResponseEntity<List<String>> getItemCategory() {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemService.getItemCategory()));
    }

    /**
     * {@code GET  /items/name} : get the "name" item.
     *
     * @param itemname the name of the item to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the item, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("")
    public ResponseEntity<Page<ItemDTO>> getItem(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(required = false, defaultValue = "lastModifiedDate")String sortBy,
                                                   @RequestParam(required = false, defaultValue = "desc")String sortDirection,
                                                   @RequestParam(defaultValue = "") String itemname,
                                                   @RequestParam(required = false) List<String> itemCategories) {
        log.debug("REST request to get Item : {}", itemname);
        Page<ItemDTO> items = itemService.getItems(page, size, sortBy, sortDirection, itemname, itemCategories);
        return ResponseEntity.ok(items);
    }

    /**
     * {@code GET  /items/Type} : get the "itemType" .
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type")
    public ResponseEntity<List<ItemType>> getItemByItemType() {
        return ResponseUtil.wrapOrNotFound(Optional.of(Arrays.stream(ItemType.values()).toList()));
    }

    /**
     * {@code GET  /all-items} : get all item name from Item entity
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/all-items")
    public ResponseEntity<List<String>> getAllItemName() {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemService.getAllItemName()));
    }
}
