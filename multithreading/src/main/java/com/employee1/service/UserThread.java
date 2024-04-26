package com.employee1.service;

import com.employee1.entity.Employee;


public class UserThread extends Thread{

	private EmployeeService employeeService;
	
	private Employee employee;
	private Long salary;
	public UserThread(EmployeeService employeeService,  Long salary, Employee employee) {
		super();
		this.employeeService = employeeService;
		
		this.salary = salary;
		this.employee=employee;
	}


	public void run() {
		
		
		employee.setSalary(salary);
		employeeService.addEmployee(employee);	
				
	}

}
