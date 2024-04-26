package com.employee1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee1.entity.Department; 

@Repository
public interface DepartmentDao  extends JpaRepository<Department, Integer>{
	
	public Department findByName(String name);

}
