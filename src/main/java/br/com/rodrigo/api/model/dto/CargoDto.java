package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoDto {

    private Long id;

    @NotNull(message = "Campo nome é requerido")
    @NotBlank(message = "Campo nome não pode estar em branco")
    private String nome;

    @NotNull(message = "Campo descricao é requerido")
    @NotBlank(message = "Campo descricao não pode estar em branco")
    private String descricao;

    @NotNull(message = "Campo responsabilidades é requerido")
    @NotBlank(message = "Campo responsabilidades não pode estar em branco")
    private String responsabilidades;

    @NotNull(message = "Campo salarioBase é requerido")
    private BigDecimal salarioBase;

    @Valid
    private DepartamentoDto departamento;

    public static CargoDto fromEntity(Cargo cargo) {
        CargoDto cargoDto = new CargoDto();
        cargoDto.setId(cargo.getId());
        cargoDto.setNome(cargo.getNome());
        cargoDto.setDescricao(cargo.getDescricao());
        cargoDto.setResponsabilidades(cargo.getResponsabilidades());
        cargoDto.setSalarioBase(cargo.getSalarioBase());

        if (ValidatorUtil.isNotEmpty(cargo.getDepartamento())) {
            cargoDto.setDepartamento(DepartamentoDto.fromEntity(cargo.getDepartamento()));
        }

        return cargoDto;
    }

    public static Cargo toEntity(CargoDto cargoDto) {
        Cargo cargo = new Cargo();
        cargo.setId(cargoDto.getId());
        cargo.setNome(cargoDto.getNome());
        cargo.setDescricao(cargoDto.getDescricao());
        cargo.setResponsabilidades(cargoDto.getResponsabilidades());
        cargo.setSalarioBase(cargoDto.getSalarioBase());

        if (ValidatorUtil.isNotEmpty(cargoDto.getDepartamento())) {
            cargo.setDepartamento(DepartamentoDto.toEntity(cargoDto.getDepartamento()));
        }

        return cargo;
    }
}
