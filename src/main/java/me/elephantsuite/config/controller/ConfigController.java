package me.elephantsuite.config.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "config")
@AllArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping
    public Response getConfigValues() {
        return configService.getConfigValues();
    }
}
