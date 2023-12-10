package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetalhesDepartamentoDto {

    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataCriacao;
    private EmpresaDto empresa;
    private ResponsavelDepartamentoDto responsavelAtual;

    public static DetalhesDepartamentoDto fromEntity(Departamento departamento, ResponsavelDepartamento responsavelAtual) {
        DetalhesDepartamentoDto dto = new DetalhesDepartamentoDto();
        dto.setId(departamento.getId());
        dto.setNome(departamento.getNome());
        dto.setDescricao(departamento.getDescricao());
        dto.setDataCriacao(departamento.getDataCriacao());

        if (ValidatorUtil.isNotEmpty(departamento.getEmpresa())) {
            dto.setEmpresa(EmpresaDto.fromEntity(departamento.getEmpresa()));
        }

        if (ValidatorUtil.isNotEmpty(responsavelAtual)) {
            dto.setResponsavelAtual(ResponsavelDepartamentoDto.fromEntity(responsavelAtual));
        }

        return dto;
    }
}
