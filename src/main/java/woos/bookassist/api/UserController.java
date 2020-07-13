package woos.bookassist.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woos.bookassist.api.request.UserRequest;
import woos.bookassist.common.exception.UserRegisterFailedException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    @PostMapping("/user/register")
    public void registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String fieldErrors = bindingResult.getFieldErrors().stream().map(f -> String.format("%s: %s", f.getField()
                    , f.getDefaultMessage())).collect(Collectors.joining(", "));
            throw new UserRegisterFailedException(fieldErrors);
        }
        // TODO: userService register
        log.debug("UserRequest {} register succeed.", userRequest);
    }
}
