package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
public class FuncionarioDto {

    private Long id;

    @NotNull(message = "Campo pessoa é requerido")
    @NotBlank(message = "Campo pessoa não pode estar em branco")
    private PessoaDto pessoa;

    @NotNull(message = "Campo cargo é requerido")
    @NotBlank(message = "Campo cargo não pode estar em branco")
    private CargoDto cargo;
}
