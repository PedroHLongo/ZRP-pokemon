package com.zrp.pokemon;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PokemonApplication {

	@Generated
	public static void main(String[] args) {
		SpringApplication.run(PokemonApplication.class, args);
	}

}
