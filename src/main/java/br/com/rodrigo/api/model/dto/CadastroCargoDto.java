package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor

public class CadastroCargoDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "O campo 'nome' não pode está vazio")
    private String nome;

    @NotNull(message = "Campo descricao é requerido")
    @NotBlank(message = "O campo 'descricao' não pode está vazio")
    private String descricao;

    @NotNull(message = "Campo salarioBase é requerido")
    @NotBlank(message = "O campo 'salarioBase' não pode está vazio")
    private BigDecimal salarioBase;

    @NotNull(message = "Campo responsabilidades é requerido")
    @NotBlank(message = "O campo 'responsabilidades' não pode está vazio")
    private String responsabilidades;

    private Long departamento;

    public static CadastroCargoDto fromEntity(Cargo cargo) {
        CadastroCargoDto cargoDto = new CadastroCargoDto();
        cargoDto.setId(cargo.getId());
        cargoDto.setNome(cargo.getNome());
        cargoDto.setDescricao(cargo.getDescricao());
        cargoDto.setResponsabilidades(cargo.getResponsabilidades());
        cargoDto.setSalarioBase(cargo.getSalarioBase());
        cargoDto.setDepartamento(cargo.getDepartamento().getId());

        return cargoDto;
    }

    public static Cargo toEntity(CadastroCargoDto cargoDto) {
        Cargo cargo = new Cargo();
        cargo.setId(cargoDto.getId());
        cargo.setNome(cargoDto.getNome());
        cargo.setDescricao(cargoDto.getDescricao());
        cargo.setResponsabilidades(cargoDto.getResponsabilidades());
        cargo.setSalarioBase(cargoDto.getSalarioBase());

        return cargo;
    }
}
