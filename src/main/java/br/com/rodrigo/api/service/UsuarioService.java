package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Endereco;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Perfil;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.DadosUsuariosDto;
import br.com.rodrigo.api.model.dto.EnderecoDto;
import br.com.rodrigo.api.model.dto.PessoaDto;
import br.com.rodrigo.api.model.dto.UsuarioFuncionarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.repository.CargoRepository;
import br.com.rodrigo.api.repository.EnderecoRepository;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.PessoaRepository;
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

import static br.com.rodrigo.api.exception.ValidationError.ERRO_CARGO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCINARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ASSOCIADA_AO_PESSOA;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL;
import static br.com.rodrigo.api.util.EmailMensagensUtil.CONFIRMACAO_CADASTRO;
import static br.com.rodrigo.api.util.EmailMensagensUtil.getEmailCadastroTexto;
import static br.com.rodrigo.api.util.ValidatorUtil.validarCpfExistente;
import static br.com.rodrigo.api.util.ValidatorUtil.validarCpfExistenteComId;
import static br.com.rodrigo.api.util.ValidatorUtil.validarEmailExistente;
import static br.com.rodrigo.api.util.ValidatorUtil.validarEmailExistenteComId;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PessoaRepository pessoaRepository;

    private final EnderecoRepository enderecoRepository;

    private final CargoRepository cargoRepository;

    private final FuncionarioRepository funcionarioRepository;

    private final FuncionarioService funcionarioService;

    private final EmailService emailService;


    public UsuarioDto criarUsuario(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa pessoaSalva = salvarNovaPessoa(cadastroUsuarioDto);
        Set<Perfil> perfis = cadastroUsuarioDto.getPerfis().stream()
                .map(idPerfil -> Perfil.toEnum(idPerfil.getId()))
                .collect(Collectors.toSet());
        cadastrarFuncionario(cadastroUsuarioDto, pessoaSalva);
        Usuario novoUsuario = criarNovoUsuario(cadastroUsuarioDto, pessoaSalva, perfis);
        return UsuarioDto.fromEntity(novoUsuario);
    }

    private Pessoa salvarNovaPessoa(CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Pessoa novaPessoa = PessoaDto.toEntity(cadastroUsuarioDto.getPessoa());
        String cpf = cadastroUsuarioDto.getPessoa().getCpf();
        validarCpfExistente(pessoaRepository, cpf);
        novaPessoa.setEndereco(salvarEndereco(cadastroUsuarioDto));
        return pessoaRepository.save(novaPessoa);
    }

    public void cadastrarFuncionario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa) {
        Cargo cargo = cargoRepository.findById(cadastroUsuarioDto.getCargo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO));
        Funcionario funcionario = new Funcionario();
        funcionario.setCargo(cargo);
        funcionario.setPessoa(pessoa);
        funcionario.setDataEntrada(cadastroUsuarioDto.getDataEntrada());

        funcionarioRepository.save(funcionario);
    }

    public void atualizarFuncionario(Long idFuncionario, CadastroUsuarioDto cadastroUsuarioDto) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_FUNCINARIO_NAO_ENCONTRADO));

        Cargo cargo = cargoRepository.findById(cadastroUsuarioDto.getCargo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO));

        funcionario.setCargo(cargo);
        funcionario.setDataEntrada(cadastroUsuarioDto.getDataEntrada());

        funcionarioRepository.save(funcionario);
    }


    private Usuario criarNovoUsuario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa, Set<Perfil> perfis) throws ParseException {
        Usuario novoUsuario = CadastroUsuarioDto.toEntity(cadastroUsuarioDto);
        String senhaGerada = gerarSenhaAleatoria();
        String email = cadastroUsuarioDto.getEmail();
        validarEmailExistente(usuarioRepository, email);
        String mensagemEmail = getEmailCadastroTexto(cadastroUsuarioDto.getPessoa().getNome(),
                cadastroUsuarioDto.getEmail(), senhaGerada);
        emailService.sendEmail(cadastroUsuarioDto.getEmail(), CONFIRMACAO_CADASTRO, mensagemEmail);
        novoUsuario.setPessoa(pessoa);
        novoUsuario.setSenha(passwordEncoder.encode(senhaGerada));
        novoUsuario.setPerfis(perfis);
        novoUsuario.setAtivo(true);
        return usuarioRepository.save(novoUsuario);
    }

    private Endereco salvarEndereco(CadastroUsuarioDto cadastroUsuarioDto) {
        Endereco endereco = EnderecoDto.toEntity(cadastroUsuarioDto.getPessoa().getEndereco());
        return enderecoRepository.save(endereco);
    }

    public UsuarioDto atualizarUsuario(Long id, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        Pessoa pessoaAtualizada = atualizarPessoaExistente(usuarioExistente.getPessoa(), cadastroUsuarioDto);
        Funcionario funcionario = funcionarioRepository.findByPessoaId(pessoaAtualizada.getId());

        if(ValidatorUtil.isNotEmpty(funcionario)) {
            atualizarFuncionario(funcionario.getId(), cadastroUsuarioDto);
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

    private Pessoa atualizarPessoaExistente(Pessoa pessoaExistente, CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        String email = cadastroUsuarioDto.getEmail();
        validarCpfExistenteComId(pessoaRepository, email, pessoaExistente.getId());
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

    public DadosUsuariosDto obterUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO_PARA_EMAIL + email));

        Funcionario funcionario = funcionarioRepository.findByPessoaId(usuario.getPessoa().getId());

        DadosUsuariosDto dadosUsuariosDto = DadosUsuariosDto.fromEntity(usuario, funcionario);

        if (ValidatorUtil.isNotEmpty(usuario.getPessoa())) {
            CadastroUsuarioDto.fromEntity(usuario);
        }
        return dadosUsuariosDto;
    }

    public List<Usuario> listarUsuarios(String email) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios.removeIf(usuario -> usuario.getUsername().equals(email));
        return usuarios;
    }

    @Transactional
    public void deletarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        Pessoa pessoa = usuario.getPessoa();

        if (ValidatorUtil.isNotEmpty(pessoa)) {
            Funcionario funcionario = funcionarioRepository.findByPessoaId(pessoa.getId());

            if (ValidatorUtil.isNotEmpty(funcionario)) {
                if (funcionarioService.funcionarioTemVinculoComDepartamento(funcionario)) {
                    throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO);
                }

                funcionarioRepository.deleteById(funcionario.getId());
            }

            pessoaRepository.deleteById(pessoa.getId());
        }

        usuarioRepository.deleteById(idUsuario);
    }

    private String gerarSenhaAleatoria() {
        String senhaAleatoria = UUID.randomUUID().toString();

        senhaAleatoria = senhaAleatoria.replaceAll
                ("[^a-zA-Z0-9]", "").substring(0, 8);
        return senhaAleatoria;
    }

    public UsuarioFuncionarioDto obterUsuarioEhFuncionario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO));

        Pessoa pessoa = usuario.getPessoa();

        if (ValidatorUtil.isEmpty(pessoa)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ASSOCIADA_AO_PESSOA);
        }

        Funcionario funcionario = funcionarioRepository.findByPessoaId(pessoa.getId());

        return UsuarioFuncionarioDto.fromEntity(pessoa, funcionario, usuario);
    }

    public Usuario buscarPorNomeUsuario(String username) {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));
    }

    public void alterarSenha(Long idUsuario, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_USUARIO_NAO_ENCONTRADO));

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.save(usuario);
    }

    public Usuario obterUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return buscarPorNomeUsuario(authentication.getName());
    }

    public Funcionario getFuncionarioDoUsuarioLogado() {
        Usuario usuario = obterUsuarioLogado();

        return funcionarioRepository.findByPessoaId(usuario.getPessoa().getId());
    }

}
