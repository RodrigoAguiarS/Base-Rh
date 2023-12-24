package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.TipoDemissao;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TipoDemissaoDto {

    private Long id;

    private String nome;

    private String descricao;

    private boolean ativo;

    public static TipoDemissaoDto fromEntity(TipoDemissao TipoDemissao) {
        TipoDemissaoDto dto = new TipoDemissaoDto();
        dto.setId(TipoDemissao.getId());
        dto.setNome(TipoDemissao.getNome());
        dto.setDescricao(TipoDemissao.getDescricao());
        dto.setAtivo(TipoDemissao.isAtivo());

        return dto;
    }

    public static TipoDemissao toEntity(TipoDemissaoDto dto) {
        TipoDemissao TipoDemissao = new TipoDemissao();
        TipoDemissao.setId(dto.getId());
        TipoDemissao.setNome(dto.getNome());
        TipoDemissao.setDescricao(dto.getDescricao());
        TipoDemissao.setAtivo(dto.isAtivo());

        return TipoDemissao;
    }
}
