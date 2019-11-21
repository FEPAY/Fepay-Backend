package io.fepay.backend.controller;

import io.fepay.backend.domain.payload.PaymentForm;
import io.fepay.backend.service.payment.PaymentService;
import io.fepay.backend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    @Autowired
    TokenService tokenService;

    @Autowired
    PaymentService paymentService;

    @GetMapping("/payments")
    public Flux findALlByAdminEmail(@RequestHeader("Authorization") String auth) {
        String email = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return paymentService.findAllByAdminEmail(email);
    }

    @PostMapping("/pay")
    public Mono payRequest(@RequestHeader("Authorization") String auth, @RequestBody PaymentForm paymentForm) {
        String userId = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return paymentService.payRequest(userId, paymentForm.toPayment());
    }

    @PatchMapping("/pay")
    public Mono payApprove(@RequestHeader("Authorization") String auth, @RequestParam("recipient_id") String recipientId) {
        String userId = tokenService.getIdentity(auth.replace("Bearer ", ""));
        return paymentService.payApprove(userId, recipientId);
    }

}
