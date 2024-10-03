package com.zrp.pokemon.adapters.in.rest;

import com.zrp.pokemon.adapters.in.exception.PokemonNotFoundException;
import com.zrp.pokemon.adapters.in.exception.domain.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(PokemonNotFoundException.class)
    ResponseEntity<Void> pokemonNotFoundExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Void> genericExceptionHandler(final Exception exception) {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<Error> methodValidationExceptionHandler() {
        final Error error = Error.builder()
                .message(this.messageSource.getMessage("field.not-blank", null, Locale.getDefault()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
