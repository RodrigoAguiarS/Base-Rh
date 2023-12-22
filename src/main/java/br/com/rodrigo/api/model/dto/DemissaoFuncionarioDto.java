package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  DemissaoFuncionarioDto {

    private Long id;

    @Email
    @NotNull(message = "Campo email é requerido")
    @NotBlank(message = "O campo 'email' não pode está vazio")
    private String email;

    @Valid
    private PessoaDto pessoa;

    private CargoDto cargo;

    private VinculoDto vinculo;

    private LocalDate dataEntrada;

    private LocalDate dataSaida;

    private boolean ativo;
    public static DemissaoFuncionarioDto fromEntity(Funcionario funcionario, Usuario usuario) {
        DemissaoFuncionarioDto demissaoFuncionarioDto = new DemissaoFuncionarioDto();
        demissaoFuncionarioDto.setId(funcionario.getId());
        demissaoFuncionarioDto.setEmail(usuario.getEmail());
        demissaoFuncionarioDto.setPessoa(PessoaDto.fromEntity(usuario.getPessoa()));
        demissaoFuncionarioDto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
        demissaoFuncionarioDto.setDataEntrada(funcionario.getDataEntrada());
        demissaoFuncionarioDto.setDataSaida(funcionario.getDataSaida());
        demissaoFuncionarioDto.setAtivo(usuario.getAtivo());
        return demissaoFuncionarioDto;
    }

    public Funcionario toEntity(DemissaoFuncionarioDto dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.getId());
        funcionario.setVinculo(VinculoDto.toEntity(dto.getVinculo()));
        funcionario.setDataSaida(dto.getDataSaida());
        funcionario.setDataEntrada(dto.getDataEntrada());
        funcionario.setPessoa(PessoaDto.toEntity(dto.getPessoa()));
        funcionario.setCargo(CargoDto.toEntity(dto.getCargo()));

        return funcionario;
    }
}
