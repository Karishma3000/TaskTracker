package com.employee1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee1.entity.Employee;
import com.employee1.service.EmployeeService;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	

	@GetMapping
	public ResponseEntity<List<Employee>>getAllEmployee(){
		log.info("list of employee");
		List<Employee> employeeList=  employeeService.getAllEmployee();
		if(!employeeList.isEmpty()) {
			return new ResponseEntity<List<Employee>>(employeeList, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<List<Employee>>(employeeList,HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee>getEmployeeById(@PathVariable Integer id){
		log.info("get employee by id "+id);
		Employee employee=employeeService.getEmployeeById(id);
		if(employee!=null) {
			return new ResponseEntity<Employee>(employee,HttpStatus.FOUND);
		}
		else {
			return new ResponseEntity<Employee>(employee,HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping
	public ResponseEntity<Employee>addEmployee(@RequestBody Employee newEmployee){
		log.info("add new employee "+newEmployee);
		Employee employee = employeeService.addEmployee(newEmployee);
		if(employee!=null) {
			return new ResponseEntity<Employee>(employee,HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Employee>(employee,HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Employee>updateEmployee(@RequestBody Employee employee, @PathVariable Integer id){
		log.info("update employe "+id);
		Employee updatedEmployee = employeeService.updateEmployee(employee,id);
		if(updatedEmployee!=null) {
			return new ResponseEntity<Employee>(updatedEmployee,HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Employee>(updatedEmployee,HttpStatus.NOT_MODIFIED);
		}
	}
	
	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable Integer id){
		log.info("delete employee "+id);
		if(employeeService.deleteEmployee(id)) {
			return "employee deleted";
		}
		else {
			 return "employee not found";
		}	
	}
	
	

}
