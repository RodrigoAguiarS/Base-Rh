package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Departamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartamentoDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    @NotNull(message = "Campo descricao é requerido")
    @NotBlank(message = "Campo descricao não pode estar em branco")
    private String descricao;

    public static DepartamentoDto fromEntity(Departamento departamento) {
        DepartamentoDto departamentoDto = new DepartamentoDto();
        departamentoDto.setId(departamento.getId());
        departamentoDto.setNome(departamento.getNome());
        departamentoDto.setDescricao(departamento.getDescricao());
        return departamentoDto;
    }

    public static Departamento toEntity(DepartamentoDto departamentoDto) {
        Departamento departamento = new Departamento();
        departamento.setId(departamentoDto.getId());
        departamento.setNome(departamentoDto.getNome());
        departamento.setDescricao(departamentoDto.getDescricao());
        return departamento;
    }
}

