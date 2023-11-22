package br.com.rodrigo.api.model.dto;

import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Long id;

    private String email;

    private String nome;

    private Set<String> perfis;

    public static UsuarioDto fromEntity(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getPessoa().getNome());
        dto.setPerfis(usuario.getPerfis().stream().map(Perfil::getDescricao).collect(Collectors.toSet()));
        return dto;
    }

    public static Usuario toEntity(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setEmail(dto.getEmail());

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());

        usuario.setPessoa(pessoa);

        Set<Perfil> perfis = dto.getPerfis().stream().map(Perfil::toEnumString).collect(Collectors.toSet());
        usuario.setPerfis(perfis);

        return usuario;
    }
}
