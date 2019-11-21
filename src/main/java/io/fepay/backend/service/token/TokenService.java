package io.fepay.backend.service.token;

public interface TokenService {

    String createAccessToken(String identity);

    String getIdentity(String jwt);

    String _generateToken(String identity, Long expireSeconds);

}
