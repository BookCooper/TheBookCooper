package com.thebookcooper;

import com.thebookcooper.model.BookSearch;
import com.thebookcooper.dao.BookSearchDAO;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class BookSearchDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BookSearchDAO bookSearchDAO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
    }

    @Test
    void findByIdTest() throws SQLException {
        when(resultSet.getLong("search_id")).thenReturn(1L);
        when(resultSet.getString("search_query")).thenReturn("Mystery");

        BookSearch foundSearch = bookSearchDAO.findById(1L);

        assertNotNull(foundSearch);
        assertEquals(1L, foundSearch.getSearchId());
        assertEquals("Mystery", foundSearch.getSearchQuery());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createSearchTest() throws SQLException {
        BookSearch search = new BookSearch();
        search.setUserId(1L);
        search.setSearchQuery("Fiction");

        when(resultSet.getLong(1)).thenReturn(1L);

        BookSearch createdSearch = bookSearchDAO.create(search);

        assertNotNull(createdSearch);
        assertEquals(1L, createdSearch.getSearchId());
        verify(preparedStatement).setLong(1, search.getUserId());
        verify(preparedStatement).setString(2, search.getSearchQuery());
    }

    @Test
    void updateSearchTest() throws SQLException {
        BookSearch search = new BookSearch();
        search.setSearchId(1L);
        search.setUserId(1L);
        search.setSearchQuery("Updated Query");

        BookSearch updatedSearch = bookSearchDAO.update(search);

        assertNotNull(updatedSearch);
        verify(preparedStatement).setLong(1, search.getUserId());
        verify(preparedStatement).setString(2, search.getSearchQuery());
        verify(preparedStatement).setLong(3, search.getSearchId());
    }

    @Test
    void deleteSearchTest() throws SQLException {
        bookSearchDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
