package io.fepay.backend.controller;

import io.fepay.backend.domain.entity.User;
import io.fepay.backend.domain.payload.SetBalance;
import io.fepay.backend.domain.payload.SignInForm;
import io.fepay.backend.domain.payload.SignUpForm;
import io.fepay.backend.service.token.TokenService;
import io.fepay.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/user")
    public Mono<User> findByUserId(@RequestParam("user_id") String userId) {
        return userService.findByUserId(userId);
    }

    @DeleteMapping("/user")
    public Mono delete(@RequestHeader("Authorization") String auth, @RequestParam("user_id") String userId) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return userService.delete(email, userId);
    }

    @GetMapping("/user/me")
    public Mono<User> getInfo(@RequestHeader("Authorization") String auth) {
        String userId = tokenService.getIdentity(auth.replace("Bearer ", ""));
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

    @PutMapping("/user/balance")
    public Mono setBalance(@RequestHeader("Authorization") String auth, @RequestParam("user_id") String userId, @RequestBody SetBalance balance) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return userService.setBalance(email, userId, balance.getBalance());
    }

    @GetMapping("/users")
    public Flux findAll(@RequestHeader("Authorization") String auth) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return userService.findAllByAdminEmail(email);
    }

    @GetMapping("/users/rank")
    public Flux<User> getTop5UserRank(@RequestHeader("Authorization") String auth) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return userService.getTop5UserRank(email);
    }

    @PatchMapping("/user/join")
    public Mono joinFestival(@RequestHeader("Authorization") String auth, @RequestParam("festival_id") String festivalId) {
        String userId = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return userService.joinFestival(userId, festivalId);
    }

}
