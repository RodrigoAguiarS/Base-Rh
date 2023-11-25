package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.util.ValidatorUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  CadastroUsuarioDto {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long id;

    @Email
    @NotNull(message = "Campo email é requerido")
    @NotBlank(message = "O campo 'email' não pode está vazio")
    private String email;

    private boolean ativo;

    @Valid
    private PessoaDto pessoa;

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil::toEnum).collect(Collectors.toSet());
    }

    @NotNull(message = "Campo perfis é requerido")
    private Set<Integer> perfis = new HashSet<>();

    public static CadastroUsuarioDto fromEntity(Usuario cadastroUsuario) {
        CadastroUsuarioDto dto = new CadastroUsuarioDto();
        dto.setId(cadastroUsuario.getId());
        dto.setEmail(cadastroUsuario.getEmail());
        dto.setAtivo(cadastroUsuario.isAtivo());

        if (ValidatorUtil.isNotEmpty(cadastroUsuario.getPessoa())) {
            dto.setPessoa(PessoaDto.fromEntity(cadastroUsuario.getPessoa()));
        }

        dto.setPerfis(cadastroUsuario.getPerfis().stream().map(Perfil::getCod).collect(Collectors.toSet()));
        return dto;
    }

    public static Usuario toEntity(CadastroUsuarioDto dto) throws ParseException {
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
