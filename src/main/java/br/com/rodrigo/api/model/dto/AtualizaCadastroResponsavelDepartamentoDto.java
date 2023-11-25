package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtualizaCadastroResponsavelDepartamentoDto {

    private Long id;

    private FuncionarioDto funcionario;

    private DepartamentoDto departamento;

}
