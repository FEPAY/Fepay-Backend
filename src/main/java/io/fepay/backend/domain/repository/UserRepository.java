package io.fepay.backend.domain.repository;

import io.fepay.backend.domain.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findById(String userId);

    Flux<User> findAllByFestivalId(String festivalId);

    Flux<User> findTop5ByOrderByBalanceDesc(String festivalId);

}
