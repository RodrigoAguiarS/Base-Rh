package br.com.rodrigo.api.model.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequestDto {
    private String currentToken;

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }
}