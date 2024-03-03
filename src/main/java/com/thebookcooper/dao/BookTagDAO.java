package com.thebookcooper.dao;

import com.thebookcooper.model.BookTag;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;

public class BookTagDAO extends DataAccessObject<BookTag> {

    private static final String GET_ONE = "SELECT tag_id, tag_name, book_id FROM book_tags WHERE tag_id=?";
    private static final String INSERT = "INSERT INTO book_tags (tag_name, book_id) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE book_tags SET tag_name=?, book_id=? WHERE tag_id=?";
    private static final String DELETE = "DELETE FROM book_tags WHERE tag_id=?";

    public BookTagDAO(Connection connection) {
        super(connection);
    }

    @Override
    public BookTag findById(long id) {
        BookTag tag = new BookTag();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tag.setTagId(rs.getLong("tag_id"));
                tag.setTagName(rs.getString("tag_name"));
                tag.setBookId(rs.getLong("book_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tag;
    }

    @Override
    public BookTag create(BookTag dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getTagName());
            statement.setLong(2, dto.getBookId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating tag failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setTagId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating tag failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookTag update(BookTag dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setString(1, dto.getTagName());
            statement.setLong(2, dto.getBookId());
            statement.setLong(3, dto.getTagId());
            statement.executeUpdate();
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
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
