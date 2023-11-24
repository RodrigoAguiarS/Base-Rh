package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Endereco;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.EnderecoDto;
import br.com.rodrigo.api.model.dto.PessoaDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.repository.EnderecoRepository;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL;
import static br.com.rodrigo.api.util.ValidatorUtil.validarCpfEmailUnico;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PessoaRepository pessoaRepository;

    private final EnderecoRepository enderecoRepository;


    public UsuarioDto criarUsuario(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa pessoaSalva = salvarNovaPessoa(cadastroUsuarioDto);
        Set<Perfil> perfis = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());
        Usuario novoUsuario = criarNovoUsuario(cadastroUsuarioDto, pessoaSalva, perfis);
        return construirUsuarioDTO(novoUsuario);
    }

    private Pessoa salvarNovaPessoa(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa novaPessoa = PessoaDto.toEntity(cadastroUsuarioDto.getPessoa());
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        String email = cadastroUsuarioDto.getEmail();
        validarCpfEmailUnico(pessoaRepository, usuarioRepository, cpf, email, null);
        novaPessoa.setEndereco(salvarEndereco(cadastroUsuarioDto));
        return pessoaRepository.save(novaPessoa);
    }

    private Usuario criarNovoUsuario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa, Set<Perfil> perfis) throws ParseException {
        Usuario novoUsuario = CadastroUsuarioDto.toEntity(cadastroUsuarioDto);
        novoUsuario.setPessoa(pessoa);
        novoUsuario.setSenha(passwordEncoder.encode(cadastroUsuarioDto.getSenha()));
        novoUsuario.setPerfis(perfis);
        novoUsuario.setAtivo(true);
        return usuarioRepository.save(novoUsuario);
    }

    private UsuarioDto construirUsuarioDTO(Usuario usuario) {
        return UsuarioDto.fromEntity(usuario);
    }

    private Endereco salvarEndereco(CadastroUsuarioDto cadastroUsuarioDto) {
        Endereco endereco = EnderecoDto.toEntity(cadastroUsuarioDto.getPessoa().getEndereco());
        return enderecoRepository.save(endereco);
    }

    public UsuarioDto atualizarUsuario(Long id, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        Pessoa pessoaAtualizada = atualizarPessoaExistente(usuarioExistente.getPessoa(), cadastroUsuarioDto);

        Set<Perfil> perfisAtualizados = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());

        usuarioExistente.setEmail(cadastroUsuarioDto.getEmail());
        usuarioExistente.setSenha(passwordEncoder.encode(cadastroUsuarioDto.getSenha()));
        usuarioExistente.setPerfis(perfisAtualizados);
        usuarioExistente.setAtivo(true);
        usuarioExistente.setPessoa(pessoaAtualizada);

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return construirUsuarioDTO(usuarioAtualizado);
    }

    private Pessoa atualizarPessoaExistente(Pessoa pessoaExistente, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        String email = cadastroUsuarioDto.getEmail();
        validarCpfEmailUnico(pessoaRepository, usuarioRepository, cpf, email, pessoaExistente.getId());

        PessoaDto pessoaDto = cadastroUsuarioDto.getPessoa();
        Pessoa pessoaAtualizada = PessoaDto.toEntity(pessoaDto);
        pessoaAtualizada.setId(pessoaExistente.getId());

        if (ValidatorUtil.isNotEmpty(pessoaDto.getEndereco())) {
            Endereco enderecoAtualizado = EnderecoDto.toEntity(pessoaDto.getEndereco());
            pessoaAtualizada.setEndereco(enderecoAtualizado);
        }

        return pessoaRepository.save(pessoaAtualizada);
    }

    public Usuario obterUsuarioPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        return usuario;
    }

    public List<String> obterPerfis(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL + email));
        return usuario.getPerfis().stream()
                .map(Perfil::getDescricao)
                .collect(Collectors.toList());
    }

    public CadastroUsuarioDto obterUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL + email));
        CadastroUsuarioDto usuarioObterDadosDto = CadastroUsuarioDto.fromEntity(usuario);

        if (ValidatorUtil.isNotEmpty(usuario.getPessoa())) {
            CadastroUsuarioDto.fromEntity(usuario);
        }

        return usuarioObterDadosDto;
    }

    public List<Usuario> listarUsuarios(String email) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios.removeIf(usuario -> usuario.getUsername().equals(email));
        return usuarios;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void deletarUsuario(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
