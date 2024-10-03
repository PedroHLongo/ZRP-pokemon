package com.zrp.pokemon.adapters.out.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AbilityEntity(AbilityDataEntity ability, @JsonProperty("is_hidden") Boolean isHidden, Integer slot) {
}
