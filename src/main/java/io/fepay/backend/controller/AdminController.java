package io.fepay.backend.controller;

import io.fepay.backend.domain.payload.AdminSignUpForm;
import io.fepay.backend.service.admin.AdminService;
import io.fepay.backend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/admin/signup")
    public Mono signUp(@RequestBody AdminSignUpForm signUpForm) {
        return adminService.signUp(signUpForm.toAdmin());
    }

    @PostMapping("/admin/auth")
    public Mono signIn(@RequestBody AdminSignUpForm signInForm) {
        return adminService.signIn(signInForm.getEmail(), signInForm.getPassword());
    }

    @GetMapping("/admin/me")
    public Mono getInfo(@RequestHeader("Authorization") String auth) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return adminService.findById(email);
    }

    @GetMapping("/festival")
    public Mono getFestivalName(@RequestParam("festival_id") String festivalId) {
        return adminService.findByFestivalId(festivalId);
    }

    @GetMapping("/festivals")
    public Flux getFestivals() {
        return adminService.findAllFestivals();
    }

}
