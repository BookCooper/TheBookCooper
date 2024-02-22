package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.dao.DatabaseConnectionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Map;

import com.thebookcooper.model.*;
import com.thebookcooper.dao.*;

import java.sql.Timestamp;

@RestController
public class ListingsController {

    @PostMapping("/listings/create")
    public Listing createListing(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

        Listing newListing = new Listing();

        try {
            Connection connection = dcm.getConnection();
            ListingsDAO listDAO = new ListingsDAO(connection);

            newListing.setUserId((long) inputMap.get("userId"));
            newListing.setBookId((long) inputMap.get("bookId"));
            newListing.setListingStatus((String) inputMap.get("listingStatus"));
            newListing.setListingDate(new Timestamp(System.currentTimeMillis()));
            

            //calls create function from dao/ListingsDAO to insert listing
            return listDAO.create(newListing);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the listing", e);
        }
    }
}
