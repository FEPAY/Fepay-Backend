package io.fepay.backend.service.user;

import io.fepay.backend.domain.entity.User;
import io.fepay.backend.domain.payload.TokenResponse;
import io.fepay.backend.domain.repository.UserRepository;
import io.fepay.backend.exception.InvalidUserCredentialException;
import io.fepay.backend.exception.UserAlreadyExistsException;
import io.fepay.backend.exception.UserNotFoundException;
import io.fepay.backend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<User> findByUserId(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono signUp(User user) {
        user.encodePassword(passwordEncoder);
        return userRepository.findById(user.getId())
                .handle((saved, sink) -> {
                    sink.error(new UserAlreadyExistsException());
                }).switchIfEmpty(userRepository.save(user));
    }

    @Override
    public Mono signIn(String userId, String password) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(new TokenResponse(tokenService.createAccessToken(userId)));
                    } else {
                        return Mono.error(new InvalidUserCredentialException());
                    }
                }).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

}
