package com.employee1.entity;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Employee")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="emp_id")
	private Integer id;
	
	@Column(name="emp_firstname")
	private String firstName;
	
	@Column(name="emp_lastname")
	private String lastName;
	
	@Column(name="emp_age")
	private int age;
	
	@Column(name="emp_salary")
	private volatile long salary;
	
//	@ManyToOne
//	private Department department;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<Department> department;
	
	
}
