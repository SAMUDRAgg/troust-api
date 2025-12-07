package com.Tourist_API.Tourist.Attraction.API.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Tourist_API.Tourist.Attraction.API.DAO.Tourist;

/**
 * Repository interface for Tourist entities.
 * Extends JpaRepository to provide CRUD operations for Tourist.
 */
public interface TouristRepository extends JpaRepository<Tourist, Long> {

}
