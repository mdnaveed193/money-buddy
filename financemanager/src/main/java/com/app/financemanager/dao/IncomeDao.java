package com.app.financemanager.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.app.financemanager.model.Expense;
import com.app.financemanager.model.Income;
import com.app.financemanager.repository.IncomeRepository;

@Repository
public class IncomeDao {

	@Autowired
	private IncomeRepository incomeRepository;

	public Income saveIncome(Income income) {
		return incomeRepository.save(income);
	}

	public List<Income> findAllIncomes(long profileId) {
		return incomeRepository.findByProfile_IdOrderByDateDesc(profileId);

	}

	public Income findIncomeById(Long incomeId) {
		return incomeRepository.findById(incomeId).orElse(null);
		
	}

	public void deleteIncome(Income income) {
		incomeRepository.delete(income);

	}

	public List<Income> findRecentIncomes(long id) {
		return incomeRepository.findTop5ByProfile_IdOrderByDateDesc(id);
	}

	public BigDecimal findTotalIncome(long profileId) {
		return incomeRepository.findTotalIncomeByProfileId(profileId);
		
	}

	public List<Income> searchIncome(Long profileId, LocalDate startDate,
			LocalDate endDate, String keyword, Sort sort) {
		return incomeRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profileId, startDate, endDate, keyword, sort);
//		return expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profileId, startDate, endDate, keyword, sort);

	}
	
	public List<Income> findIncomeBetweenDates(Long profileId, LocalDate startDate, LocalDate endDate) {
		return incomeRepository.findByProfile_IdAndDateBetween(profileId, startDate, endDate);
	}
	
	
    
}
