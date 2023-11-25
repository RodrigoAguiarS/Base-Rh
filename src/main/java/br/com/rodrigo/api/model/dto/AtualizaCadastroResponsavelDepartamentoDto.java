package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AtualizaCadastroResponsavelDepartamentoDto {

    private Long id;

    private FuncionarioDto funcionario;

    private DepartamentoDto departamento;

    private LocalDate dataInicioResponsabilidade;

}
