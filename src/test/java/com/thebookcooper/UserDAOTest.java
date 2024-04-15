package com.thebookcooper;

import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class UserDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);
    }

    @Test
    void findByIdTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getString("user_name")).thenReturn("JohnDoe");
        when(resultSet.getDouble("b_bucks_balance")).thenReturn(100.00);
        when(resultSet.getTimestamp("creation_date")).thenReturn(new Timestamp(System.currentTimeMillis()));

        User user = userDAO.findById(1L);
        assertNotNull(user);
        assertEquals(1L, user.getUserId());
        assertEquals("JohnDoe", user.getUserName());

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void createUserTest() throws SQLException {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        User user = new User();
        user.setUserName("JohnDoe");
        user.setPassword("pass123");
        user.setEmail("johndoe@example.com");
        user.setBBucksBalance(100.00);
        user = userDAO.create(user);

        assertNotNull(user);
        assertEquals(1L, user.getUserId());

        verify(preparedStatement).setString(1, "JohnDoe");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void updateUserTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);  // Simulate successful update

        User user = new User();
        user.setUserId(1L);
        user.setUserName("UpdatedName");
        user.setPassword("newPassword");
        user.setEmail("update@example.com");
        user.setBBucksBalance(200.00);
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));

        User updatedUser = userDAO.update(user);

        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals("UpdatedName", updatedUser.getUserName(), "Username should be updated");

        verify(preparedStatement).setString(1, user.getUserName());
        verify(preparedStatement).setString(2, user.getPassword());
        verify(preparedStatement).setString(3, user.getEmail());
        verify(preparedStatement).setDouble(4, user.getBBucksBalance());
        verify(preparedStatement).setTimestamp(5, user.getLastLogin());
        verify(preparedStatement).setLong(6, user.getUserId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void updateWithNoAffectedRows() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        User user = new User();
        user.setUserId(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> userDAO.update(user));
        assertEquals("java.sql.SQLException: Updating user failed, no rows affected.", exception.getMessage());
    }

    @Test
    void deleteUserTest() throws SQLException {
        // Ensure that the PreparedStatement is mocked and returned when prepareStatement is called
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Setup the executeUpdate to return a non-zero value indicating a row was affected
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Perform the operation
        assertDoesNotThrow(() -> userDAO.delete(1L));

        // Verify that setLong was called correctly
        verify(preparedStatement).setLong(1, 1L);

        // Verify that executeUpdate was indeed called
        verify(preparedStatement).executeUpdate();
    }

}
