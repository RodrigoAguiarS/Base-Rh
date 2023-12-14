package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Empresa;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.DadosGeraisUsuarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL;
import static br.com.rodrigo.api.util.ValidatorUtil.validarEmailExistente;
import static br.com.rodrigo.api.util.ValidatorUtil.validarEmailExistenteComId;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PessoaService pessoaService;

    private final FuncionarioService funcionarioService;

    private final EmailService emailService;

    private final EmpresaService empresaService;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;


    public UsuarioDto cadastrarUsuario(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa pessoaSalva = pessoaService.cadastrarNovaPessoa(cadastroUsuarioDto);
        Set<Perfil> perfis = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());
        funcionarioService.cadastrarFuncionario(cadastroUsuarioDto, pessoaSalva);
        Usuario novoUsuario = criarNovoUsuario(cadastroUsuarioDto, pessoaSalva, perfis);
        return UsuarioDto.fromEntity(novoUsuario);
    }


    private Usuario criarNovoUsuario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa, Set<Perfil> perfis) throws ParseException {
        Usuario novoUsuario = CadastroUsuarioDto.toEntity(cadastroUsuarioDto);
        String senhaGerada = gerarSenhaAleatoria();
        String email = cadastroUsuarioDto.getEmail();
        validarEmailExistente(usuarioRepository, email);
//        String mensagemEmail = getEmailCadastroTexto(cadastroUsuarioDto.getPessoa().getNome(),
//                cadastroUsuarioDto.getEmail(), senhaGerada);
//        emailService.sendEmail(cadastroUsuarioDto.getEmail(), CONFIRMACAO_CADASTRO, mensagemEmail);
        novoUsuario.setPessoa(pessoa);
        novoUsuario.setSenha(passwordEncoder.encode(senhaGerada));
        novoUsuario.setPerfis(perfis);
        novoUsuario.setAtivo(true);
        return usuarioRepository.save(novoUsuario);
    }

    public UsuarioDto atualizaUsuario(Long id, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        Pessoa pessoaAtualizada = pessoaService.atualizaPessoaExistente(usuarioExistente.getPessoa(), cadastroUsuarioDto);
        Funcionario funcionario = funcionarioService.buscaFuncionarioPorIdPessoa(pessoaAtualizada.getId());

        if(ValidatorUtil.isNotEmpty(funcionario)) {
            funcionarioService.atualizaFuncionario(funcionario.getId(), cadastroUsuarioDto);
        }
        Set<Perfil> perfisAtualizados = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());
        validarEmailExistenteComId(usuarioRepository, cadastroUsuarioDto.getEmail(), usuarioExistente.getId());
        usuarioExistente.setEmail(cadastroUsuarioDto.getEmail());
        usuarioExistente.setPerfis(perfisAtualizados);
        usuarioExistente.setAtivo(true);
        usuarioExistente.setPessoa(pessoaAtualizada);

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return UsuarioDto.fromEntity(usuarioAtualizado);
    }

    public Usuario obterUsuarioPorId(Long idUsuario) {

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));
    }

    public List<String> obterPerfis(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL + email));
        return usuario.getPerfis().stream()
                .map(Perfil::getDescricao)
                .collect(Collectors.toList());
    }

    public DadosGeraisUsuarioDto obterUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL + email));

        Pessoa pessoa = usuario.getPessoa();
        Funcionario funcionario = getFuncionarioDoUsuarioLogado();
        Empresa empresa = empresaService.obterEmpresaDoFuncionario(funcionario);
        ResponsavelDepartamento responsavelDepartamento =
                responsavelDepartamentoService.obterResponsavelDepartamentoDoCargo(funcionario);

        return DadosGeraisUsuarioDto.fromEntity(pessoa, funcionario, usuario, responsavelDepartamento, empresa);
    }

    public List<Usuario> listaUsuarios(String email) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios.removeIf(usuario -> usuario.getUsername().equals(email));
        return usuarios;
    }

    @Transactional
    public void deleteUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        Pessoa pessoa = usuario.getPessoa();

        if (ValidatorUtil.isNotEmpty(pessoa)) {
            Funcionario funcionario = funcionarioService.buscaFuncionarioPorIdPessoa(pessoa.getId());

            if (ValidatorUtil.isNotEmpty(funcionario)) {
                if (funcionarioService.funcionarioTemVinculoComDepartamento(funcionario)) {
                    throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO);
                }

                funcionarioService.deletaFuncionario(funcionario.getId());
            }

            pessoaService.deletePessoa(pessoa.getId());
        }

        usuarioRepository.deleteById(idUsuario);
    }

    private String gerarSenhaAleatoria() {
        String senhaAleatoria = UUID.randomUUID().toString();

        senhaAleatoria = senhaAleatoria.replaceAll
                ("[^a-zA-Z0-9]", "").substring(0, 8);
        return senhaAleatoria;
    }

    public DadosGeraisUsuarioDto obterDadosGeraisUsuario(Long usuarioId) {
        Usuario usuario = obterUsuario(usuarioId);
        Pessoa pessoa = pessoaService.obterPessoaDoUsuario(usuario);
        Funcionario funcionario = funcionarioService.buscaFuncionarioPorIdPessoa(pessoa.getId());
        Empresa empresa = empresaService.obterEmpresaDoFuncionario(funcionario);
        ResponsavelDepartamento responsavelDepartamento =
                responsavelDepartamentoService.obterResponsavelDepartamentoDoCargo(funcionario);

        return DadosGeraisUsuarioDto.fromEntity(pessoa, funcionario, usuario, responsavelDepartamento, empresa );
    }

    public Usuario buscaPorNomeUsuario(String username) {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));
    }

    public void alteraSenha(Long idUsuario, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.save(usuario);
    }

    public Funcionario getFuncionarioDoUsuarioLogado() {
        Usuario usuario = obterUsuarioLogado();

        return funcionarioService.buscaFuncionarioPorIdPessoa(usuario.getPessoa().getId());
    }

    public Usuario obterUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return buscaPorNomeUsuario(authentication.getName());
    }

    private Usuario obterUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO));
    }
}
