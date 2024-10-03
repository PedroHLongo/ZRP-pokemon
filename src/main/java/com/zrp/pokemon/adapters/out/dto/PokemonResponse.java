package com.zrp.pokemon.adapters.out.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PokemonResponse(Long id, String name, List<AbilityResponse> abilities) { }
