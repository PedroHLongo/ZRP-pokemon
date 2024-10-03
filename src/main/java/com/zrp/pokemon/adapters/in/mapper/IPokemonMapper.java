package com.zrp.pokemon.adapters.in.mapper;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.out.dto.PokemonResponse;
import com.zrp.pokemon.adapters.out.entity.PokemonEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface IPokemonMapper {
    PokemonDomain entityToDomain(final PokemonEntity entity);
    PokemonResponse domainToResponse(final PokemonDomain domain);
}
