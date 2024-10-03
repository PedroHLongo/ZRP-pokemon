package com.zrp.pokemon.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.in.exception.PokemonNotFoundException;
import com.zrp.pokemon.adapters.in.exception.domain.Error;
import com.zrp.pokemon.adapters.in.mapper.IPokemonMapper;
import com.zrp.pokemon.adapters.in.rest.mocks.AbilityResponseMock;
import com.zrp.pokemon.adapters.in.rest.mocks.PokemonResponseMock;
import com.zrp.pokemon.adapters.out.dto.PokemonResponse;
import com.zrp.pokemon.application.ports.in.service.IGetPokemonByNameService;
import com.zrp.pokemon.application.ports.out.service.mocks.AbilityDomainMock;
import com.zrp.pokemon.application.ports.out.service.mocks.PokemonDomainMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebMvcTest(PokemonController.class)
@Tag("integration")
class PokemonControllerTest {
    @MockBean
    private IPokemonMapper pokemonMapper;
    @MockBean
    private IGetPokemonByNameService getPokemonByNameService;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().findAndAddModules().build();
    private static final String POKEMON_NAME = "Pikachu";
    private static final String URL = "/api/pokemons";

    @Test
    @DisplayName("Should return status 200 and a pokemon response with its abilities sorted by name with success")
    void getById_shouldReturn200AndPokemonResponseWithAbilitiesSortedByName() throws Exception {
        //Arrange
        final PokemonDomain pokemonDomainMock = PokemonDomainMock.getPokemonDomain(
                1L,
                POKEMON_NAME,
                AbilityDomainMock.getSortedAbilitiesDomain());
        final PokemonResponse expectedResponse = PokemonResponseMock.getPokemonResponseMock(1L,
                POKEMON_NAME,
                AbilityResponseMock.getAbilitiesMock());

        when(this.getPokemonByNameService.execute(POKEMON_NAME.toLowerCase())).thenReturn(pokemonDomainMock);
        when(this.pokemonMapper.domainToResponse(pokemonDomainMock)).thenReturn(expectedResponse);

        //Act
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .param("name", POKEMON_NAME.toLowerCase())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        final PokemonResponse responseAsObject = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(),
                PokemonResponse.class);

        //Assert
        verify(this.pokemonMapper, times(1)).domainToResponse(pokemonDomainMock);
        verify(this.getPokemonByNameService, times(1)).execute(POKEMON_NAME.toLowerCase());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseAsObject).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return status 404 when didn't found any pokemon by name")
    void shouldReturnStatus404_whenDidntFoundPokemonByName() throws Exception {
        //Arrange
        when(this.getPokemonByNameService.execute(POKEMON_NAME.toLowerCase()))
                .thenThrow(PokemonNotFoundException.class);

        //Act
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .param("name", POKEMON_NAME.toLowerCase())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        //Assert
        verifyNoInteractions(this.pokemonMapper);
        verify(this.getPokemonByNameService, times(1)).execute(POKEMON_NAME.toLowerCase());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should return status 500 when generic exception is thrown")
    void shouldReturn500_whenGenericExceptionIsThrown() throws Exception {
        //Arrange
        when(this.getPokemonByNameService.execute(POKEMON_NAME.toLowerCase()))
                .thenThrow(RuntimeException.class);

        //Act
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .param("name", POKEMON_NAME.toLowerCase())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        //Assert
        verifyNoInteractions(this.pokemonMapper);
        verify(this.getPokemonByNameService, times(1)).execute(POKEMON_NAME.toLowerCase());
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " })
    @DisplayName("Should return status 400 when request param is empty")
    void shouldReturn400_whenRequestParamEmpty(final String pokemonName) throws Exception {
        //Arrange
        final String expectedMessage = "Field `name` must not be empty.";
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        //Act
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .param("name", pokemonName)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        final Error responseAsObject = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(),
                Error.class);

        //Assert
        verifyNoInteractions(this.getPokemonByNameService);
        verifyNoInteractions(this.pokemonMapper);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseAsObject.getMessage()).isEqualTo(expectedMessage);
    }
}