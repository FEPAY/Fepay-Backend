package io.fepay.backend.service.admin;

import io.fepay.backend.domain.entity.Admin;
import io.fepay.backend.domain.payload.TokenResponse;
import io.fepay.backend.domain.repository.AdminRepository;
import io.fepay.backend.exception.InvalidUserCredentialException;
import io.fepay.backend.exception.UserAlreadyExistsException;
import io.fepay.backend.exception.UserNotFoundException;
import io.fepay.backend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TokenService tokenService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono signUp(Admin admin) {
        String random = numberGen(6, 2);
        admin.setFestivalId(random);
        admin.encodePassword(passwordEncoder);
        return adminRepository.findByEmail(admin.getEmail())
                .handle((saved, sink) -> {
                    sink.error(new UserAlreadyExistsException());
                }).switchIfEmpty(adminRepository.save(admin));
    }

    @Override
    public Mono signIn(String adminId, String password) {
        return adminRepository.findById(adminId)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(new TokenResponse(tokenService.createAccessToken(adminId)));
                    } else {
                        return Mono.error(new InvalidUserCredentialException());
                    }
                }).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono findById(String email) {
        return adminRepository.findByEmail(email);
    }

    /**
     * 전달된 파라미터에 맞게 난수를 생성한다
     * @param len : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     *
     * Created by 닢향
     * http://niphyang.tistory.com
     */
    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = "";

        for(int i=0;i<len;i++) {
            String ran = Integer.toString(rand.nextInt(10));
            if(dupCd==1) {
                numStr += ran;
            }else if(dupCd==2) {
                if(!numStr.contains(ran)) {
                    numStr += ran;
                }else {
                    i-=1;
                }
            }
        }
        return numStr;
    }


}
