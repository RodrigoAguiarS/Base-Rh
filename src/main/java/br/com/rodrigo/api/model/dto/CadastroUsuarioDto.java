package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  CadastroUsuarioDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Email
    @NotNull(message = "Campo email é requerido")
    @NotBlank(message = "O campo 'email' não pode está vazio")
    private String email;

    @NotNull(message = "Campo senha é requerido")
    @NotBlank(message = "O campo 'senha' não pode está vazio")
    private String senha;

    private boolean ativo;

    @Valid
    private PessoaDto pessoa;

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil::toEnum).collect(Collectors.toSet());
    }

    @NotNull(message = "Campo perfis é requerido")
    private Set<Integer> perfis = new HashSet<>();

    public CadastroUsuarioDto(Usuario obj) {
        super();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
    }
}
