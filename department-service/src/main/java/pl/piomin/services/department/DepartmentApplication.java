package pl.piomin.services.department;


import io.jaegertracing.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

@SpringBootApplication
public class DepartmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentApplication.class, args);
	}
	
	@Bean
	DepartmentRepository repository() {
		DepartmentRepository repository = new DepartmentRepository();
		repository.add(new Department(1L, "Development"));
		repository.add(new Department(1L, "Operations"));
		repository.add(new Department(2L, "Development"));
		repository.add(new Department(2L, "Operations"));		
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
		return Configuration.fromEnv("department-service").withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
	}
}
