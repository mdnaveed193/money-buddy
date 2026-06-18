package com.app.financemanager.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String type;
	private String icon;
	@ManyToOne
	@JoinColumn(name="profile_id", nullable = false)
	@JsonIgnore
	private Profile profile;
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	@CreationTimestamp
	private LocalDateTime updatedAt;
	
	

}
