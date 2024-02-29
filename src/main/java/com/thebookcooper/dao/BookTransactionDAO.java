package com.thebookcooper.dao;

import com.thebookcooper.model.BookTransaction;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;

public class BookTransactionDAO extends DataAccessObject<BookTransaction> {
    private static final String INSERT = "INSERT INTO book_transactions (buyer_id, seller_id, listing_id, transaction_price, transaction_status) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT * FROM book_transactions WHERE transaction_id=?";
    private static final String UPDATE = "UPDATE book_transactions SET buyer_id=?, seller_id=?, listing_id=?, transaction_price=?, transaction_status=? WHERE transaction_id=?";
    private static final String DELETE = "DELETE FROM book_transactions WHERE transaction_id=?";

    public BookTransactionDAO(Connection connection) { super(connection); }

    @Override
    public BookTransaction findById(long id) {
        BookTransaction transaction = new BookTransaction();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                transaction.setTransactionId(rs.getLong("transaction_id"));
                transaction.setBuyerId(rs.getLong("buyer_id"));
                transaction.setSellerId(rs.getLong("seller_id"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transaction.setListingId(rs.getLong("listing_id"));
                transaction.setTransactionPrice(rs.getDouble("transaction_price"));
                transaction.setTransactionStatus(rs.getString("transaction_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return transaction;
    }

    @Override
    public BookTransaction create(BookTransaction dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, dto.getBuyerId());
            statement.setLong(2, dto.getSellerId());
            statement.setLong(3, dto.getListingId());
            statement.setDouble(4, dto.getTransactionPrice());
            statement.setString(5, dto.getTransactionStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setTransactionId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookTransaction update(BookTransaction dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setLong(1, dto.getBuyerId());
            statement.setLong(2, dto.getSellerId());
            statement.setLong(3, dto.getListingId());
            statement.setDouble(4, dto.getTransactionPrice());
            statement.setString(5, dto.getTransactionStatus());
            statement.setLong(6, dto.getTransactionId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating transaction failed, no rows affected.");
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
                throw new SQLException("Deleting transaction failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
