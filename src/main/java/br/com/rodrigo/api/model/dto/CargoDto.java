package br.com.rodrigo.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    private String descricao;

    private String responsabilidades;

    @NotNull(message = "Campo salarioBase é requerido")
    @NotBlank(message = "Campo salarioBase não pode estar em branco")
    private BigDecimal salarioBase;

    @NotNull(message = "Campo departamento é requerido")
    @NotBlank(message = "Campo departamento não pode estar em branco")
    @Valid
    private DepartamentoDto departamento;
}
