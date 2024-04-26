package com.employee1.serviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.employee1.dao.DepartmentDao;
import com.employee1.dao.EmployeeDao;
import com.employee1.entity.Department;
import com.employee1.entity.Employee;

import com.employee1.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeSerciveImp implements EmployeeService {
	@Autowired
	private EmployeeDao empDao;
	
	@Autowired
	private DepartmentDao departmenDao;

	public List<Employee> getAllEmployee() {
		try {
			List<Employee> employeelist = empDao.findAll();
			return employeelist;
		} catch (Exception e) {
			log.error("exception " + e);
		}
		return null;
	}

	public Employee getEmployeeById(Integer id) {

		try {
			log.info("employee id " + id);
			Employee emp = empDao.findById(id).get();
			if(emp!=null) {
				return emp;
			}
		} catch (Exception e) {
			log.error("department not found with id " + e);
		}
		return null;
	}
	
	public Employee addEmployee(Employee employee) {
		log.info("employee object "+employee);
		
		List<String> employeeNames= new ArrayList<String>();
		
		for(Employee empObj:empDao.findAll()) {
			employeeNames.add(empObj.getFirstName().toLowerCase());
		}
		
		Employee newEmployee = new Employee();
		if(!employeeNames.contains(employee.getFirstName()) && !(employee.getFirstName()!=null)) {
			
			newEmployee.setFirstName(employee.getFirstName().toLowerCase());
			newEmployee.setLastName(employee.getLastName().toLowerCase());
			newEmployee.setAge(employee.getAge());
			newEmployee.setSalary(employee.getSalary());
	
		try {
			
			EmployeeSerciveImp employeeServiceImp=new EmployeeSerciveImp();
			List<Department> departments  = employeeServiceImp.departmentUpdateOrAdd(employee,departmenDao);
			newEmployee.setDepartment(departments);
			return empDao.save(newEmployee);
			
		} catch (Exception e) {
			log.error("exception "+e);
		}
		}
		
		return null;
	}
	
	


	public Employee updateEmployee(Employee employee, Integer id) {
		EmployeeSerciveImp employeeServiceImp=new EmployeeSerciveImp();
		try {
			log.info("Employee object " + employee + "employee id " + id);
			Employee updatedEmployee = empDao.findById(id).get();
			log.info("employee object " + updatedEmployee);
			if (updatedEmployee != null) {
				if (employee.getFirstName() != null) {
					updatedEmployee.setFirstName(employee.getFirstName().toLowerCase());
				}
				if (employee.getLastName() != null) {
					updatedEmployee.setLastName(employee.getLastName().toLowerCase());
				}
				if (employee.getDepartment() != null) {
					updatedEmployee.setDepartment(employeeServiceImp.departmentUpdateOrAdd(employee, departmenDao));
				}
				if (employee.getAge() != 0) {
					updatedEmployee.setAge(employee.getAge());
				}
				if (employee.getSalary() != 0) {
					updatedEmployee.setSalary(employee.getSalary());
				}
				log.info("updated employee "+updatedEmployee);
				return empDao.save(updatedEmployee);
			}
		} catch (Exception e) {
			log.error("exception occurred " + e);
		}

		return null;
	}

	//delete employee from database from their id
	public Boolean deleteEmployee(Integer id) {
		try {
			Optional<Employee> employee = empDao.findById(id);
			log.info("employee id " + id);
			if(employee.isPresent()) {
				empDao.deleteById(id);
				return true;
			}

		} catch (Exception e) {
			log.error("exception " + e);
		}
		return false;
	}
	
	

	//check department if department not exist in database then new department will be created 
	public List<Department> departmentUpdateOrAdd(Employee employee, DepartmentDao depDao){
		
		List<Department> departments = new ArrayList<Department>();
		for(Department department:employee.getDepartment()) {
			Department dep = depDao.findByName(department.getName());
			if(dep==null) {
				dep=new Department();
				dep.setName(department.getName().toLowerCase());
				depDao.save(dep);
			}
			departments.add(dep);
		}
		return departments;
	}

}
