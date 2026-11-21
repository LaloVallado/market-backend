package com.tecnm.merida.market_backend.domain.dto;

// Respuesta del login: el token JWT generado
public class LoginResponseDTO {

    private String token;
    private String type = "Bearer";

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}