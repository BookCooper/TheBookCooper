package com.thebookcooper.dao;

import com.thebookcooper.model.StoreItem;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;

public class StoreItemDAO extends DataAccessObject<StoreItem> {
    private static final String INSERT = "INSERT INTO store_item (item, item_price, special_offer, item_description) VALUES (?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT * FROM store_item WHERE store_id=?";
    private static final String UPDATE = "UPDATE store_item SET item=?, item_price=?, special_offer=?, item_description=? WHERE store_id=?";
    private static final String DELETE = "DELETE FROM store_item WHERE store_id=?";

    public StoreItemDAO(Connection connection) { super(connection); }

    @Override
    public StoreItem findById(long id) {
        StoreItem item = new StoreItem();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                item.setStoreId(rs.getLong("store_id"));
                item.setItem(rs.getString("item"));
                item.setItemPrice(rs.getBigDecimal("item_price"));
                item.setSpecialOffer(rs.getString("special_offer"));
                item.setItemDescription(rs.getString("item_description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return item;
    }

    @Override
    public StoreItem create(StoreItem dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getItem());
            statement.setBigDecimal(2, dto.getItemPrice());
            statement.setString(3, dto.getSpecialOffer());
            statement.setString(4, dto.getItemDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating store item failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setStoreId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating store item failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public StoreItem update(StoreItem dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setString(1, dto.getItem());
            statement.setBigDecimal(2, dto.getItemPrice());
            statement.setString(3, dto.getSpecialOffer());
            statement.setString(4, dto.getItemDescription());
            statement.setLong(5, dto.getStoreId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating store item failed, no rows affected.");
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting store item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
