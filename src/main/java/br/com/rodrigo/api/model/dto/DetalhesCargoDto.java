package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetalhesCargoDto {

    private Long id;

    private String nome;

    private String descricao;

    private String responsabilidades;

    private BigDecimal salarioBase;

    private DepartamentoDto departamento;

    private ResponsavelDepartamentoDto responsavelAtual;

    public static DetalhesCargoDto fromEntity(Cargo cargo, ResponsavelDepartamento responsavelAtual) {
        DetalhesCargoDto dto = new DetalhesCargoDto();
        dto.setId(cargo.getId());
        dto.setNome(cargo.getNome());
        dto.setDescricao(cargo.getDescricao());
        dto.setResponsabilidades(cargo.getResponsabilidades());
        dto.setSalarioBase(cargo.getSalarioBase());
        dto.setDepartamento(DepartamentoDto.fromEntity(cargo.getDepartamento()));

        if (ValidatorUtil.isNotEmpty(responsavelAtual)) {
            dto.setResponsavelAtual(ResponsavelDepartamentoDto.fromEntity(responsavelAtual));
        }

        return dto;
    }
}
