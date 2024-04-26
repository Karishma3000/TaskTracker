package com.employee1.service;

import java.util.List;

import com.employee1.entity.Employee;

public interface EmployeeService {
	
	public List<Employee> getAllEmployee();
	public Employee getEmployeeById(Integer id);
	public Employee addEmployee(Employee employee);
	public Employee updateEmployee(Employee employee, Integer id);
	public Boolean deleteEmployee(Integer id);
	

}
