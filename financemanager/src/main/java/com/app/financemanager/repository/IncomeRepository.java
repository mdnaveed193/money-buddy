package com.app.financemanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;

//import org.springframework.boot.data.autoconfigure.web.DataWebPropertie

//s.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.financemanager.model.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {

	List<Income> findByProfile_IdOrderByDateDesc(Long id);
	
	List<Income> findTop5ByProfile_IdOrderByDateDesc(Long id);
	
	@Query("Select sum(i.amount) from Income i where i.profile.id = :profileId")    
	BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);
	
	List<Income> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
	
	List<Income>findByProfile_IdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
	
	
}
