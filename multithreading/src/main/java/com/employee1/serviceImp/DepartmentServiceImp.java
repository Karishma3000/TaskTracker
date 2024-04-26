package com.employee1.serviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee1.dao.DepartmentDao;
import com.employee1.dao.EmployeeDao;
import com.employee1.entity.Department;
import com.employee1.entity.Employee;
import com.employee1.service.DepartmentService;
import com.employee1.service.EmployeeService;
import com.employee1.service.UserThread;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DepartmentServiceImp implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeDao employeeDao;
	@Override
	public List<Department> getAllDepartment() {
		
		return departmentDao.findAll();
	}

	@Override
	public Department getDepartmentById(Integer id) {
		try {
			log.info("department id "+id);
			return departmentDao.findById(id).get();
		} catch (Exception e) {
			log.error("exception "+e);
		}
		return null;
	}

	@Override
	public Department addDepartment(Department department) {
		try {
			log.info("department object "+department.getEmployee());
			return departmentDao.save(department);
		} catch (Exception e) {
			log.error("exception "+e);
		}
		return department;
	}

	@Override
	public Department updateDepartment(Department department, Integer id) {
		try {
			log.info("department object"+department+"department id "+id);
			Department dep = departmentDao.findById(id).get();
			dep.setName(department.getName());
			return departmentDao.save(dep);
		} catch (Exception e) {
			log.error("excetion "+e);
		}
		return null;
	}

	@Override
	public Boolean deleteDepartmnet(Integer id) {
		try {
			Optional<Department> department = departmentDao.findById(id);
			if(department.isPresent()) {
				departmentDao.deleteById(id);
				return true;
			}
		} catch (Exception e) {
			log.error("exception "+e);
		}	
		return false;
	}
	
	@Override
	public void updateSalary(Long salary, Integer id) {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(5);
			Department dep = departmentDao.findById(id).get();
			
			List<Employee> listEmployee =dep.getEmployee();
			
			 for (Employee employee:listEmployee) { 
		            UserThread userThread = new UserThread(employeeService,salary,employee);  
		            executor.execute(userThread); 
		            
		          } 
			 executor.shutdown();  
		        while (!executor.isTerminated()) {   }  	
		} catch (Exception e) {
		log.error("exception "+e);
		}
	}
	
	
}
