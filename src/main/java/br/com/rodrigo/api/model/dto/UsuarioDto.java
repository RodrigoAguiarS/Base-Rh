package br.com.rodrigo.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class UsuarioDto {

    private Long id;
    private String email;
    private String nome;

    public UsuarioDto(Long id, String email, String nome) {
        this.id = id;
        this.email = email;
        this.nome = nome;
    }
}
