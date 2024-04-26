package com.employee1.service;

import java.util.List;

import com.employee1.entity.Department;
public interface DepartmentService {
	public List<Department> getAllDepartment();
	
	public Department getDepartmentById(Integer id);
	
	public Department addDepartment(Department department) ;
	
	public Department updateDepartment(Department department, Integer id);
	
	public Boolean deleteDepartmnet(Integer id);
	
	public void updateSalary(Long salary, Integer id);
}
