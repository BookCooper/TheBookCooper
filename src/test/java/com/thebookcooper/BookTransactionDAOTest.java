package com.thebookcooper;

import com.thebookcooper.model.BookTransaction;
import com.thebookcooper.dao.BookTransactionDAO;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class BookTransactionDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BookTransactionDAO bookTransactionDAO;

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
        when(resultSet.getLong("transaction_id")).thenReturn(1L);
        when(resultSet.getDouble("transaction_price")).thenReturn(100.00);

        BookTransaction transaction = bookTransactionDAO.findById(1L);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getTransactionId());
        assertEquals(100.00, transaction.getTransactionPrice());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createTransactionTest() throws SQLException {
        BookTransaction transaction = new BookTransaction();
        transaction.setBuyerId(1L);
        transaction.setSellerId(2L);
        transaction.setListingId(3L);
        transaction.setTransactionPrice(100.00);
        transaction.setTransactionStatus("Completed");

        when(resultSet.getLong(1)).thenReturn(1L);

        BookTransaction createdTransaction = bookTransactionDAO.create(transaction);

        assertNotNull(createdTransaction);
        assertEquals(1L, createdTransaction.getTransactionId());
        verify(preparedStatement).setLong(1, transaction.getBuyerId());
        verify(preparedStatement).setLong(2, transaction.getSellerId());
        verify(preparedStatement).setLong(3, transaction.getListingId());
        verify(preparedStatement).setDouble(4, transaction.getTransactionPrice());
        verify(preparedStatement).setString(5, transaction.getTransactionStatus());
    }

    @Test
    void updateTransactionTest() throws SQLException {
        BookTransaction transaction = new BookTransaction();
        transaction.setTransactionId(1L);
        transaction.setBuyerId(1L);
        transaction.setSellerId(2L);
        transaction.setListingId(3L);
        transaction.setTransactionPrice(120.00);
        transaction.setTransactionStatus("Pending");

        BookTransaction updatedTransaction = bookTransactionDAO.update(transaction);

        assertNotNull(updatedTransaction);
        verify(preparedStatement).setLong(1, transaction.getBuyerId());
        verify(preparedStatement).setLong(2, transaction.getSellerId());
        verify(preparedStatement).setLong(3, transaction.getListingId());
        verify(preparedStatement).setDouble(4, transaction.getTransactionPrice());
        verify(preparedStatement).setString(5, transaction.getTransactionStatus());
        verify(preparedStatement).setLong(6, transaction.getTransactionId());
    }

    @Test
    void deleteTransactionTest() throws SQLException {
        bookTransactionDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
