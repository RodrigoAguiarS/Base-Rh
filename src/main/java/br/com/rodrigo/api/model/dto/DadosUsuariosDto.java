package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DadosUsuariosDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Email
    @NotNull(message = "Campo email é requerido")
    @NotBlank(message = "O campo 'email' não pode está vazio")
    private String email;

    private boolean ativo;

    @Valid
    private PessoaDto pessoa;

    private CargoDto cargo;

    private LocalDate dataEntrada;

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil::toEnum).collect(Collectors.toSet());
    }

    @NotNull(message = "Campo perfis é requerido")
    private Set<Integer> perfis = new HashSet<>();

    public static DadosUsuariosDto fromEntity(Usuario cadastroUsuario, Funcionario funcionario) {
        DadosUsuariosDto dto = new DadosUsuariosDto();
        dto.setId(cadastroUsuario.getId());
        dto.setEmail(cadastroUsuario.getEmail());
        dto.setAtivo(cadastroUsuario.isAtivo());

        if (ValidatorUtil.isNotEmpty(funcionario.getCargo())) {
            dto.setCargo(CargoDto.fromEntity(funcionario.getCargo()));
            dto.setDataEntrada(funcionario.getDataEntrada());
        }

        if (ValidatorUtil.isNotEmpty(cadastroUsuario.getPessoa())) {
            dto.setPessoa(PessoaDto.fromEntity(cadastroUsuario.getPessoa()));
        }

        dto.setPerfis(cadastroUsuario.getPerfis().stream().map(Perfil::getCod).collect(Collectors.toSet()));
        return dto;
    }

    public static Usuario toEntity(DadosUsuariosDto dto) throws ParseException {
        Usuario cadastroUsuario = new Usuario();
        cadastroUsuario.setId(dto.getId());
        cadastroUsuario.setEmail(dto.getEmail());
        cadastroUsuario.setAtivo(dto.isAtivo());

        if (ValidatorUtil.isNotEmpty(dto.getPessoa())) {
            cadastroUsuario.setPessoa(PessoaDto.toEntity(dto.getPessoa()));
        }

        dto.getPerfis().forEach(perfilCodigo -> cadastroUsuario.getPerfis().add(Perfil.toEnum(perfilCodigo.getId())));

        return cadastroUsuario;
    }
}
