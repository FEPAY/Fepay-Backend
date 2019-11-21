package io.fepay.backend.service.user;

import io.fepay.backend.domain.entity.User;
import io.fepay.backend.domain.payload.TokenResponse;
import io.fepay.backend.domain.repository.AdminRepository;
import io.fepay.backend.domain.repository.UserRepository;
import io.fepay.backend.exception.InvalidUserCredentialException;
import io.fepay.backend.exception.UserAlreadyExistsException;
import io.fepay.backend.exception.UserNotFoundException;
import io.fepay.backend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<User> findByUserId(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
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

    @Override
    public Flux<User> findAllByAdminEmail(String email) {
        return adminRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMapMany(admin -> userRepository.findAllByFestivalId(admin.getFestivalId()));
    }

    @Override
    public Flux<User> getTop5UserRank(String email) {
        return adminRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMapMany(admin -> userRepository.findTop5ByOrderByBalanceDesc(admin.getFestivalId()));
    }

    @Override
    public Mono joinFestival(String userId, String festivalId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(user -> {
                    user.setFestivalId(festivalId);
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono delete(String email, String userId) {
        return adminRepository.findById(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(admin -> userRepository.findById(userId)
                        .switchIfEmpty(Mono.error(new UserNotFoundException()))
                        .flatMap(user -> userRepository.delete(user)));
    }

    @Override
    public Mono setBalance(String email, String userId, String balance) {
        return adminRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(admin -> userRepository.findById(userId)
                            .switchIfEmpty(Mono.error(new UserNotFoundException()))
                            .flatMap(user -> {
                                user.setBalance(Integer.parseInt(balance));
                                return userRepository.save(user);
                            })
                );
    }

}
