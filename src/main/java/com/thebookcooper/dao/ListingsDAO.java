package com.thebookcooper.dao;

import com.thebookcooper.model.*;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;


public class ListingsDAO extends DataAccessObject<Listing> {

    private static final String GET_ONE = "SELECT listing_id, user_id, book_id, listing_status, listing_date " +
            "FROM book_listings WHERE listing_id=?";

    private static final String INSERT = "INSERT INTO book_listings (user_id, book_id, listing_status, listing_date) " +
            "VALUES (?, ?, ?, ?)";

    public ListingsDAO(Connection connection) {
        super(connection);
    }


    @Override
    public Listing findById(long id) {
        Listing listing = new Listing();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                
                listing.setListingId(rs.getLong("listing_id"));
                listing.setUserId(rs.getLong("user_id"));
                listing.setBookId(rs.getLong("book_id"));
                listing.setListingStatus(rs.getString("listing_status"));
                listing.setListingDate(rs.getTimestamp("listing_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listing;
    }

    @Override
    public Listing create(Listing dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setLong(1, dto.getUserId());
            statement.setLong(2, dto.getBookId());
            statement.setString(3, dto.getListingStatus());
            statement.setTimestamp(4, dto.getListingDate());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating listing failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setListingId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating listing failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}





