package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDto {

    private Long id;

    @NotNull(message = "Campo pessoa é requerido")
    @NotBlank(message = "Campo pessoa não pode estar em branco")
    private PessoaDto pessoa;

    @NotNull(message = "Campo cargo é requerido")
    @NotBlank(message = "Campo cargo não pode estar em branco")
    private CargoDto cargo;

    @NotNull(message = "Campo vinculo é requerido")
    @NotBlank(message = "Campo vinculo não pode estar em branco")
    private VinculoDto vinculo;

    private LocalDate dataEntrada;

    private LocalDate dataSaida;

    public static FuncionarioDto fromEntity(Funcionario funcionario) {

        FuncionarioDto dto = new FuncionarioDto();
        dto.setId(funcionario.getId());
        dto.setPessoa(PessoaDto.fromEntity(funcionario.getPessoa()));
        dto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
        dto.setVinculo(VinculoDto.fromEntity(funcionario.getVinculo()));
        dto.setDataEntrada(funcionario.getDataEntrada());
        dto.setDataSaida(funcionario.getDataSaida());

        return dto;
    }

    public static Funcionario toEntity(FuncionarioDto dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.getId());
        funcionario.setPessoa(PessoaDto.toEntity(dto.getPessoa()));
        funcionario.setCargo(CargoDto.toEntity(dto.getCargo()));
        funcionario.setVinculo(VinculoDto.toEntity(dto.getVinculo()));
        funcionario.setDataEntrada(dto.getDataEntrada());
        funcionario.setDataSaida(dto.getDataSaida());

        return funcionario;
    }
}
