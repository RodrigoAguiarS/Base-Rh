package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetalhesFuncionarioDto {

    private Long id;
    private PessoaDto pessoa;
    private CargoDto cargo;
    private LocalDate dataAdmissao;
    private ResponsavelDepartamentoDto responsavelAtual;

    public static DetalhesFuncionarioDto fromEntity(Funcionario funcionario, ResponsavelDepartamento responsavelAtual) {
        DetalhesFuncionarioDto dto = new DetalhesFuncionarioDto();
        dto.setId(funcionario.getId());
        dto.setPessoa(PessoaDto.fromEntity(funcionario.getPessoa()));
        dto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
        dto.setDataAdmissao(funcionario.getDataEntrada());

        if (ValidatorUtil.isNotEmpty(responsavelAtual)) {
            dto.setResponsavelAtual(ResponsavelDepartamentoDto.fromEntity(responsavelAtual));
        }

        return dto;
    }
}
