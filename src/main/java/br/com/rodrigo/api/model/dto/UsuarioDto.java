package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class UsuarioDto {

    private Long id;
    private String email;
    private String nome;
    private Set<String> perfis;

    public UsuarioDto(Long id, String email, String nome) {
        this.id = id;
        this.email = email;
        this.nome = nome;

    }

    public static class DepartamentoCargoDto {
    }
}
