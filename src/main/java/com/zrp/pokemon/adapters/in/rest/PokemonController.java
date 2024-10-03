package com.zrp.pokemon.adapters.in.rest;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.in.mapper.IPokemonMapper;
import com.zrp.pokemon.adapters.out.dto.PokemonResponse;
import com.zrp.pokemon.application.ports.in.service.IGetPokemonByNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pokemons")
@RequiredArgsConstructor
@Slf4j
public class PokemonController {

    private final IPokemonMapper pokemonMapper;
    private final IGetPokemonByNameService getPokemonByNameService;

    @GetMapping("/{name}")
    ResponseEntity<PokemonResponse> getByName(@PathVariable final String name) {
        log.info("Fetching pokemon by name.");
        log.debug("Fetching pokemon by name [{}].", name);

        final PokemonDomain pokemonFound = this.getPokemonByNameService.execute(name);
        log.info("Pokemon found.");
        log.debug("Pokemon found with details [{}].", pokemonFound);

        return ResponseEntity.ok(this.pokemonMapper.domainToResponse(pokemonFound));
    }
}
