package io.fepay.backend.service.user;

import io.fepay.backend.domain.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface UserService {

    Mono<User> findByUserId(String userId);

    Mono signUp(User user);

    Mono signIn(String userId, String password);

}
