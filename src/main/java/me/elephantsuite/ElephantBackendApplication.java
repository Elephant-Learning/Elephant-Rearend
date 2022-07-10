package me.elephantsuite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElephantBackendApplication {

	public static Logger LOGGER = LogManager.getLogger(ElephantBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ElephantBackendApplication.class, args);
	}

}
