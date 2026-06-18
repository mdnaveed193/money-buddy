package com.app.financemanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.app.financemanager.model.Category;
import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.service.CategoryService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public ResponseEntity<ResponseStructure<Category>> createCategory(@RequestBody Category category) {

		return categoryService.createCategory(category);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Category>>> getAllCategories() {
		return categoryService.getAllCategories();

	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Category>> getCategoryById(@PathVariable Long id) {

		return categoryService.getCategoryById(id);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity<ResponseStructure<List<Category>>> getCategoriesByType(@PathVariable String type) {

		return categoryService.getCategoriesByType(type);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteCategory(@PathVariable Long id) {

		return categoryService.deleteCategory(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Category>> updateCategory(@PathVariable Long id,
			@RequestBody Category category) {

		return categoryService.updateCategory(id, category);
	}

}
