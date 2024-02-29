package com.thebookcooper.dao;

import com.thebookcooper.model.PointTransaction;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;

public class PointTransactionDAO extends DataAccessObject<PointTransaction> {

    private static final String INSERT = "INSERT INTO point_transactions (user_id, transaction_type, amount, transaction_date, current_balance) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
    private static final String GET_ONE = "SELECT * FROM point_transactions WHERE bb_transaction_id=?";
    private static final String UPDATE = "UPDATE point_transactions SET user_id=?, transaction_type=?, amount=?, transaction_date=CURRENT_TIMESTAMP, current_balance=? WHERE bb_transaction_id=?";
    private static final String DELETE = "DELETE FROM point_transactions WHERE bb_transaction_id=?";

    public PointTransactionDAO(Connection connection) {
        super(connection);
    }

    @Override
    public PointTransaction findById(long id) {
        PointTransaction transaction = new PointTransaction();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                transaction.setBbTransactionId(rs.getLong("bb_transaction_id"));
                transaction.setUserId(rs.getLong("user_id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transaction.setCurrentBalance(rs.getBigDecimal("current_balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return transaction;
    }

    @Override
    public PointTransaction create(PointTransaction dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, dto.getUserId());
            statement.setString(2, dto.getTransactionType());
            statement.setBigDecimal(3, dto.getAmount());
            statement.setBigDecimal(4, dto.getCurrentBalance());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating point transaction failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setBbTransactionId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating point transaction failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public PointTransaction update(PointTransaction dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setLong(1, dto.getUserId());
            statement.setString(2, dto.getTransactionType());
            statement.setBigDecimal(3, dto.getAmount());
            statement.setBigDecimal(4, dto.getCurrentBalance());
            statement.setLong(5, dto.getBbTransactionId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating point transaction failed, no rows affected.");
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
                throw new SQLException("Deleting point transaction failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
