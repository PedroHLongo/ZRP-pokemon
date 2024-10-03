package com.zrp.pokemon.application.ports.out.service.mocks;

import com.zrp.pokemon.adapters.in.domain.AbilityDomain;
import com.zrp.pokemon.adapters.in.domain.PokemonDomain;

import java.util.List;

public class PokemonDomainMock {
    public static PokemonDomain getPokemonDomain(final Long id, final String name,
                                                 final List<AbilityDomain> abilities) {
        return PokemonDomain.builder()
                .id(id)
                .name(name)
                .abilities(abilities)
                .build();
    }
}
