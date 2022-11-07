package me.elephantsuite.admin;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final ElephantUserService userService;

    private final BCryptPasswordEncoder encoder;

    public Response resetTos(AdminRequest.AuthRequest request) {
        long id = request.getId();
        String password = request.getPassword();

        ElephantUser user = userService.getUserById(id);

        if (user == null) {
            throw new InvalidIdException(request, InvalidIdType.USER);
        }

        if (!encoder.matches(password, user.getPassword())) {
            return ResponseBuilder
                    .create()
                    .addResponse(ResponseStatus.FAILURE, "Invalid Password!")
                    .addObject("request", request)
                    .build();
        }

        userService.resetTos();

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Reset TOS for all Users!")
                .build();


    }
}
