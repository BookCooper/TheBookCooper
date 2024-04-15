package com.thebookcooper;

import com.thebookcooper.model.Book;
import com.thebookcooper.dao.BookInfoDAO;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.List;

public class BookInfoDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BookInfoDAO bookInfoDAO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);  // Adjust based on actual result expectations
    }

    @Test
    void findByIdTest() throws SQLException {
        when(resultSet.getLong("book_id")).thenReturn(1L);
        when(resultSet.getDouble("price")).thenReturn(19.99);

        Book foundBook = bookInfoDAO.findById(1L);

        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getBookId());
        assertEquals(19.99, foundBook.getPrice());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createBookTest() throws SQLException {
        Book book = new Book();
        book.setTitle("New Book");
        book.setISBN(1234567890123L);
        book.setAuthor("Author");
        book.setGenre("Genre");
        book.setBookCondition("New");
        book.setPrice(29.99);

        when(resultSet.getLong(1)).thenReturn(1L);

        Book createdBook = bookInfoDAO.create(book);

        assertNotNull(createdBook);
        assertEquals(1L, createdBook.getBookId());
        verify(preparedStatement).setString(1, book.getTitle());
        verify(preparedStatement).setLong(2, book.getISBN());
        verify(preparedStatement).setDate(3, book.getPublishDate());
        verify(preparedStatement).setString(4, book.getAuthor());
        verify(preparedStatement).setString(5, book.getGenre());
        verify(preparedStatement).setString(6, book.getBookCondition());
        verify(preparedStatement).setDouble(7, book.getPrice());
    }

    @Test
    void updateBookTest() throws SQLException {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Updated Book");
        book.setPrice(39.99);

        Book updatedBook = bookInfoDAO.update(book);

        assertNotNull(updatedBook);
        verify(preparedStatement).setString(1, book.getTitle());
        verify(preparedStatement).setLong(2, book.getISBN());
        verify(preparedStatement).setDate(3, book.getPublishDate());
        verify(preparedStatement).setString(4, book.getAuthor());
        verify(preparedStatement).setString(5, book.getGenre());
        verify(preparedStatement).setString(6, book.getBookCondition());
        verify(preparedStatement).setDouble(7, book.getPrice());
        verify(preparedStatement).setLong(8, book.getBookId());
    }

    @Test
    void deleteBookTest() throws SQLException {
        bookInfoDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void findByTitleTest() throws SQLException {
        when(resultSet.getLong("book_id")).thenReturn(1L);
        when(resultSet.getString("title")).thenReturn("Book");

        List<Book> books = bookInfoDAO.findByTitle("Book");

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals("Book", books.get(0).getTitle());
        verify(preparedStatement).setString(1, "%Book%");
    }
}
