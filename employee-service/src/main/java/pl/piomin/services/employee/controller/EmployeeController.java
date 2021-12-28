package pl.piomin.services.employee.controller;


import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.model.ResponseObject;
import pl.piomin.services.employee.repository.EmployeeRepository;


@RestController
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeRepository repository;
	
	@PostMapping("/employee/")
	public ResponseEntity<ResponseObject> add(@RequestBody Employee employee) {
		LOGGER.info("Employee add: {}", employee);
		return new ResponseEntity<>( new ResponseObject(repository.add(employee)), HttpStatus.OK);
	}
	
	@GetMapping("/employee/{id}")
	public ResponseEntity<ResponseObject> findById(@PathVariable("id") Long id) {
		LOGGER.info("Employee find: id={}", id);
		return new ResponseEntity<>(new ResponseObject(repository.findById(id)),HttpStatus.OK);
	}
	
	@GetMapping("/employee/")
	public ResponseEntity<ResponseObject> findAll() {
		LOGGER.info("Employee find");
		return new ResponseEntity<>(new ResponseObject(repository.findAll()), HttpStatus.OK);
	}

	@GetMapping("/employee/department/{departmentId}")
	public ResponseEntity<ResponseObject> findByDepartment(@PathVariable("departmentId") Long departmentId) {
		LOGGER.info("Employee find: departmentId={}", departmentId);
		return new ResponseEntity<>(new ResponseObject(repository.findByDepartment(departmentId)), HttpStatus.OK);
	}

	@GetMapping("/employee/organization/{organizationId}")
	public ResponseEntity<ResponseObject> findByOrganization(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Employee find: organizationId={}", organizationId);
		return new ResponseEntity<>(new ResponseObject(repository.findByOrganization(organizationId)), HttpStatus.OK);
	}
	
}
