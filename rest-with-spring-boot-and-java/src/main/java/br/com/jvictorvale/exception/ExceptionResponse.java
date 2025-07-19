package br.com.jvictorvale.exception;

import br.com.jvictorvale.model.Greeting;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
