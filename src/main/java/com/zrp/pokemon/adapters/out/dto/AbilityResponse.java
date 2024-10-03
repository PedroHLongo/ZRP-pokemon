package com.zrp.pokemon.adapters.out.dto;

import lombok.Builder;

@Builder
public record AbilityResponse(AbilityDataResponse ability, Boolean isHidden, Integer slot) {
}
