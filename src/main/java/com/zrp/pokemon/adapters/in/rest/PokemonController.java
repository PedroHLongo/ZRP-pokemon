package com.zrp.pokemon.adapters.in.rest;

import com.zrp.pokemon.adapters.in.domain.PokemonDomain;
import com.zrp.pokemon.adapters.in.mapper.IPokemonMapper;
import com.zrp.pokemon.adapters.out.dto.PokemonResponse;
import com.zrp.pokemon.application.ports.in.service.IGetPokemonByNameService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pokemons")
@RequiredArgsConstructor
@Slf4j
public class PokemonController {

    private final IPokemonMapper pokemonMapper;
    private final IGetPokemonByNameService getPokemonByNameService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "ok",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PokemonResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "204", description = "no content", content = @Content
            ),
            @ApiResponse(
                    responseCode = "400", description = "bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404", description = "not found", content = @Content
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error", content = @Content
            )
    })
    @GetMapping
    ResponseEntity<PokemonResponse> getByName(@RequestParam @NotBlank final String name) {
        log.info("Fetching pokemon by name.");
        log.debug("Fetching pokemon by name [{}].", name);

        final PokemonDomain pokemonFound = this.getPokemonByNameService.execute(name);
        log.info("Pokemon found.");
        log.debug("Pokemon found with details [{}].", pokemonFound);

        return ResponseEntity.ok(this.pokemonMapper.domainToResponse(pokemonFound));
    }
}
