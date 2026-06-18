package com.app.financemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.financemanager.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	// find the all categories for current user
	List<Category> findCategoryByProfileId(Long id);

	// find a category of specific id under current user
	Category findCategoryByIdAndProfileId(Long id, Long profileId);

	// find the category by type
	List<Category> findByTypeAndProfileId(String type, Long profileId);

	Category findByNameAndTypeAndProfileId(String name, String type, Long profileId);

	Category findByNameAndProfileId(String name, Long profileId);
}
