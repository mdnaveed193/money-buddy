package com.app.financemanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.CategoryDao;
import com.app.financemanager.dao.ExpenseDao;
import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.model.Category;
import com.app.financemanager.model.Expense;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;
//import com.app.financemanager.repository.LOcalDate;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseDao expenseDao;

	@Autowired
	private ProfileDao profileDao;

	@Autowired
	private CategoryDao categoryDao;

	private Profile getCurrentProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return profileDao.findByEmail(email);
	}

	public ResponseEntity<ResponseStructure<Expense>> addExpense(Expense expenseRequest) {
		Profile profile = getCurrentProfile();
		ResponseStructure<Expense> structure = new ResponseStructure<>();

		Long categoryId = null;
		if (expenseRequest != null && expenseRequest.getCategory() != null) {
			Category tmpCategory = expenseRequest.getCategory();
			if (tmpCategory != null) {
				categoryId = tmpCategory.getId();
			}
		}
		if (categoryId == null || categoryId == 0L) {
			structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
		}

		Category category = categoryDao.findCategoryByIdAndProfileId(categoryId, profile.getId());

		if (category == null) {
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Category Not Found");
			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		Expense expense = expenseRequest;
		expense.setProfile(profile);
		expense.setCategory(category);

		Expense savedExpense = expenseDao.saveExpense(expense);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Expense Added Successfully");
		structure.setData(savedExpense);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Expense>>> getAllExpenses() {

		Profile profile = getCurrentProfile();

		List<Expense> expenses = expenseDao.findAllExpense(profile.getId());

		ResponseStructure<List<Expense>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Expenses Fetched Successfully");

		structure.setData(expenses);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Expense>> getExpenseById(Long expenseId) {

		Profile profile = getCurrentProfile();

		Expense expense = expenseDao.findExpenseById(expenseId);

		ResponseStructure<Expense> structure = new ResponseStructure<>();

		if (expense == null || expense.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Expense Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Expense Found");

		structure.setData(expense);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Expense>> updateExpense(Long expenseId, Expense updatedExpense) {

		Profile profile = getCurrentProfile();

		ResponseStructure<Expense> structure = new ResponseStructure<>();

		Expense existingExpense = expenseDao.findExpenseById(expenseId);

		if (existingExpense == null || existingExpense.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Expense Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		Long categoryId = null;
		if (updatedExpense != null && updatedExpense.getCategory() != null) {
			Category tmpCategory = updatedExpense.getCategory();
			if (tmpCategory != null) {
				categoryId = tmpCategory.getId();
			}
		}
		if (categoryId == null || categoryId == 0L) {
			structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
		}

		Category category = categoryDao.findCategoryByIdAndProfileId(categoryId, profile.getId());

		if (category == null) {
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			structure.setMessage("Category Not Found");
			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		existingExpense.setName(updatedExpense.getName());
		existingExpense.setAmount(updatedExpense.getAmount());
		existingExpense.setDate(updatedExpense.getDate());
		existingExpense.setIcon(updatedExpense.getIcon());
		existingExpense.setCategory(category);

		Expense savedExpense = expenseDao.saveExpense(existingExpense);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Expense Updated Successfully");

		structure.setData(savedExpense);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteExpense(Long expenseId) {

		Profile profile = getCurrentProfile();

		ResponseStructure<String> structure = new ResponseStructure<>();

		Expense expense = expenseDao.findExpenseById(expenseId);

		if (expense == null || expense.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Expense Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		expenseDao.deleteExpense(expense);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Expense Deleted Successfully");

		structure.setData("Deleted");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Expense>>> getRecentExpenses() {

		Profile profile = getCurrentProfile();

		List<Expense> expenses = expenseDao.findRecentExpenses(profile.getId());

		ResponseStructure<List<Expense>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Recent Expenses");

		structure.setData(expenses);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<BigDecimal>> getTotalExpense() {

		Profile profile = getCurrentProfile();

		BigDecimal total = expenseDao.findTotalExpense(profile.getId());

		ResponseStructure<BigDecimal> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Total Expense");

		structure.setData(total == null ? BigDecimal.ZERO : total);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Expense>>> getExpensesBetweenDates(LocalDate startDate,
			LocalDate endDate) {

		Profile profile = getCurrentProfile();

		List<Expense> expenses = expenseDao.findExpenseBetweenDates(profile.getId(), startDate, endDate);

		ResponseStructure<List<Expense>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Expenses Fetched Successfully");

		structure.setData(expenses);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Expense>>> searchExpenses(

			String keyword, LocalDate startDate, LocalDate endDate) {

		Profile profile = getCurrentProfile();

		List<Expense> expenses = expenseDao.searchExpense(

				profile.getId(),

				startDate,

				endDate,

				keyword,

				Sort.by(Sort.Direction.DESC, "date"));

		ResponseStructure<List<Expense>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		
		structure.setMessage("Expenses Found");
		

		structure.setData(expenses);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

}
