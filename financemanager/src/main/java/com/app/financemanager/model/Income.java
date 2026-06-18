package com.app.financemanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="incomes")
@Data
public class Income {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String icon;
	private LocalDate date;
	private BigDecimal amount;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	@CreationTimestamp
	private LocalDateTime updatedAt;
	

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "profile_id", nullable = false)
	private Profile profile;
	
	
	@PrePersist
	public void prePersist() {
		if(this.date==null) {
			this.date=LocalDate.now();
		}
	}
}
