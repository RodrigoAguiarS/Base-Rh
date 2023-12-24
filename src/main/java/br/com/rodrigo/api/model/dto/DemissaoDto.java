package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.DemissaoFuncionario;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DemissaoDto {

    private Long id;

    private TipoDemissaoDto tipoDemissao;

    @NotNull(message = "Campo motivo é requerido")
    private String motivo;

    public static DemissaoDto fromEntity(DemissaoFuncionario demissaoFuncionario) {
        DemissaoDto dto = new DemissaoDto();
        dto.setId(demissaoFuncionario.getId());
        dto.setTipoDemissao(TipoDemissaoDto.fromEntity(demissaoFuncionario.getTipoDemissao()));
        dto.setMotivo(demissaoFuncionario.getMotivo());
        return dto;
    }
}
