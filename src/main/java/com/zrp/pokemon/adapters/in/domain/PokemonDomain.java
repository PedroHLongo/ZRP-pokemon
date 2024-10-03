package com.zrp.pokemon.adapters.in.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record PokemonDomain(Long id, String name, List<AbilityDomain> abilities) {
}
