package com.zrp.pokemon.adapters.in.rest.mocks;

import com.zrp.pokemon.adapters.out.dto.AbilityDataResponse;
import com.zrp.pokemon.adapters.out.dto.AbilityResponse;

import java.util.List;

public class AbilityResponseMock {
    public static List<AbilityResponse> getAbilitiesMock() {
        return List.of(
                AbilityResponse.builder().ability(AbilityDataResponse.builder().name("lightning-rod").url("url1").build())
                        .isHidden(Boolean.FALSE).slot(1).build(),
                AbilityResponse.builder().ability(AbilityDataResponse.builder().name("static").url("url2").build())
                        .isHidden(Boolean.TRUE).slot(2).build(),
                AbilityResponse.builder().ability(AbilityDataResponse.builder().name("static-lightning").url("url3").build())
                        .isHidden(Boolean.FALSE).slot(3).build()
        );
    }
}
