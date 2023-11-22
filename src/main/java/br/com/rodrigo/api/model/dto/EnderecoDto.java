package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class EnderecoDto {

    private Long id;

    private String rua;

    private String bairro;

    private String cidade;

    private String estado;

    @NotNull(message = "Campo Cep é requerido")
    @NotBlank(message = "Campo Cep não pode estar em branco")
    private String cep;

    @NotNull(message = "Campo Número é requerido")
    @NotBlank(message = "Campo Número não pode estar em branco")
    private String numero;
}
