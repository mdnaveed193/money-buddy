package com.app.financemanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.CategoryDao;
import com.app.financemanager.dao.IncomeDao;
import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.model.Category;
import com.app.financemanager.model.Expense;
import com.app.financemanager.model.Income;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;

@Service
public class IncomeService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ProfileDao profileDao;

	@Autowired
	private IncomeDao incomeDao;

	private Profile getCurrentProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return profileDao.findByEmail(email);
	}

	public ResponseEntity<ResponseStructure<Income>> addIncome(Income income) {
		Profile profile = getCurrentProfile();
		ResponseStructure<Income> structure = new ResponseStructure<>();

		Long categoryId = null;
		if (income != null && income.getCategory() != null) {
			categoryId = income.getCategory().getId();
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

		income.setProfile(profile);
		income.setCategory(category);
		Income savedIncome = incomeDao.saveIncome(income);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Income Added Successfully");
		structure.setData(savedIncome);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);

	}

	public ResponseEntity<ResponseStructure<List<Income>>> getAllIncomes() {
		Profile profile = getCurrentProfile();

		List<Income> incomes = incomeDao.findAllIncomes(profile.getId());

		ResponseStructure<List<Income>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Incomes Fetched Successfully");

		structure.setData(incomes);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<Income>> getIncomeById(Long id) {
		Profile profile = getCurrentProfile();

		Income income = incomeDao.findIncomeById(id);

		ResponseStructure<Income> structure = new ResponseStructure<>();

		if (income == null || income.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Income Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Income Found");

		structure.setData(income);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<Income>> updateIncome(Long incomeId, Income updatedIncome) {
		Profile profile = getCurrentProfile();

		ResponseStructure<Income> structure = new ResponseStructure<>();

		Income existingIncome = incomeDao.findIncomeById(incomeId);

		if (existingIncome == null || existingIncome.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Income Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		Long categoryId = null;
		if (updatedIncome != null && updatedIncome.getCategory() != null) {
			categoryId = updatedIncome.getCategory().getId();
		}
		if (categoryId == null || categoryId == 0L) {
			structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
			structure.setMessage("categoryId is required");
			return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
		}

		Category category = categoryDao.findCategoryByIdAndProfileId(categoryId,
				profile.getId());

		if (category == null) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Category Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		existingIncome.setName(updatedIncome.getName());

		existingIncome.setAmount(updatedIncome.getAmount());

		existingIncome.setDate(updatedIncome.getDate());

		existingIncome.setIcon(updatedIncome.getIcon());

		existingIncome.setCategory(category);

		Income savedIncome = incomeDao.saveIncome(existingIncome);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Inocme Updated Successfully");

		structure.setData(savedIncome);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<String>> deleteIncome(Long incomeId) {
		Profile profile = getCurrentProfile();

		ResponseStructure<String> structure = new ResponseStructure<>();

		Income income = incomeDao.findIncomeById(incomeId);

		if (income == null || income.getProfile().getId() != profile.getId()) {

			structure.setStatusCode(HttpStatus.NOT_FOUND.value());

			structure.setMessage("Income Not Found");

			return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
		}

		incomeDao.deleteIncome(income);

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Income Deleted Successfully");

		structure.setData("Deleted");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Income>>> getRecentIncomes() {
		Profile profile = getCurrentProfile();

		List<Income> incomes = incomeDao.findRecentIncomes(profile.getId());

		ResponseStructure<List<Income>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Recent Incomes");

		structure.setData(incomes);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<BigDecimal>> getTotalIncomes() {
		Profile profile = getCurrentProfile();

		BigDecimal total = incomeDao.findTotalIncome(profile.getId());

		ResponseStructure<BigDecimal> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Total Income");

		structure.setData(total == null ? BigDecimal.ZERO : total);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<List<Income>>> getIncomesBetweenDates(LocalDate startDate,
			LocalDate endDate) {
		Profile profile = getCurrentProfile();

		List<Income> incomes = incomeDao.findIncomeBetweenDates(profile.getId(), startDate, endDate);

		ResponseStructure<List<Income>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Incomea Fetched Successfully");
		structure.setData(incomes);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<List<Income>>> searchIncomes(String keyword, LocalDate startDate,
			LocalDate endDate) {

		Profile profile = getCurrentProfile();

		List<Income> expenses = incomeDao.searchIncome(

				profile.getId(),

				startDate,

				endDate,

				keyword,

				Sort.by(Sort.Direction.DESC, "date"));

		ResponseStructure<List<Income>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());

		structure.setMessage("Income Found");

		structure.setData(expenses);

		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

}
