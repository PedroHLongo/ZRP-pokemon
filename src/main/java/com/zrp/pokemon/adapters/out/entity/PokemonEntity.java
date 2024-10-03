package com.zrp.pokemon.adapters.out.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record PokemonEntity(Long id, String name, List<AbilityEntity> abilities) {
}
