package pl.piomin.services.organization.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import pl.piomin.services.organization.model.Department;
import pl.piomin.services.organization.model.Employee;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.model.ResponseObject;
import pl.piomin.services.organization.repository.OrganizationRepository;


@RestController
public class OrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	OrganizationRepository repository;

	@Autowired
	RestTemplate restTemplate;
	
	@PostMapping
	public ResponseEntity<ResponseObject> add(@RequestBody Organization organization) {
		LOGGER.info("Organization add: {}", organization);
		return new ResponseEntity<>(new ResponseObject(repository.add(organization)), HttpStatus.OK);
	}
	
	@GetMapping("/organization/")
	public ResponseEntity<ResponseObject> findAll() {
		LOGGER.info("Organization find");
		return new ResponseEntity<>(new ResponseObject(repository.findAll()), HttpStatus.OK);
	}
	
	@GetMapping("/organization/{id}")
	public ResponseEntity<ResponseObject> findById(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		return new ResponseEntity<>(new ResponseObject(repository.findById(id)), HttpStatus.OK);
	}

	@GetMapping("/organization/{id}/with-departments")
	public ResponseEntity<ResponseObject> findByIdWithDepartments(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		String url = "http://department-service:8080/department/organization/"+id.toString();
		LOGGER.info("url {}",url);
		ResponseEntity<ResponseObject> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),ResponseObject.class);
		ResponseObject responseObject = response.getBody();
		LOGGER.info("response {}",responseObject.getData());
		//List<Department> departments = (List<Department>) response.getData();
		//organization.setDepartments(departments);
		return new ResponseEntity<>(new ResponseObject(organization), HttpStatus.OK);
	}
	
	@GetMapping("/organization/{id}/with-departments-and-employees")
	public ResponseEntity<ResponseObject> findByIdWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		ResponseEntity<ResponseObject> responseDepartment = restTemplate.exchange(
				"http://department-service:8080/department/organization/"+id.toString()+"/with-employees",
				HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()),
				ResponseObject.class);
		LOGGER.info("response {}",responseDepartment.toString());
		List<Department> departments = (List<Department>) responseDepartment.getBody().getData();
		organization.setDepartments(departments);
		return new ResponseEntity<>(new ResponseObject(organization), HttpStatus.OK);
	}
	
	@GetMapping("/organization/{id}/with-employees")
	public ResponseEntity<ResponseObject> findByIdWithEmployees(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		ResponseEntity<ResponseObject> response = restTemplate.exchange("http://employee-service:8080/employee/organization/"+id.toString()
				,HttpMethod.GET
				,new HttpEntity<>(new HttpHeaders())
				,ResponseObject.class);
		ResponseObject responseObject = response.getBody();
		LOGGER.info("**Get employee {}",responseObject.getData());
		return new ResponseEntity<>(new ResponseObject(organization), HttpStatus.OK);
	}
	
}
