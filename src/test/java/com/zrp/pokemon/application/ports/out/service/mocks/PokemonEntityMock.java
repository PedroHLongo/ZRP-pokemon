package com.zrp.pokemon.application.ports.out.service.mocks;

import com.zrp.pokemon.adapters.out.entity.PokemonEntity;

public class PokemonEntityMock {
    public static PokemonEntity getPokemonEntity(final Long id, final String name) {
        return PokemonEntity.builder()
                .id(id)
                .name(name)
                .abilities(AbilityEntityMock.getAbilitiesEntity())
                .build();
    }
}
