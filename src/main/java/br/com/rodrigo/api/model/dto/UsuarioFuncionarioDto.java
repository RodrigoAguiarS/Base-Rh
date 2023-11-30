package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UsuarioFuncionarioDto {

    private Long id;

    private String email;

    private Set<String> perfis;

    private PessoaDto pessoa;

    private Long funcionario;

    private LocalDate dataEntrada;

    private LocalDate dataSaida;

    private CargoDto cargo;

    private boolean ativo;

    public static UsuarioFuncionarioDto fromEntity(Pessoa pessoa, Funcionario funcionario, Usuario usuario) {
        UsuarioFuncionarioDto dto = new UsuarioFuncionarioDto();
        dto.setPessoa(PessoaDto.fromEntity(pessoa));
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setAtivo(usuario.isAtivo());
        dto.setPerfis(usuario.getPerfis().stream().map(Perfil::getDescricao).collect(Collectors.toSet()));

        if (ValidatorUtil.isNotEmpty(funcionario)) {
            dto.setFuncionario(funcionario.getId());
            dto.setDataEntrada(funcionario.getDataEntrada());
            dto.setDataSaida(funcionario.getDataSaida());
            dto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
        }
        return dto;
    }
}

