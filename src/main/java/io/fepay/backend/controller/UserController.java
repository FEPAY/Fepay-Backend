package io.fepay.backend.controller;

import io.fepay.backend.domain.entity.User;
import io.fepay.backend.domain.payload.SignInForm;
import io.fepay.backend.domain.payload.SignUpForm;
import io.fepay.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public Mono<User> findByUserId(@RequestParam("user_id") String userId) {
        return userService.findByUserId(userId);
    }

    @PostMapping("/user/signup")
    public Mono signUp(@RequestBody SignUpForm signUpForm) {
        return userService.signUp(signUpForm.toUser());
    }

    @PostMapping("/user/auth")
    public Mono signIn(@RequestBody SignInForm signInForm) {
        return userService.signIn(signInForm.getUser_id(), signInForm.getPassword());
    }

}
