package br.com.jvictorvale.com.jvictorvale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundExeception extends RuntimeException {

    public ResourceNotFoundExeception(String message) {
        super(message);
    }
}
