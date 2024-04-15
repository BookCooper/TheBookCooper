package com.thebookcooper.dao;

import com.thebookcooper.model.User;
import com.thebookcooper.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDAO extends DataAccessObject<User> {

    private static final String GET_ONE = "SELECT user_id, user_name, email, b_bucks_balance, creation_date, last_login " +
            "FROM users WHERE user_id=?";
    
    private static final String GET_ONE_EMAIL = "SELECT user_id, user_name, password, email, b_bucks_balance, creation_date, last_login " +
            "FROM users WHERE email=?";

    private static final String INSERT = "INSERT INTO users (user_name, email, b_bucks_balance, creation_date, last_login) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE users SET user_name=?, email=?, b_bucks_balance=?, last_login=? WHERE user_id=?";

    private static final String DELETE = "DELETE FROM users WHERE user_id=?";

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public User findById(long id) {
        User user = new User();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setUserId(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
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

    public User findByEmail(String email) {
        User user = new User();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_EMAIL)) {
            statement.setString(1, email);
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
            statement.setString(2, dto.getEmail());
            statement.setDouble(3, dto.getBBucksBalance());
            statement.setTimestamp(4, dto.getCreationDate());
            statement.setTimestamp(5, dto.getLastLogin());

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

    @Override
    public User update(User dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getUserName());
            statement.setString(2, dto.getEmail());
            statement.setDouble(3, dto.getBBucksBalance());
            statement.setTimestamp(4, dto.getLastLogin());
            statement.setLong(5, dto.getUserId());
            

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}





