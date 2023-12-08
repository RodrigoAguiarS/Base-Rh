package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.RegistroPonto;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CadastroRegistroPontoDto {

    private String observacoes;

    public static RegistroPonto toEntity(CadastroRegistroPontoDto dto) {
        RegistroPonto registroPonto = new RegistroPonto();
        registroPonto.setObservacoes(dto.getObservacoes());
        return registroPonto;
    }
}
