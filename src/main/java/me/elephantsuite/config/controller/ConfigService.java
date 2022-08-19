package me.elephantsuite.config.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ConfigService {

    public Response getConfigValues() {
        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Retrieved Backend Config!")
            .addObject("values", ElephantBackendApplication.ELEPHANT_CONFIG.getConfigValues())
            .build();
    }
}
