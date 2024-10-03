package com.zrp.pokemon.application.ports.out.service.mocks;

import com.zrp.pokemon.adapters.in.domain.AbilityDataDomain;
import com.zrp.pokemon.adapters.in.domain.AbilityDomain;

import java.util.ArrayList;
import java.util.List;

public class AbilityDomainMock {
    public static List<AbilityDomain> getAbilitiesDomain() {
        return new ArrayList<>(List.of(
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("static").build()).build(),
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("lightning-rod").build()).build(),
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("static-lightning").build()).build()
        ));
    }

    public static List<AbilityDomain> getSortedAbilitiesDomain() {
        return new ArrayList<>(List.of(
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("lightning-rod").build()).build(),
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("static").build()).build(),
                AbilityDomain.builder().ability(AbilityDataDomain.builder().name("static-lightning").build()).build()
        ));
    }
}
