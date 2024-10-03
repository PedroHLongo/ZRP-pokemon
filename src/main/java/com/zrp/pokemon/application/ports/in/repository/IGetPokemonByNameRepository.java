package com.zrp.pokemon.application.ports.in.repository;

import com.zrp.pokemon.adapters.out.entity.PokemonEntity;
import com.zrp.pokemon.configuration.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "getPokemonByName", url = "https://pokeapi.co/api/v2/", configuration = FeignConfig.class)
public interface IGetPokemonByNameRepository {

    @GetMapping(value = "pokemon/{name}", produces = "application/json")
    PokemonEntity execute(@PathVariable("name") final String name);
}
