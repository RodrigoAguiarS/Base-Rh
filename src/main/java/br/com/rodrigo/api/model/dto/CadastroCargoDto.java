package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroCargoDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    @NotNull(message = "Campo descricao é requerido")
    @NotBlank(message = "Campo descricao não pode estar em branco")
    private String descricao;

    @NotNull(message = "Campo descricao é requerido")
    @NotBlank(message = "Campo descricao não pode estar em branco")
    private String responsabilidades;

    @NotNull(message = "Campo salarioBase é requerido")
    @NotBlank(message = "Campo salarioBase não pode estar em branco")
    private BigDecimal salarioBase;

    @NotNull(message = "Campo departamento é requerido")
    private Long departamento;

    public static CadastroCargoDto fromEntity(Cargo cargo) {
        CadastroCargoDto cadastroCargoDto = new CadastroCargoDto();
        cadastroCargoDto.setId(cargo.getId());
        cadastroCargoDto.setNome(cargo.getNome());
        cadastroCargoDto.setDescricao(cargo.getDescricao());
        cadastroCargoDto.setResponsabilidades(cargo.getResponsabilidades());
        cadastroCargoDto.setSalarioBase(cargo.getSalarioBase());
        cadastroCargoDto.setDepartamento(cargo.getDepartamento().getId());

        return cadastroCargoDto;
    }

    public static Cargo toEntity(CadastroCargoDto cadastroCargoDto) {
        Cargo cargo = new Cargo();
        cargo.setId(cadastroCargoDto.getId());
        cargo.setNome(cadastroCargoDto.getNome());
        cargo.setDescricao(cadastroCargoDto.getDescricao());
        cargo.setResponsabilidades(cadastroCargoDto.getResponsabilidades());
        cargo.setSalarioBase(cadastroCargoDto.getSalarioBase());

        return cargo;
    }
}
