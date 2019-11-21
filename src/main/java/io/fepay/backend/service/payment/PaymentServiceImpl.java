package io.fepay.backend.service.payment;

import io.fepay.backend.domain.entity.Payment;
import io.fepay.backend.domain.repository.AdminRepository;
import io.fepay.backend.domain.repository.PaymentRepository;
import io.fepay.backend.domain.repository.UserRepository;
import io.fepay.backend.exception.InsufficientBalanceException;
import io.fepay.backend.exception.PaymentNotFoundException;
import io.fepay.backend.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public Flux findAllByAdminEmail(String email) {
        return adminRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMapMany(admin -> paymentRepository.findAllByFestivalId(admin.getFestivalId())
                                            .switchIfEmpty(Mono.error(new PaymentNotFoundException()))
                );
    }

    @Override
    public Mono payRequest(String userId, Payment payment) {
        payment.setRecipient(userId);
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(user -> {
                    payment.setFestivalId(user.getFestivalId());
                    return paymentRepository.save(payment);
                });
    }

    @Override
    public Mono payApprove(String senderId, String recipientId) {
        return userRepository.findById(senderId)
            .switchIfEmpty(Mono.error(new UserNotFoundException()))
            .flatMap(sender -> userRepository.findById(recipientId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(recipient -> paymentRepository.findByRecipientAndIsClosed(recipientId, false)
                    .switchIfEmpty(Mono.error(new PaymentNotFoundException()))
                    .flatMap(payment -> {
                        payment.setMatchedAt(LocalDateTime.now());
                        if (sender.getBalance() < payment.getAmount()) {
                            return Mono.error(new InsufficientBalanceException());
                        }
                        sender.setBalance(sender.getBalance() - payment.getAmount());
                        recipient.setBalance(recipient.getBalance() + payment.getAmount());
                        payment.setSender(senderId);
                        payment.setClosed(true);
                        return paymentRepository.save(payment)
                                .then(userRepository.save(sender))
                                .then(userRepository.save(recipient));
                    })
                )
            );
    }
}
