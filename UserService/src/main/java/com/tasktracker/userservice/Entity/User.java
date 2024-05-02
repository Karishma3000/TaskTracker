package com.tasktracker.userservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "otp")
	private Long otp;

	@Column(name = "creation_date")
	@CreationTimestamp
	private Date creationDate;

	@Column(name = "fileName")
	private String fileName;

	@Transient
	private List<Task> task;

}
