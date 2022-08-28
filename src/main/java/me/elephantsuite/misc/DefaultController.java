package me.elephantsuite.misc;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class DefaultController {

	@GetMapping
	public String getDefaultMsg() {
		return ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("welcomeMessageHtmlFile");
	}
}
