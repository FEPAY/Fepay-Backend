package io.fepay.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid signature in token.")
public class InvalidTokenSignatureException extends RuntimeException {
}
