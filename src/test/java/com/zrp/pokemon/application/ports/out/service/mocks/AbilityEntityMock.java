package com.zrp.pokemon.application.ports.out.service.mocks;

import com.zrp.pokemon.adapters.out.entity.AbilityDataEntity;
import com.zrp.pokemon.adapters.out.entity.AbilityEntity;

import java.util.List;

public class AbilityEntityMock {
    public static List<AbilityEntity> getAbilitiesEntity() {
        return List.of(
                AbilityEntity.builder().ability(AbilityDataEntity.builder().name("static").build()).build(),
                AbilityEntity.builder().ability(AbilityDataEntity.builder().name("lightning-rod").build()).build(),
                AbilityEntity.builder().ability(AbilityDataEntity.builder().name("static-lightning").build()).build()
        );
    }
}
