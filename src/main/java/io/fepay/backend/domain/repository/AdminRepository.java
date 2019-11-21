package io.fepay.backend.domain.repository;

import io.fepay.backend.domain.entity.Admin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AdminRepository extends ReactiveMongoRepository<Admin, String> {

    Mono<Admin> findByEmailAndFestivalId(String email, String festivalId);

    Mono<Admin> findByEmail(String email);

    Mono<Admin> findByFestivalId(String festivalId);

}
