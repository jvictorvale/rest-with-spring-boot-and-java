package br.com.jvictorvale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullExeception extends RuntimeException {

    public RequiredObjectIsNullExeception() {
        super("It is not allowed to persist a null object!");
    }

    public RequiredObjectIsNullExeception(String message) {
        super(message);
    }
}
