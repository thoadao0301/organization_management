package pl.piomin.services.employee;

import io.jaegertracing.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

@SpringBootApplication
public class EmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}
	
	@Bean
	EmployeeRepository repository() {
		EmployeeRepository repository = new EmployeeRepository();
		repository.add(new Employee(1L, 1L, "John Smith", 34, "Analyst"));
		repository.add(new Employee(1L, 1L, "Darren Hamilton", 37, "Manager"));
		repository.add(new Employee(1L, 1L, "Tom Scott", 26, "Developer"));
		repository.add(new Employee(1L, 2L, "Anna London", 39, "Analyst"));		
		repository.add(new Employee(1L, 2L, "Patrick Dempsey", 27, "Developer"));
		repository.add(new Employee(2L, 3L, "Kevin Price", 38, "Developer"));		
		repository.add(new Employee(2L, 3L, "Ian Scott", 34, "Developer"));
		repository.add(new Employee(2L, 3L, "Andrew Campton", 30, "Manager"));
		repository.add(new Employee(2L, 4L, "Steve Franklin", 25, "Developer"));
		repository.add(new Employee(2L, 4L, "Elisabeth Smith", 30, "Developer"));
		return repository;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public io.opentracing.Tracer initTracer() {
		Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration().withType("const").withParam(1);
		Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
		return Configuration.fromEnv("employee-service").withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
	}
}
