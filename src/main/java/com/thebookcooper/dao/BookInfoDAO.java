package com.thebookcooper.dao;

import com.thebookcooper.model.*;
import com.thebookcooper.util.DataAccessObject;

import java.sql.*;


public class BookInfoDAO extends DataAccessObject<Book> {

    private static final String GET_ONE = "SELECT book_id, title, isbn, publish_date, author, genre, book_condition, price " +
            "FROM book_info WHERE book_id=?";

    private static final String INSERT = "INSERT INTO book_info (title, isbn, publish_date, author, genre, book_condition, price) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                book.setBookId(rs.getLong("book_id"));
                book.setTitle(rs.getString("title"));
                book.setISBN(rs.getLong("isbn"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setBookCondition(rs.getString("book_condition"));
                book.setPrice(rs.getDouble("price")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public Book create(Book dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getTitle());
            statement.setLong(2, dto.getISBN());
            statement.setDate(3, dto.getPublishDate());
            statement.setString(4, dto.getAuthor());
            statement.setString(5, dto.getGenre());
            statement.setString(6, dto.getBookCondition());
            statement.setDouble(7, dto.getPrice());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setBookId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}





