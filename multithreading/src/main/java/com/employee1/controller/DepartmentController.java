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

import com.employee1.entity.Department;
import com.employee1.entity.Employee;
import com.employee1.service.DepartmentService;


import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping
	public ResponseEntity<List<Department>> getAllDepartment(){
		log.info("list of de");
		List<Department> departmentlist = departmentService.getAllDepartment();
		if(!departmentlist.isEmpty()) {
			return new ResponseEntity<List<Department>>( departmentlist, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<List<Department>>(departmentlist,HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Department> getDepartmentById(@PathVariable Integer id){
		log.info("get department by id" +id);
		Department department = departmentService.getDepartmentById(id);
		if(department!=null) {
			return new ResponseEntity<Department>(department,HttpStatus.FOUND);
		}
		else {
			return new ResponseEntity<Department>(department,HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<Department> addDepartment(@RequestBody Department department){
		log.info("add department "+department.getEmployee());
		Department newDepartment = departmentService.addDepartment(department);
		if(newDepartment!=null) {
			return new ResponseEntity<Department>(newDepartment,HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Department>(newDepartment,HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Department> updateDepartment(@RequestBody Department department, @PathVariable Integer id){
		log.info("update deprtment "+id);
		Department updatedDepartment = departmentService.updateDepartment(department,id);
		if(updatedDepartment!=null) {
			return new ResponseEntity<Department>(updatedDepartment,HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Department>(updatedDepartment,HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public String deleteDepartment(@PathVariable Integer id){
		log.info("delete department "+id);
		
		if(departmentService.deleteDepartmnet(id)) {;
			return "employee deleted";
		}
		else {
			return "department not found";
		}
		
	}
	
	@PutMapping("/updateSalary")
	public String updateSalary(@RequestBody Employee employee) {
		log.info("update salary of "+employee);
		departmentService.updateSalary(employee.getSalary(), employee.getDepartment().get(0).getId());
		
//		departmentService.updateSalary(employee.getSalary(), employee.getDepartment().getId());
		return "salary updated";
	}
	
	

}
