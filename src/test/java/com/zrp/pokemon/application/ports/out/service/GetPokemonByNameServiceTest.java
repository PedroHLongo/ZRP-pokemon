package com.zrp.pokemon.application.ports.out.service;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.in.mapper.IPokemonMapper;
import com.zrp.pokemon.adapters.out.entity.PokemonEntity;
import com.zrp.pokemon.application.ports.in.repository.IGetPokemonByNameRepository;
import com.zrp.pokemon.application.ports.out.service.mocks.AbilityDomainMock;
import com.zrp.pokemon.application.ports.out.service.mocks.PokemonDomainMock;
import com.zrp.pokemon.application.ports.out.service.mocks.PokemonEntityMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class GetPokemonByNameServiceTest {
    @InjectMocks
    GetPokemonByNameService service;

    @Mock
    private IPokemonMapper pokemonMapper;
    @Mock
    private IGetPokemonByNameRepository repository;

    @Test
    @DisplayName("Should return a pokemon domain when call to repository is success")
    void execute_shouldReturnPokemonDomain_whenSuccessCallingRepository() {
        //Arrange
        final Long pokemonId = 1L;
        final String pokemonName = "Pikachu";

        final PokemonEntity pokemonEntityMock = PokemonEntityMock.getPokemonEntity(pokemonId, pokemonName);
        final PokemonDomain pokemonDomainMock = PokemonDomainMock.getPokemonDomain(pokemonId, pokemonName,
                AbilityDomainMock.getAbilitiesDomain());
        final PokemonDomain expectedReturn = PokemonDomainMock.getPokemonDomain(pokemonId, pokemonName,
                AbilityDomainMock.getSortedAbilitiesDomain());

        when(this.repository.execute(pokemonName.toLowerCase())).thenReturn(pokemonEntityMock);
        when(this.pokemonMapper.entityToDomain(pokemonEntityMock)).thenReturn(pokemonDomainMock);

        //Act
        final PokemonDomain result = this.service.execute(pokemonName);

        //Assert
        verify(this.repository, times(1)).execute(pokemonName.toLowerCase());
        verify(this.pokemonMapper, times(1)).entityToDomain(pokemonEntityMock);
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedReturn);
    }
}