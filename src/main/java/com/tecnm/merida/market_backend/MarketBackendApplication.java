package com.tecnm.merida.market_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Habilita el boton "Authorize" (candado) en Swagger para pegar el token JWT
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Market Backend API", version = "1.0"),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class MarketBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketBackendApplication.class, args);
	}

}