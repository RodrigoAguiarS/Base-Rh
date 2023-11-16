package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.PessoaDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PessoaRepository pessoaRepository;

    public UsuarioDto criarUsuario(CadastroUsuarioDto cadastroUsuarioDto) {
        Pessoa pessoaSalva = salvarNovaPessoa(cadastroUsuarioDto.getPessoa());

        Set<Perfil> perfis = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());

        Usuario novoUsuario = criarNovoUsuario(cadastroUsuarioDto, pessoaSalva, perfis);

        return construirUsuarioDTO(novoUsuario);
    }

    private Pessoa salvarNovaPessoa(PessoaDto pessoaDTO) {
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(pessoaDTO.getNome());
        novaPessoa.setTelefone(pessoaDTO.getTelefone());
        novaPessoa.setCpf(pessoaDTO.getCpf());
        novaPessoa.setSexo(pessoaDTO.getSexo());
        novaPessoa.setDataNascimento(pessoaDTO.getDataNascimento());

        return pessoaRepository.save(novaPessoa);
    }

    private Usuario criarNovoUsuario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa, Set<Perfil> perfis) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setPessoa(pessoa);
        novoUsuario.setEmail(cadastroUsuarioDto.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(cadastroUsuarioDto.getSenha()));
        novoUsuario.setPerfis(perfis);
        novoUsuario.setAtivo(true);

        return usuarioRepository.save(novoUsuario);
    }

    private UsuarioDto construirUsuarioDTO(Usuario usuario) {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(usuario.getId());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setNome(usuario.getPessoa().getNome());
        return usuarioDto;
    }
}
