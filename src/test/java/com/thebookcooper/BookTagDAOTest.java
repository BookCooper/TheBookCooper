package com.thebookcooper;

import com.thebookcooper.dao.BookTagDAO;
import com.thebookcooper.model.BookTag;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class BookTagDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BookTagDAO bookTagDAO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
    }

    @Test
    void findByIdTest() throws SQLException {
        when(resultSet.getLong("tag_id")).thenReturn(1L);
        when(resultSet.getString("tag_name")).thenReturn("Fiction");
        when(resultSet.getLong("book_id")).thenReturn(100L);

        BookTag foundTag = bookTagDAO.findById(1L);

        assertNotNull(foundTag);
        assertEquals(1L, foundTag.getTagId());
        assertEquals("Fiction", foundTag.getTagName());
        assertEquals(100L, foundTag.getBookId());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createTagTest() throws SQLException {
        BookTag tag = new BookTag();
        tag.setTagName("Fiction");
        tag.setBookId(100L);

        when(resultSet.getLong(1)).thenReturn(1L);

        BookTag createdTag = bookTagDAO.create(tag);

        assertNotNull(createdTag);
        assertEquals(1L, createdTag.getTagId());
        verify(preparedStatement).setString(1, tag.getTagName());
        verify(preparedStatement).setLong(2, tag.getBookId());
    }

    @Test
    void updateTagTest() throws SQLException {
        BookTag tag = new BookTag();
        tag.setTagId(1L);
        tag.setTagName("Non-Fiction");
        tag.setBookId(101L);

        BookTag updatedTag = bookTagDAO.update(tag);

        assertNotNull(updatedTag);
        verify(preparedStatement).setString(1, tag.getTagName());
        verify(preparedStatement).setLong(2, tag.getBookId());
        verify(preparedStatement).setLong(3, tag.getTagId());
    }

    @Test
    void deleteTagTest() throws SQLException {
        bookTagDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
