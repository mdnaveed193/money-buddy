package com.app.financemanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.financemanager.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>  {

	List<Expense> findByProfile_IdOrderByDateDesc(Long id);
	
	List<Expense> findTop5ByProfile_IdOrderByDateDesc(Long id);
	
	@Query("Select sum(e.amount) from Expense e where e.profile.id = :profileId")	
	BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);
	
	List<Expense> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword,Sort sort);
	
	List<Expense>findByProfile_IdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
	
	
}
