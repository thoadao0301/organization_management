package pl.piomin.services.employee.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.piomin.services.employee.model.Employee;

public class EmployeeRepository {

	private List<Employee> employees = new ArrayList<>();
	
	public Employee add(Employee employee) {
		employee.setId((long) (employees.size()+1));
		employees.add(employee);
		return employee;
	}
	
	public Employee findById(Long id) {
		return employees.stream()
				.filter(a -> a.getId().equals(id))
				.findFirst()
				.orElseThrow();
	}
	
	public List<Employee> findAll() {
		return employees;
	}

	public List<Employee> findByDepartment(Long departmentId) {
		List<Employee> find = new ArrayList<>();
		for (Employee employee : employees){
			if (employee.getDepartmentId().equals(departmentId)){
				find.add(employee);
			}
		}
		return find;
	}

	public List<Employee> findByOrganization(Long organizationId) {
		List<Employee> find = new ArrayList<>();
		for (Employee employee : employees){
			if (employee.getDepartmentId().equals(organizationId)){
				find.add(employee);
			}
		}
		return find;
	}
	
}
