package com.zrp.pokemon.adapters.in.rest;

import com.zrp.pokemon.adapters.in.exception.PokemonNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException.class)
    ResponseEntity<Void> pokemonNotFoundExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Void> genericExceptionHandler() {
        return ResponseEntity.internalServerError().build();
    }
}
