package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Pessoa;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class FuncionarioDto {

    private Long id;

    private PessoaDto pessoa;

    private CargoDto cargo;
}
