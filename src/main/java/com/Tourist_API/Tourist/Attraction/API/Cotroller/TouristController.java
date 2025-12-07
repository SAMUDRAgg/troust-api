package com.Tourist_API.Tourist.Attraction.API.Cotroller;

import com.Tourist_API.Tourist.Attraction.API.DAO.Tourist;
import com.Tourist_API.Tourist.Attraction.API.Service.TouristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing tourist attractions.
 * Provides endpoints to add, list, search, and update tourist attractions.
 */
@RestController
@RequestMapping("/tourist")
public class TouristController {
    @Autowired
    private TouristService touristService;

    /**
     * Add a new tourist attraction.
     *
     * @param tourist the tourist attraction to add
     * @return 201 Created with the saved entity, or 400 Bad Request on error
     */
    @PostMapping("/add")
    public ResponseEntity<?> addTouristAttraction(@RequestBody Tourist tourist) {
        try {
            if (tourist == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tourist attraction body cannot be null");
            }
            Tourist saved = touristService.addTouristAttraction(tourist);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all tourist attractions.
     *
     * @return 200 OK with list, or 400 Bad Request if no data
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllTouristAttractions() {
        try {
            List<Tourist> list = touristService.getAllTouristAttractions();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data available");
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Search tourist attractions by any combination of name, description, or location.
     * All parameters are optional and can be used together or individually.
     *
     * @param name optional name to search
     * @param description optional description to search
     * @param location optional location to search
     * @return ResponseEntity containing a list of matching Tourist objects with HTTP 200 OK,
     *         or HTTP 400 Bad Request with an error message if no matches or an exception occurs.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTouristAttractions(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String description,
                                                     @RequestParam(required = false) String location) {
        try {
            // Call the service to search for tourist attractions matching the given parameters.
            List<Tourist> results = touristService.searchTouristAttractions(name, description, location);
            if (results.isEmpty()) {
                // Return 400 Bad Request if no matching tourist attractions are found.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No matching tourist attraction spots found");
            }
            // Return 200 OK with the list of matching tourist attractions.
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            // Return 400 Bad Request if an exception occurs during the search.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update the overall rating of a tourist attraction by ID.
     *
     * @param id the attraction ID
     * @param body must contain 'overallRating'
     * @return 200 OK with updated entity, or 400 Bad Request on error
     */
    @PutMapping("/update-rating/{id}")
    public ResponseEntity<?> updateOverallRating(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("overallRating")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing overallRating in request body");
            }
            double newRating;
            try {
                newRating = Double.parseDouble(body.get("overallRating").toString());
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid overallRating value");
            }
            return touristService.updateOverallRating(id, newRating)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Attraction not found for ID:" + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
