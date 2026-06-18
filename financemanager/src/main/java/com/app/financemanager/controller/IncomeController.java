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
import com.app.financemanager.model.Income;
import com.app.financemanager.dto.IncomeRequest;
import com.app.financemanager.responseStructure.ResponseStructure;
import com.app.financemanager.service.ExpenseService;
import com.app.financemanager.service.IncomeService;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
	@Autowired
	private IncomeService incomeService;

	@PostMapping
	public ResponseEntity<ResponseStructure<Income>> addIncome(@RequestBody IncomeRequest incomeRequest) {

		if (incomeRequest == null || incomeRequest.getCategoryId() == null || incomeRequest.getCategoryId() == 0L) {
			ResponseStructure<Income> structure = new ResponseStructure<>();
			structure.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		Income income = new Income();
		income.setName(incomeRequest.getName());
		income.setAmount(incomeRequest.getAmount());
		income.setDate(incomeRequest.getDate());
		income.setIcon(incomeRequest.getIcon());
		com.app.financemanager.model.Category category = new com.app.financemanager.model.Category();
		category.setId(incomeRequest.getCategoryId());
		income.setCategory(category);

		return incomeService.addIncome(income);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Income>>> getAllIncomes() {

		return incomeService.getAllIncomes();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Income>> getIncomeById(@PathVariable Long id) {

		return incomeService.getIncomeById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Income>> updateIncome(@PathVariable Long id, @RequestBody IncomeRequest incomeRequest) {

		if (incomeRequest == null || incomeRequest.getCategoryId() == null || incomeRequest.getCategoryId() == 0L) {
			ResponseStructure<Income> structure = new ResponseStructure<>();
			structure.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		Income income = new Income();
		income.setName(incomeRequest.getName());
		income.setAmount(incomeRequest.getAmount());
		income.setDate(incomeRequest.getDate());
		income.setIcon(incomeRequest.getIcon());
		com.app.financemanager.model.Category category = new com.app.financemanager.model.Category();
		category.setId(incomeRequest.getCategoryId());
		income.setCategory(category);

		return incomeService.updateIncome(id, income);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteIncome(@PathVariable Long id) {

		return incomeService.deleteIncome(id);
	}

	@GetMapping("/recent")
	public ResponseEntity<ResponseStructure<List<Income>>> getRecentIncomes() {

		return incomeService.getRecentIncomes();
	}

	@GetMapping("/total")
	public ResponseEntity<ResponseStructure<BigDecimal>> getTotalExpense() {

		return incomeService.getTotalIncomes();
	}

	@GetMapping("/date-range")
	    public ResponseEntity<ResponseStructure<List<Income>>> getExpensesBetweenDates(

		    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return incomeService.getIncomesBetweenDates(startDate, endDate);
	}

	@GetMapping("/search")
	    public ResponseEntity<ResponseStructure<List<Income>>> searchExpenses(

		    @RequestParam String keyword,

		    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

		    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return incomeService.searchIncomes(keyword, startDate, endDate);
	}
}
