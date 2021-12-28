package pl.piomin.services.department.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.model.Employee;
import pl.piomin.services.department.model.ResponseObject;
import pl.piomin.services.department.repository.DepartmentRepository;

import java.util.Arrays;
import java.util.List;

@RestController
public class DepartmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	DepartmentRepository repository;
	@Autowired
	RestTemplate restTemplate;

	public DepartmentController(DepartmentRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/department/")
	public ResponseEntity<ResponseObject> add(@RequestBody Department department, @RequestHeader HttpHeaders headers) {
		LOGGER.info("Department add: {}", department);
		return new ResponseEntity<>(new ResponseObject(repository.add(department)), HttpStatus.OK);
	}
	
	@GetMapping("/department/{id}")
	public ResponseEntity<ResponseObject> findById(@PathVariable("id") Long id) {
		LOGGER.info("Department find: id={}", id);
		return new ResponseEntity<>(new ResponseObject(repository.findById(id)), HttpStatus.OK);
	}
	
	@GetMapping("/department/")
	public ResponseEntity<ResponseObject> findAll() {
		LOGGER.info("Department find");
		return new ResponseEntity<>(new ResponseObject(repository.findAll()), HttpStatus.OK);
	}

	@GetMapping("/department/organization/{organizationId}")
	public ResponseEntity<ResponseObject> findByOrganization(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);
		return new ResponseEntity<>(new ResponseObject(departments), HttpStatus.OK);
	}

	@GetMapping("/department/organization/{organizationId}/with-employees")
	public ResponseEntity<ResponseObject> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);

		departments.forEach(d -> {
			ResponseEntity<ResponseObject> response = restTemplate.exchange(
					"http://employee-service:8080/employee/department/"+d.getId().toString(), HttpMethod.GET,
					new HttpEntity<>(new HttpHeaders()),ResponseObject.class);
			ResponseObject responseObject = response.getBody();
			LOGGER.info("response {}",responseObject.getData());
			List<Employee> employees = (List<Employee>) responseObject.getData();
			d.setEmployees(employees);
		});
		return new ResponseEntity<>(new ResponseObject(departments), HttpStatus.OK);
	}
	
}
