package br.com.rodrigo.api.model.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponseDto {
    private String newToken;

    public RefreshTokenResponseDto(String newToken) {
        this.newToken = newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
