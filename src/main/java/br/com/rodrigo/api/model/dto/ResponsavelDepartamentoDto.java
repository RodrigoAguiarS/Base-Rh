package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.ResponsavelDepartamento;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class ResponsavelDepartamentoDto {

    private Long id;
    private String nomeDepartamento;
    private String descricaoDepartamento;
    private String nomeFuncionarioResponsavel;

    public static ResponsavelDepartamentoDto fromEntity(ResponsavelDepartamento responsavelDepartamento) {
        ResponsavelDepartamentoDto dto = new ResponsavelDepartamentoDto();
        dto.setId(responsavelDepartamento.getId());
        dto.setNomeDepartamento(responsavelDepartamento.getDepartamento().getNome());
        dto.setDescricaoDepartamento(responsavelDepartamento.getDepartamento().getDescricao());
        dto.setNomeFuncionarioResponsavel(responsavelDepartamento.getFuncionario().getPessoa().getNome());
        return dto;
    }
}
