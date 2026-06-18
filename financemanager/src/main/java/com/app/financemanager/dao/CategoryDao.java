package com.app.financemanager.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.financemanager.model.Category;
import com.app.financemanager.repository.CategoryRepository;

@Repository
public class CategoryDao {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	public List<Category> findAllCategoriesByProfileId(long profileId) {

		return categoryRepository.findCategoryByProfileId(profileId);
	}

	public Category findCategoryByIdAndProfileId(Long categoryId, Long profileId) {

		return categoryRepository.findCategoryByIdAndProfileId(categoryId, profileId);
	}

	public List<Category> findByTypeAndProfileId(String type, Long profileId) {

		return categoryRepository.findByTypeAndProfileId(type, profileId);
	}

	public Category findByNameAndProfileId(String name, Long profileId) {

		return categoryRepository.findByNameAndProfileId(name, profileId);
	}
	public void deleteCategory(Category category) {
		categoryRepository.delete(category);
	}
	
	public Category updateCategory(Category category) {
		return categoryRepository.save(category);
	}

}
