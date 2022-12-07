package com.spring.quoctrong.models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "secure_Tokens")
public class SecureToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true)
	private String token;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	@Column(updatable = false)
	private LocalDateTime expireAt;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@Transient
	public boolean isExpired() {
		return this.expireAt.isBefore(LocalDateTime.now()) ? true : false;
	}
}
