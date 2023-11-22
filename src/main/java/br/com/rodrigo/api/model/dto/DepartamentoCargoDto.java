package br.com.rodrigo.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartamentoCargoDto {
    private Long id;
    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;
    private String descricao;
}
