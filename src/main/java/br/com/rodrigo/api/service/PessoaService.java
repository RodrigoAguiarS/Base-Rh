package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    private final ModelMapper modelMapper;

    public UsuarioDto criarUsuario(CadastroUsuarioDto cadastroUsuarioDto) {
        Pessoa pessoaSalva = salvarNovaPessoa(cadastroUsuarioDto);
        Set<Perfil> perfis = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());
        Usuario novoUsuario = criarNovoUsuario(cadastroUsuarioDto, pessoaSalva, perfis);
        return construirUsuarioDTO(novoUsuario);
    }

    private Pessoa salvarNovaPessoa(CadastroUsuarioDto cadastroUsuarioDto) {
        Pessoa novaPessoa = modelMapper.map(cadastroUsuarioDto.getPessoa(), Pessoa.class);
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        String email = cadastroUsuarioDto.getEmail();
        validarCpfEmailUnico(pessoaRepository, usuarioRepository, cpf, email, null);
        return pessoaRepository.save(novaPessoa);
    }

    private Usuario criarNovoUsuario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa, Set<Perfil> perfis) {
        Usuario novoUsuario = modelMapper.map(cadastroUsuarioDto, Usuario.class);
        novoUsuario.setPessoa(pessoa);
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
        usuarioDto.setPerfis(usuario.getPerfis().stream().map(Perfil::getDescricao).collect(Collectors.toSet()));
        return usuarioDto;
    }

    public UsuarioDto atualizarUsuario(Long id, CadastroUsuarioDto cadastroUsuarioDto) {
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

    private Pessoa atualizarPessoaExistente(Pessoa pessoaExistente, CadastroUsuarioDto cadastroUsuarioDto) {
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        String email = cadastroUsuarioDto.getEmail();
        validarCpfEmailUnico(pessoaRepository, usuarioRepository, cpf, email, pessoaExistente.getId());
        pessoaExistente.setNome(cadastroUsuarioDto.getPessoa().getNome());
        pessoaExistente.setTelefone(cadastroUsuarioDto.getPessoa().getTelefone());
        pessoaExistente.setCpf(cadastroUsuarioDto.getPessoa().getCpf());
        pessoaExistente.setSexo(cadastroUsuarioDto.getPessoa().getSexo());
        pessoaExistente.setDataNascimento(cadastroUsuarioDto.getPessoa().getDataNascimento());

        return pessoaRepository.save(pessoaExistente);
    }

    public UsuarioDto obterUsuarioPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        return construirUsuarioDTO(usuario);
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
        CadastroUsuarioDto usuarioObterDadosDto = modelMapper.map(usuario, CadastroUsuarioDto.class);

        if (ValidatorUtil.isNotEmpty(usuario.getPessoa())) {
            modelMapper.map(usuario.getPessoa(), usuarioObterDadosDto.getPessoa());
        }

        return usuarioObterDadosDto;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void deletarUsuario(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
