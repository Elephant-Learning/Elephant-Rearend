package me.elephantsuite;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import me.elephantsuite.config.PropertiesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ElephantBackendApplication {

	public static Logger LOGGER = LogManager.getLogger(ElephantBackendApplication.class);

	public static final PropertiesHandler ELEPHANT_CONFIG = PropertiesHandler
		.builder()
		.addConfigOption("isDevelopment", true)
		.addConfigOption("senderEmailAddress", "no-reply@elephantsuite.net")
		.addConfigOption("elephantDomain", "localhost:8080")
		.addConfigOption("pfpIdMax", 47)
		.addConfigOption("recentlyViewedDecksMax", 50)
		.addConfigOption("tokenExpiredLimitMinutes", 15)
		.setFileName("elephant-config.properties")
		.addConfigOption("welcomeMessageHtmlFile", "WelcomeRedirectMessage.html")
		.addConfigOption("confirmationEmailHtmlFile", "ConfirmAccountEmail.html")
		.addConfigOption("forgotPasswordEmailHtmlFile", "ForgotPasswordEmail.html")
		.addConfigOption("friendEmailHtmlFile", "FriendEmailHtmlFile.html")
		.addConfigOption("inviteEmailHtmlFile", "InviteEmailHtmlFile.html")
		.build();

	public static final PropertiesHandler AI_INTEGRATION = PropertiesHandler
		.builder()
		.addConfigOption("chatGptApiKey", "")
		.setFileName("ai-integration.properties")
		.build();

	public static void main(String[] args) {
		SpringApplication.run(ElephantBackendApplication.class, args);
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedOrigins("*")
					.allowedMethods("*");
			}
		};
	}
}
