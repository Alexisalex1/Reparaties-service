package nl.backend.reparatieservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("nl.backend.reparatieservice.model")
@EnableJpaRepositories("nl.backend.reparatieservice.repository")

public class ReparatieserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReparatieserviceApplication.class, args);
	}

}
