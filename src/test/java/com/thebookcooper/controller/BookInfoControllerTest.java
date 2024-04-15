import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.thebookcooper.controller.BookInfoController;
import com.thebookcooper.dao.DatabaseConnectionManager;


@ExtendWith(MockitoExtension.class)
public class BookInfoControllerTest {

    @Mock
    private DatabaseConnectionManager dcm;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BookInfoController controller;

    @BeforeEach
    void setUp() throws Exception {
        when(dcm.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    void testCountBooks_Success() throws SQLException {
        when(statement.executeQuery("SELECT COUNT(*) FROM book_info")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        ResponseEntity<?> response = controller.countBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Number of books: 10", response.getBody());
    }

    @Test
    void testGetBookById_Success() throws SQLException {
        long bookId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(bookId);

        ResponseEntity<?> response = controller.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSearchBooksByTitle_Success() throws SQLException {
        String title = "Effective Java";
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        ResponseEntity<?> response = controller.searchBooksByTitle(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateBook_Success() throws SQLException, JsonProcessingException {
        String json = "{\"title\":\"Clean Code\",\"publishDate\":\"2021-04-01\",\"author\":\"Robert C. Martin\",\"genre\":\"Programming\",\"bookCondition\":\"new\",\"price\":45.00}";
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        ResponseEntity<?> response = controller.createBook(json);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateBook_Success() throws SQLException, JsonProcessingException {
        long bookId = 1;
        String json = "{\"title\":\"Updated Title\",\"publishDate\":\"2022-01-01\",\"author\":\"New Author\",\"genre\":\"New Genre\",\"bookCondition\":\"used\",\"price\":55.00}";
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ResponseEntity<?> response = controller.updateBook(bookId, json);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteBook_Success() throws SQLException {
        long bookId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ResponseEntity<?> response = controller.deleteBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book with ID " + bookId + " deleted successfully", response.getBody());
    }
}

