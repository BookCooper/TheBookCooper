package com.thebookcooper;

import com.thebookcooper.dao.PointTransactionDAO;
import com.thebookcooper.model.PointTransaction;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.sql.*;

public class PointTransactionDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private PointTransactionDAO pointTransactionDAO;

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
        when(resultSet.getLong("bb_transaction_id")).thenReturn(1L);
        when(resultSet.getBigDecimal("amount")).thenReturn(new BigDecimal("100.00"));

        PointTransaction transaction = pointTransactionDAO.findById(1L);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getBbTransactionId());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createTransactionTest() throws SQLException {
        PointTransaction transaction = new PointTransaction();
        transaction.setUserId(2L);
        transaction.setTransactionType("Credit");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setCurrentBalance(new BigDecimal("200.00"));

        when(resultSet.getLong(1)).thenReturn(1L);

        PointTransaction createdTransaction = pointTransactionDAO.create(transaction);

        assertNotNull(createdTransaction);
        assertEquals(1L, createdTransaction.getBbTransactionId());
        verify(preparedStatement).setLong(1, transaction.getUserId());
        verify(preparedStatement).setString(2, transaction.getTransactionType());
        verify(preparedStatement).setBigDecimal(3, transaction.getAmount());
        verify(preparedStatement).setBigDecimal(4, transaction.getCurrentBalance());
    }

    @Test
    void updateTransactionTest() throws SQLException {
        PointTransaction transaction = new PointTransaction();
        transaction.setBbTransactionId(1L);
        transaction.setUserId(2L);
        transaction.setTransactionType("Debit");
        transaction.setAmount(new BigDecimal("50.00"));
        transaction.setCurrentBalance(new BigDecimal("150.00"));

        PointTransaction updatedTransaction = pointTransactionDAO.update(transaction);

        assertNotNull(updatedTransaction);
        verify(preparedStatement).setLong(1, transaction.getUserId());
        verify(preparedStatement).setString(2, transaction.getTransactionType());
        verify(preparedStatement).setBigDecimal(3, transaction.getAmount());
        verify(preparedStatement).setBigDecimal(4, transaction.getCurrentBalance());
        verify(preparedStatement).setLong(5, transaction.getBbTransactionId());
    }

    @Test
    void deleteTransactionTest() throws SQLException {
        pointTransactionDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void fetchCurrentBalanceByUserIdTest() throws SQLException {
        when(resultSet.getBigDecimal("b_bucks_balance")).thenReturn(new BigDecimal("200.00"));

        BigDecimal balance = pointTransactionDAO.fetchCurrentBalanceByUserId(2L);

        assertNotNull(balance);
        assertEquals(new BigDecimal("200.00"), balance);
        verify(preparedStatement).setLong(1, 2L);
    }
}
