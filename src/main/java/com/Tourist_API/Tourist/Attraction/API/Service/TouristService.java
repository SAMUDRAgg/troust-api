package com.Tourist_API.Tourist.Attraction.API.Service;

import com.Tourist_API.Tourist.Attraction.API.DAO.Tourist;
import com.Tourist_API.Tourist.Attraction.API.Repo.TouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing tourist attractions.
 * Handles business logic for adding, retrieving, searching, and updating tourist attractions.
 */
@Service
public class TouristService {
    @Autowired
    private TouristRepository touristRepository;

    /**
     * Add a new tourist attraction.
     *
     * @param tourist the tourist attraction to add
     * @return the saved Tourist entity
     */
    public Tourist addTouristAttraction(Tourist tourist) {
        return touristRepository.save(tourist);
    }

    /**
     * Get all tourist attractions.
     *
     * @return list of all Tourist entities
     */
    public List<Tourist> getAllTouristAttractions() {
        return touristRepository.findAll();
    }

    /**
     * Search tourist attractions by name, description, or location.
     *
     * @param name name to search (optional)
     * @param description description to search (optional)
     * @param location location to search (optional)
     * @return list of matching Tourist entities
     */
    public List<Tourist> searchTouristAttractions(String name, String description, String location) {
        return touristRepository.findAll().stream()
                .filter(t -> (name != null && t.getName().toLowerCase().contains(name.toLowerCase())) ||
                        (description != null && t.getDescription().toLowerCase().contains(description.toLowerCase())) ||
                        (location != null && t.getLocation().toLowerCase().contains(location.toLowerCase())))
                .toList();
    }

    /**
     * Update the overall rating of a tourist attraction by ID.
     *
     * @param id the attraction ID
     * @param newRating the new overall rating
     * @return Optional containing the updated Tourist entity if found, otherwise empty
     */
    public Optional<Tourist> updateOverallRating(Long id, double newRating) {
        Optional<Tourist> touristOpt = touristRepository.findById(id);
        if (touristOpt.isPresent()) {
            Tourist tourist = touristOpt.get();
            tourist.setOverallRating(newRating);
            touristRepository.save(tourist);
            return Optional.of(tourist);
        }
        return Optional.empty();
    }
}
