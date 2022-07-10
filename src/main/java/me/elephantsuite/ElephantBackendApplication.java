package me.elephantsuite;

import me.elephantsuite.config.PropertiesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElephantBackendApplication {

	public static Logger LOGGER = LogManager.getLogger(ElephantBackendApplication.class);

	public static final PropertiesHandler ELEPHANT_CONFIG = PropertiesHandler
		.builder()
		.addConfigOption("isDevelopment", "true")
		.addConfigOption("senderEmailAddress", "no-reply@elephantsuite.me")
		.addConfigOption("elephantDomain", "http://localhost:8080")
		.setFileName("elephant-config.properties")
		.build();

	public static void main(String[] args) {
		SpringApplication.run(ElephantBackendApplication.class, args);
	}

}
