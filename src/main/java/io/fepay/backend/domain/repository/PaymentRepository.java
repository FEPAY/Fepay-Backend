package io.fepay.backend.domain.repository;

import io.fepay.backend.domain.entity.Payment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {

    Flux<Payment> findAllByFestivalId(String festivalId);

    Mono<Payment> findBySenderAndRecipientAndClosed(String sender, String recipient, boolean closed);

    Mono<Payment> findByRecipientAndIsClosed(String recipient, boolean closed);

}
