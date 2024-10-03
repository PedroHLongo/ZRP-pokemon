package com.zrp.pokemon.application.ports.out.service;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.in.mapper.IPokemonMapper;
import com.zrp.pokemon.adapters.out.entity.PokemonEntity;
import com.zrp.pokemon.application.ports.in.repository.IGetPokemonByNameRepository;
import com.zrp.pokemon.application.ports.in.service.IGetPokemonByNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetPokemonByNameService implements IGetPokemonByNameService {
    private final IPokemonMapper pokemonMapper;
    private final IGetPokemonByNameRepository getPokemonByNameRepository;

    @Override
    public PokemonDomain execute(String name) {
        log.info("Executing GetPokemonByNameService.");
        log.debug("Executing GetPokemonByNameService with name [{}].", name);

       final PokemonDomain pokemonDomain = this.pokemonMapper.entityToDomain(this.getPokemonByNameRepository
               .execute(name.toLowerCase()));

       log.info("Pokemon found.");
       log.debug("Pokemon found with details [{}]", pokemonDomain);

        log.info("Sorting abilities");
       pokemonDomain.abilities().sort(Comparator.comparing(abilityDomain -> abilityDomain.ability().name()));

       return pokemonDomain;
    }
}
