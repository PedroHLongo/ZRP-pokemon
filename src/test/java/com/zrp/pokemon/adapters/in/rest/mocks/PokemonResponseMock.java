package com.zrp.pokemon.adapters.in.rest.mocks;

import com.zrp.pokemon.adapters.out.dto.AbilityResponse;
import com.zrp.pokemon.adapters.out.dto.PokemonResponse;

import java.util.List;

public class PokemonResponseMock {
    public static PokemonResponse getPokemonResponseMock(final Long id, final String name,
                                                         final List<AbilityResponse> abilities) {
        return PokemonResponse.builder()
                .id(id)
                .name(name)
                .abilities(abilities)
                .build();
    }
}
