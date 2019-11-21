package io.fepay.backend.service.admin;

import io.fepay.backend.domain.entity.Admin;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AdminService {

    Mono signUp(Admin admin);

    Mono signIn(String adminId, String password);

    Mono findById(String email);

}
