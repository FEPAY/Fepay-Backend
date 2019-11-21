package io.fepay.backend.service.payment;

import io.fepay.backend.domain.entity.Payment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface PaymentService {

    Flux findAllByAdminEmail(String email);

    Mono payRequest(String userId, Payment payment);

    Mono payApprove(String senderId, String recipientId);

}
