package woos.bookassist.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woos.bookassist.api.request.UserRequest;
import woos.bookassist.common.exception.UserRegisterFailedException;
import woos.bookassist.domain.user.repository.Users;
import woos.bookassist.domain.user.service.DatabaseUserDetailsService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    @Autowired
    DatabaseUserDetailsService databaseUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/user/register")
    public void registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String fieldErrors = bindingResult.getFieldErrors().stream().map(f -> String.format("%s: %s", f.getField()
                    , f.getDefaultMessage())).collect(Collectors.joining(", "));
            throw new UserRegisterFailedException(fieldErrors);
        }
        // if user id or email already exists
        if (databaseUserDetailsService.isUserIdOrEmailAlreadyExists(userRequest.getId(), userRequest.getEmail())) {
            throw new UserRegisterFailedException("user id or email already exists!");
        }
        log.debug("UserRequest {} validation succeed.", userRequest);

        Users user = Users.builder().id(userRequest.getId())
                .name(userRequest.getName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .build();
        Long savedUserNumber = databaseUserDetailsService.register(user);
        log.debug("Users table insert succeed. userNumber: {}", savedUserNumber);
    }
}
