package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CadastroResponsavelDepartamentoDto {

    private Long id;

    private Long funcionario;

    private Long departamento;

}
