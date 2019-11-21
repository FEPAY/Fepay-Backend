package io.fepay.backend.service.user;

import io.fepay.backend.domain.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface UserService {

    Mono<User> findByUserId(String userId);

    Mono signUp(User user);

    Mono signIn(String userId, String password);

    Flux<User> findAllByAdminEmail(String email);

    Flux<User> getTop5UserRank(String email);

    Mono joinFestival(String userId, String festivalId);

    Mono delete(String email, String userId);

    Mono setBalance(String email, String userId, String balance);

}
