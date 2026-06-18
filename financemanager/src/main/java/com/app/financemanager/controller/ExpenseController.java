package com.app.financemanager.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;

import com.app.financemanager.model.Expense;
import com.app.financemanager.model.Category;
import com.app.financemanager.dto.ExpenseRequest;
import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	@PostMapping
	public ResponseEntity<ResponseStructure<Expense>> addExpense(@RequestBody ExpenseRequest expenseRequest) {

		if (expenseRequest == null || expenseRequest.getCategoryId() == null || expenseRequest.getCategoryId() == 0L) {
			ResponseStructure<Expense> structure = new ResponseStructure<>();
			structure.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		Expense expense = new Expense();
		expense.setName(expenseRequest.getName());
		expense.setAmount(expenseRequest.getAmount());
		expense.setDate(expenseRequest.getDate());
		expense.setIcon(expenseRequest.getIcon());
		Category category = new Category();
		category.setId(expenseRequest.getCategoryId());
		expense.setCategory(category);

		return expenseService.addExpense(expense);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Expense>>> getAllExpenses() {

		return expenseService.getAllExpenses();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Expense>> getExpenseById(@PathVariable Long id) {

		return expenseService.getExpenseById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Expense>> updateExpense(@PathVariable Long id,
			@RequestBody ExpenseRequest expenseRequest) {

		if (expenseRequest == null || expenseRequest.getCategoryId() == null || expenseRequest.getCategoryId() == 0L) {
			ResponseStructure<Expense> structure = new ResponseStructure<>();
			structure.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		Expense updatedExpense = new Expense();
		updatedExpense.setName(expenseRequest.getName());
		updatedExpense.setAmount(expenseRequest.getAmount());
		updatedExpense.setDate(expenseRequest.getDate());
		updatedExpense.setIcon(expenseRequest.getIcon());
		Category category = new Category();
		category.setId(expenseRequest.getCategoryId());
		updatedExpense.setCategory(category);

		return expenseService.updateExpense(id, updatedExpense);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteExpense(@PathVariable Long id) {

		return expenseService.deleteExpense(id);
	}

	@GetMapping("/recent")
	public ResponseEntity<ResponseStructure<List<Expense>>> getRecentExpenses() {

		return expenseService.getRecentExpenses();
	}

	@GetMapping("/total")
	public ResponseEntity<ResponseStructure<BigDecimal>> getTotalExpense() {

		return expenseService.getTotalExpense();
	}

	@GetMapping("/date-range")
	public ResponseEntity<ResponseStructure<List<Expense>>> getExpensesBetweenDates(

		    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return expenseService.getExpensesBetweenDates(startDate, endDate);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseStructure<List<Expense>>> searchExpenses(

			
			
			
			@RequestParam String keyword,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return expenseService.searchExpenses(keyword, startDate, endDate);
	}
}
