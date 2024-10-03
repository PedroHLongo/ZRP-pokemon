package com.zrp.pokemon.application.ports.in.service;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;

public interface IGetPokemonByNameService {
    PokemonDomain execute(final String name);
}
