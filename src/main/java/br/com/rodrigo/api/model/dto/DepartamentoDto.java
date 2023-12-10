package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Departamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    private LocalDate dataCriacao = LocalDate.now();

    private EmpresaDto empresa;

    public static DepartamentoDto fromEntity(Departamento departamento) {
        DepartamentoDto departamentoDto = new DepartamentoDto();
        departamentoDto.setId(departamento.getId());
        departamentoDto.setNome(departamento.getNome());
        departamentoDto.setDescricao(departamento.getDescricao());
        departamentoDto.setDataCriacao(departamento.getDataCriacao());
        departamentoDto.setEmpresa(EmpresaDto.fromEntity(departamento.getEmpresa()));
        return departamentoDto;
    }

    public static Departamento toEntity(DepartamentoDto departamentoDto) {
        Departamento departamento = new Departamento();
        departamento.setId(departamentoDto.getId());
        departamento.setNome(departamentoDto.getNome());
        departamento.setDescricao(departamentoDto.getDescricao());
        departamento.setDataCriacao(departamentoDto.getDataCriacao());
        departamento.setEmpresa(EmpresaDto.toEntity(departamentoDto.getEmpresa()));
        return departamento;
    }
}

