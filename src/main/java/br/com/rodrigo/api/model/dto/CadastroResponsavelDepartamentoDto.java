package br.com.rodrigo.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CadastroResponsavelDepartamentoDto {

    private Long id;

    private Long funcionario;

    private Long departamento;

    private LocalDate dataInicioResponsabilidade;

}
