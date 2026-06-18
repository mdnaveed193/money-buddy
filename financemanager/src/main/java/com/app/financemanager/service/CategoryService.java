package com.app.financemanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.CategoryDao;
import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.model.Category;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;

@Service
public class CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ProfileDao profileDao;

	private Profile getCurrentProfile() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		return profileDao.findByEmail(email);
	}

	public ResponseEntity<ResponseStructure<Category>> createCategory(Category category) {

		Profile profile = getCurrentProfile();

		Category existingCategory = categoryDao.findByNameAndProfileId(category.getName(), profile.getId());

		ResponseStructure<Category> structure = new ResponseStructure<>();

		if (existingCategory != null) {

			structure.setStatusCode(HttpStatus.CONFLICT.value());

			structure.setMessage("Category Already Exists");

			return new ResponseEntity<>(structure, HttpStatus.CONFLICT);
		}

		category.setProfile(profile);

		Category savedCategory = categoryDao.saveCategory(category);

		structure.setStatusCode(HttpStatus.CREATED.value());

		structure.setMessage("Category Created Successfully");

		structure.setData(savedCategory);

		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Category>>> getAllCategories() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Profile profile = profileDao.findByEmail(email);

		List<Category> categories = categoryDao.findAllCategoriesByProfileId(profile.getId());

		ResponseStructure<List<Category>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Categories Fetched Successfully");

		structure.setData(categories);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<Category>> getCategoryById(Long categoryId) {

		Profile profile = getCurrentProfile();

		Category category = categoryDao.findCategoryByIdAndProfileId(categoryId, profile.getId());

		ResponseStructure<Category> structure = new ResponseStructure<>();

		if (category == null) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Category Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Category Found");

		structure.setData(category);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Category>>> getCategoriesByType(String type) {

		Profile profile = getCurrentProfile();

		List<Category> categories = categoryDao.findByTypeAndProfileId(type, profile.getId());

		ResponseStructure<List<Category>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Categories Found");

		structure.setData(categories);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteCategory(Long categoryId) {

		Profile profile = getCurrentProfile();

		Category category = categoryDao.findCategoryByIdAndProfileId(categoryId, profile.getId());

		ResponseStructure<String> structure = new ResponseStructure<>();

		if (category == null) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Category Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		categoryDao.deleteCategory(category);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Category Deleted Successfully");

		structure.setData("Deleted");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Category>> updateCategory(Long categoryId, Category updatedCategory) {

		Profile profile = getCurrentProfile();

		ResponseStructure<Category> structure = new ResponseStructure<>();

		Category existingCategory = categoryDao.findCategoryByIdAndProfileId(categoryId, profile.getId());

		if (existingCategory == null) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Category Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		Category duplicateCategory = categoryDao.findByNameAndProfileId(updatedCategory.getName(), profile.getId());

		if (duplicateCategory != null && duplicateCategory.getId() != existingCategory.getId()) {

			structure.setStatusCode(HttpStatus.CONFLICT.value());

			structure.setMessage("Category Already Exists");

			return new ResponseEntity<>(structure, HttpStatus.CONFLICT);
		}

		existingCategory.setName(updatedCategory.getName());

		existingCategory.setType(updatedCategory.getType());

		existingCategory.setIcon(updatedCategory.getIcon());

		Category savedCategory = categoryDao.updateCategory(existingCategory);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Category Updated Successfully");

		structure.setData(savedCategory);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
