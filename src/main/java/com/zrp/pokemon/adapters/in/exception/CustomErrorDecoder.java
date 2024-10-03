package com.zrp.pokemon.adapters.in.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException();
            case 404 -> new PokemonNotFoundException();
            case 500 -> new InternalServerErrorException();
            default -> new Exception();
        };
    }
}
