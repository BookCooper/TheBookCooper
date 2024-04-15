package com.thebookcooper;

import com.thebookcooper.dao.ListingsDAO;
import com.thebookcooper.model.Listing;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.List;

public class ListingsDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ListingsDAO listingsDAO;

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
        when(resultSet.getLong("listing_id")).thenReturn(1L);
        when(resultSet.getDouble("price")).thenReturn(100.00);

        Listing foundListing = listingsDAO.findById(1L);

        assertNotNull(foundListing);
        assertEquals(1L, foundListing.getListingId());
        assertEquals(100.00, foundListing.getPrice());
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void createListingTest() throws SQLException {
        Listing listing = new Listing();
        listing.setUserId(2L);
        listing.setBookId(3L);
        listing.setListingStatus("Available");
        listing.setBookCondition("New");
        listing.setPrice(100.00);

        when(resultSet.getLong(1)).thenReturn(1L);

        Listing createdListing = listingsDAO.create(listing);

        assertNotNull(createdListing);
        assertEquals(1L, createdListing.getListingId());
        verify(preparedStatement).setLong(1, listing.getUserId());
        verify(preparedStatement).setLong(2, listing.getBookId());
        verify(preparedStatement).setString(3, listing.getListingStatus());
        verify(preparedStatement).setString(4, listing.getBookCondition());
        verify(preparedStatement).setDouble(5, listing.getPrice());
    }

    @Test
    void updateListingTest() throws SQLException {
        Listing listing = new Listing();
        listing.setListingId(1L);
        listing.setUserId(2L);
        listing.setBookId(3L);
        listing.setListingStatus("Sold");
        listing.setBookCondition("Used");
        listing.setPrice(80.00);

        Listing updatedListing = listingsDAO.update(listing);

        assertNotNull(updatedListing);
        verify(preparedStatement).setLong(1, listing.getUserId());
        verify(preparedStatement).setLong(2, listing.getBookId());
        verify(preparedStatement).setString(3, listing.getListingStatus());
        verify(preparedStatement).setString(4, listing.getBookCondition());
        verify(preparedStatement).setDouble(5, listing.getPrice());
        verify(preparedStatement).setTimestamp(6, listing.getListingDate());
        verify(preparedStatement).setLong(7, listing.getListingId());
    }

    @Test
    void deleteListingTest() throws SQLException {
        listingsDAO.delete(1L);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void findByBookIdTest() throws SQLException {
        List<Listing> listings = listingsDAO.findByBookId(3L);

        assertNotNull(listings);
        assertFalse(listings.isEmpty());
        verify(preparedStatement).setLong(1, 3L);
    }
}
