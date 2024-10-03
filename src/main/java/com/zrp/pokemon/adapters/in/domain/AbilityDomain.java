package com.zrp.pokemon.adapters.in.domain;

import lombok.Builder;

@Builder
public record AbilityDomain(AbilityDataDomain ability, Boolean isHidden, Integer slot) {
}
