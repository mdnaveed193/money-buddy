package com.app.financemanager.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.app.financemanager.model.Expense;
import com.app.financemanager.repository.ExpenseRepository;

@Repository
public class ExpenseDao {

	@Autowired
	private ExpenseRepository expenseRepository;

	public Expense saveExpense(Expense expense) {
		return expenseRepository.save(expense);
	}

	public Expense findExpenseById(Long expenseId) {
		return expenseRepository.findById(expenseId).orElse(null);
	}

	public void deleteExpense(Expense expense) {
		expenseRepository.delete(expense);
	}

	public List<Expense> findAllExpense(Long profileId) {
		return expenseRepository.findByProfile_IdOrderByDateDesc(profileId);
	}

	public List<Expense> findRecentExpenses(Long profileId) {
		return expenseRepository.findTop5ByProfile_IdOrderByDateDesc(profileId);
	}

//	@Query("Select sum(e.amount) from Expense e where i.profile.id = :profileId")
	public BigDecimal findTotalExpense(Long profileId) {
		return expenseRepository.findTotalExpenseByProfileId(profileId);
	}

	public List<Expense> searchExpense(Long profileId, LocalDate startDate,
			LocalDate endDate, String keyword, Sort sort) {
		return expenseRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profileId, startDate, endDate, keyword, sort);
//		return expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profileId, startDate, endDate, keyword, sort);

	}

	public List<Expense> findExpenseBetweenDates(Long profileId, LocalDate startDate, LocalDate endDate) {
		return expenseRepository.findByProfile_IdAndDateBetween(profileId, startDate, endDate);
	}

}
