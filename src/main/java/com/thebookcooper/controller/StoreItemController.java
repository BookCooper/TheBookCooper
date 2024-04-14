package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.thebookcooper.model.StoreItem;
import com.thebookcooper.dao.StoreItemDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@CrossOrigin
@RequestMapping("/store-items")
public class StoreItemController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @PostMapping("/create")
    public ResponseEntity<?> createStoreItem(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            StoreItem newItem = new StoreItem();
            Connection connection = dcm.getConnection();
            StoreItemDAO itemDAO = new StoreItemDAO(connection);

            newItem.setItem((String) inputMap.get("item"));
            newItem.setItemPrice(new java.math.BigDecimal(inputMap.get("item_price").toString()));
            newItem.setSpecialOffer((String) inputMap.get("special_offer"));
            newItem.setItemDescription((String) inputMap.get("item_description"));

            StoreItem createdItem = itemDAO.create(newItem);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create store item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreItemById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            StoreItemDAO itemDAO = new StoreItemDAO(connection);
            StoreItem item = itemDAO.findById(id);
            if (item != null) {
                return new ResponseEntity<>(item, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Store item not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving store item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStoreItem(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            StoreItemDAO itemDAO = new StoreItemDAO(connection);

            StoreItem itemToUpdate = new StoreItem();
            itemToUpdate.setStoreId(id);
            itemToUpdate.setItem((String) inputMap.get("item"));
            itemToUpdate.setItemPrice(new java.math.BigDecimal(inputMap.get("item_price").toString()));
            itemToUpdate.setSpecialOffer((String) inputMap.get("special_offer"));
            itemToUpdate.setItemDescription((String) inputMap.get("item_description"));

            StoreItem updatedItem = itemDAO.update(itemToUpdate);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update store item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStoreItem(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            StoreItemDAO itemDAO = new StoreItemDAO(connection);
            itemDAO.delete(id);
            return new ResponseEntity<>("Store item deleted successfully", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete store item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
