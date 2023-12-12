package br.com.rodrigo.api.model.dto;


import br.com.rodrigo.api.model.Vinculo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VinculoDto {
    
    private Long id;

    private String nome;

    private String descricao;
    
    private boolean ativo;

    public static VinculoDto fromEntity(Vinculo vinculo) {
        VinculoDto dto = new VinculoDto();
        dto.setId(vinculo.getId());
        dto.setNome(vinculo.getNome());
        dto.setDescricao(vinculo.getDescricao());
        dto.setAtivo(vinculo.isAtivo());
        return dto;
    }

    public static Vinculo toEntity(VinculoDto dto) {
        Vinculo vinculo = new Vinculo();
        vinculo.setId(dto.getId());
        vinculo.setNome(dto.getNome());
        vinculo.setDescricao(dto.getDescricao());
        vinculo.setAtivo(dto.isAtivo());

        return vinculo;
    }
}
