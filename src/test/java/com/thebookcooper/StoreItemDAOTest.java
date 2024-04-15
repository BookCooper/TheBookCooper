package com.thebookcooper;

import com.thebookcooper.dao.StoreItemDAO;
import com.thebookcooper.model.StoreItem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.sql.*;

public class StoreItemDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private StoreItemDAO storeItemDAO;

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
        when(resultSet.getLong("store_id")).thenReturn(1L);
        when(resultSet.getString("item")).thenReturn("Notebook");
        when(resultSet.getBigDecimal("item_price")).thenReturn(new BigDecimal("10.50"));
        when(resultSet.getString("special_offer")).thenReturn("15% off");
        when(resultSet.getString("item_description")).thenReturn("A high-quality notebook");

        StoreItem foundItem = storeItemDAO.findById(1L);

        assertNotNull(foundItem);
        assertEquals(1L, foundItem.getStoreId());
        assertEquals("Notebook", foundItem.getItem());
        assertEquals(new BigDecimal("10.50"), foundItem.getItemPrice());
        assertEquals("15% off", foundItem.getSpecialOffer());
        assertEquals("A high-quality notebook", foundItem.getItemDescription());
    }

    @Test
    void createTest() throws SQLException {
        StoreItem newItem = new StoreItem();
        newItem.setItem("Notebook");
        newItem.setItemPrice(new BigDecimal("10.50"));
        newItem.setSpecialOffer("15% off");
        newItem.setItemDescription("A high-quality notebook");

        when(resultSet.getLong(1)).thenReturn(1L);

        StoreItem createdItem = storeItemDAO.create(newItem);

        assertNotNull(createdItem);
        assertEquals(1L, createdItem.getStoreId());
        verify(preparedStatement).setString(1, "Notebook");
        verify(preparedStatement).setBigDecimal(2, new BigDecimal("10.50"));
        verify(preparedStatement).setString(3, "15% off");
        verify(preparedStatement).setString(4, "A high-quality notebook");
    }

    @Test
    void updateTest() throws SQLException {
        StoreItem updateItem = new StoreItem();
        updateItem.setStoreId(1L);
        updateItem.setItem("Updated Notebook");
        updateItem.setItemPrice(new BigDecimal("12.00"));
        updateItem.setSpecialOffer("10% off");
        updateItem.setItemDescription("Updated description");

        StoreItem updatedItem = storeItemDAO.update(updateItem);

        assertNotNull(updatedItem);
        assertEquals("Updated Notebook", updatedItem.getItem());
        verify(preparedStatement).setString(1, "Updated Notebook");
        verify(preparedStatement).setBigDecimal(2, new BigDecimal("12.00"));
        verify(preparedStatement).setString(3, "10% off");
        verify(preparedStatement).setString(4, "Updated description");
        verify(preparedStatement).setLong(5, 1L);
    }

    @Test
    void deleteTest() throws SQLException {
        storeItemDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
