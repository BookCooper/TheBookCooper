package com.thebookcooper.dao;

import com.thebookcooper.model.BookSearch;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;

public class BookSearchDAO extends DataAccessObject<BookSearch> {

    private static final String INSERT = "INSERT INTO book_searches (user_id, search_query, search_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
    private static final String GET_ONE = "SELECT search_id, user_id, search_query, search_date FROM book_searches WHERE search_id=?";
    private static final String UPDATE = "UPDATE book_searches SET user_id=?, search_query=?, search_date=CURRENT_TIMESTAMP WHERE search_id=?";
    private static final String DELETE = "DELETE FROM book_searches WHERE search_id=?";

    public BookSearchDAO(Connection connection) {
        super(connection);
    }

    @Override
    public BookSearch findById(long id) {
        BookSearch search = new BookSearch();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                search.setSearchId(rs.getLong("search_id"));
                search.setUserId(rs.getLong("user_id"));
                search.setSearchQuery(rs.getString("search_query"));
                search.setSearchDate(rs.getTimestamp("search_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return search;
    }

    @Override
    public BookSearch create(BookSearch dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, dto.getUserId());
            statement.setString(2, dto.getSearchQuery());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book search failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setSearchId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating book search failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookSearch update(BookSearch dto) {

        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setLong(1, dto.getUserId());
            statement.setString(2, dto.getSearchQuery());
            statement.setLong(3, dto.getSearchId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating book search failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dto;
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting book search failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
