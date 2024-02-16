package com.thebookcooper.dao;

import com.thebookcooper.model.Book;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;


public class BookInfoDAO extends DataAccessObject<Book> {

    private static final String GET_ONE = "SELECT user_id, user_name, password, email, b_bucks_balance, creation_date, last_login " +
            "FROM users WHERE user_id=?";

    private static final String INSERT = "INSERT INTO users (user_name, password, email, b_bucks_balance, creation_date, last_login) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    public BookInfoDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Book findById(long id) {
        Book book = new Book();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setUserId(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setBBucksBalance(rs.getDouble("b_bucks_balance"));
                user.setCreationDate(rs.getTimestamp("creation_date"));
                user.setLastLogin(rs.getTimestamp("last_login"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User create(User dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getUserName());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getEmail());
            statement.setDouble(4, dto.getBBucksBalance());
            statement.setTimestamp(5, dto.getCreationDate());
            statement.setTimestamp(6, dto.getLastLogin());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setUserId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}





