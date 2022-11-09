package me.elephantsuite.admin;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService service;

    @PostMapping("resetTos")
    public Response resetTos(@RequestBody AdminRequest.AuthRequest request) {
        return service.resetTos(request);
    }
}
