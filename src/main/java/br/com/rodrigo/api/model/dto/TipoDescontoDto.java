package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.TipoDesconto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TipoDescontoDto {

    private Long id;

    private String nome;

    private String descricao;

    private boolean ativo;

    public static TipoDescontoDto fromEntity(TipoDesconto tipoDesconto) {
        TipoDescontoDto dto = new TipoDescontoDto();
        dto.setId(tipoDesconto.getId());
        dto.setNome(tipoDesconto.getNome());
        dto.setDescricao(tipoDesconto.getDescricao());
        dto.setAtivo(tipoDesconto.isAtivo());

        return dto;
    }

    public static TipoDesconto toEntity(TipoDescontoDto dto) {
        TipoDesconto tipoDesconto = new TipoDesconto();
        tipoDesconto.setId(dto.getId());
        tipoDesconto.setNome(dto.getNome());
        tipoDesconto.setDescricao(dto.getDescricao());
        tipoDesconto.setAtivo(dto.isAtivo());

        return tipoDesconto;
    }
}
