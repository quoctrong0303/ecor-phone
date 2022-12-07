package com.spring.quoctrong.models;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {

	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int id;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Order order;
	
	@Transient
	public int getDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	@Transient
	public int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	@Transient
	public int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.YEAR);
	}


	
}
