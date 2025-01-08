package com.example.demo;

import com.example.demo.Repository.ListingRepository;
import com.example.demo.Service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ListingServiceTest {

    private ListingService listingService;
    private ListingRepository listingRepository;

    @BeforeEach
    void setUp() {
        listingRepository = Mockito.mock(ListingRepository.class);
        listingService = new ListingService();
        listingService.listingRepository = listingRepository;
    }

    @Test
    void testDeleteListingById() throws SQLException {
        int listingId = 1;

        when(listingRepository.deleteListingById(listingId)).thenReturn(true);

        boolean result = listingService.deleteListingById(listingId);

        assertTrue(result);
        verify(listingRepository, times(1)).deleteListingById(listingId);
    }
}
