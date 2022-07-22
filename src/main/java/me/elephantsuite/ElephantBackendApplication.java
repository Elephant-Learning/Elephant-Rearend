package me.elephantsuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
