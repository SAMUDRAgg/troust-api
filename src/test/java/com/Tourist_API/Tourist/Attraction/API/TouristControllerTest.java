package com.Tourist_API.Tourist.Attraction.API;

import com.Tourist_API.Tourist.Attraction.API.Cotroller.TouristController;
import com.Tourist_API.Tourist.Attraction.API.DAO.Tourist;
import com.Tourist_API.Tourist.Attraction.API.Service.TouristService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TouristControllerTest {
    @Mock
    private TouristService touristService;

    @InjectMocks
    private TouristController touristController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTouristAttraction_Success() {
        Tourist tourist = new Tourist("Name", "Location", "Desc", 4.5, 100, "9-5");
        when(touristService.addTouristAttraction(any(Tourist.class))).thenReturn(tourist);
        ResponseEntity<?> response = touristController.addTouristAttraction(tourist);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tourist, response.getBody());
    }

    @Test
    void testAddTouristAttraction_Failure() {
        when(touristService.addTouristAttraction(any(Tourist.class))).thenThrow(new RuntimeException("fail"));
        ResponseEntity<?> response = touristController.addTouristAttraction(new Tourist());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("fail"));
    }

    @Test
    void addTouristAttraction_NullBody_ReturnsBadRequest() {
        ResponseEntity<?> response = touristController.addTouristAttraction(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addTouristAttraction_EmptyFields_ReturnsCreated() {
        Tourist tourist = new Tourist("", "", "", 0.0, 0.0, "");
        when(touristService.addTouristAttraction(any(Tourist.class))).thenReturn(tourist);
        ResponseEntity<?> response = touristController.addTouristAttraction(tourist);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tourist, response.getBody());
    }

    @Test
    void addTouristAttraction_NullFields_ReturnsCreated() {
        Tourist tourist = new Tourist(null, null, null, 0.0, 0.0, null);
        when(touristService.addTouristAttraction(any(Tourist.class))).thenReturn(tourist);
        ResponseEntity<?> response = touristController.addTouristAttraction(tourist);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tourist, response.getBody());
    }

    @Test
    void testGetAllTouristAttractions_Success() {
        List<Tourist> list = List.of(new Tourist("A", "B", "C", 4, 10, "8-5"));
        when(touristService.getAllTouristAttractions()).thenReturn(list);
        ResponseEntity<?> response = touristController.getAllTouristAttractions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void testGetAllTouristAttractions_Empty() {
        when(touristService.getAllTouristAttractions()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = touristController.getAllTouristAttractions();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No data available", response.getBody());
    }

    @Test
    void testGetAllTouristAttractions_Exception_ReturnsBadRequest() {
        when(touristService.getAllTouristAttractions()).thenThrow(new RuntimeException("db error"));
        ResponseEntity<?> response = touristController.getAllTouristAttractions();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("db error"));
    }

    @Test
    void testGetAllTouristAttractions_LargeList_ReturnsOk() {
        List<Tourist> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(new Tourist("Name" + i, "Loc" + i, "Desc" + i, i, i, "9-5"));
        }
        when(touristService.getAllTouristAttractions()).thenReturn(largeList);
        ResponseEntity<?> response = touristController.getAllTouristAttractions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(largeList, response.getBody());
    }

    @Test
    void testSearchTouristAttractions_Found() {
        List<Tourist> results = List.of(new Tourist("A", "B", "C", 4, 10, "8-5"));
        when(touristService.searchTouristAttractions("A", null, null)).thenReturn(results);
        ResponseEntity<?> response = touristController.searchTouristAttractions("A", null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(results, response.getBody());
    }

    @Test
    void testSearchTouristAttractions_NotFound() {
        when(touristService.searchTouristAttractions(any(), any(), any())).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = touristController.searchTouristAttractions("X", null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No matching tourist attraction spots found", response.getBody());
    }

    @Test
    void searchTouristAttractions_AllNullParams_ReturnsBadRequestIfEmpty() {
        when(touristService.searchTouristAttractions(null, null, null)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = touristController.searchTouristAttractions(null, null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No matching tourist attraction spots found", response.getBody());
    }

    @Test
    void searchTouristAttractions_Exception_ReturnsBadRequest() {
        when(touristService.searchTouristAttractions(any(), any(), any())).thenThrow(new RuntimeException("search error"));
        ResponseEntity<?> response = touristController.searchTouristAttractions("a", "b", "c");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("search error"));
    }

    @Test
    void testUpdateOverallRating_Success() {
        Tourist tourist = new Tourist("A", "B", "C", 4, 10, "8-5");
        when(touristService.updateOverallRating(eq(1L), eq(4.5))).thenReturn(Optional.of(tourist));
        Map<String, Object> body = new HashMap<>();
        body.put("overallRating", 4.5);
        ResponseEntity<?> response = touristController.updateOverallRating(1L, body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tourist, response.getBody());
    }

    @Test
    void testUpdateOverallRating_NotFound() {
        when(touristService.updateOverallRating(eq(1L), eq(4.5))).thenReturn(Optional.empty());
        Map<String, Object> body = new HashMap<>();
        body.put("overallRating", 4.5);
        ResponseEntity<?> response = touristController.updateOverallRating(1L, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Attraction not found for ID:" + 1L, response.getBody());
    }

    @Test
    void testUpdateOverallRating_MissingRating() {
        Map<String, Object> body = new HashMap<>();
        ResponseEntity<?> response = touristController.updateOverallRating(1L, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Missing overallRating in request body", response.getBody());
    }

    @Test
    void testUpdateOverallRating_InvalidRating() {
        Map<String, Object> body = new HashMap<>();
        body.put("overallRating", "notANumber");
        ResponseEntity<?> response = touristController.updateOverallRating(1L, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid overallRating value", response.getBody());
    }

    @Test
    void updateOverallRating_Exception_ReturnsBadRequest() {
        Map<String, Object> body = new HashMap<>();
        body.put("overallRating", 5.0);
        when(touristService.updateOverallRating(anyLong(), anyDouble())).thenThrow(new RuntimeException("update error"));
        ResponseEntity<?> response = touristController.updateOverallRating(1L, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("update error"));
    }
}
