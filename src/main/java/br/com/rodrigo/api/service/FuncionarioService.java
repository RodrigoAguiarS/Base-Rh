package br.com.rodrigo.api.service;


import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.Vinculo;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.DemissaoDto;
import br.com.rodrigo.api.model.dto.DetalhesFuncionarioDto;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_CARGO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DATA_SAIDA_FUNCINARIO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCINARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_PESSOA_NAO_ENCONTRADA;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_USUARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_VINCULO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    private final CargoService cargoService;

    private final VinculoService vinculoService;

    private final UsuarioRepository usuarioRepository;

    private final PessoaRepository pessoaRepository;

    private final DemissaoFuncionarioService demissaoFuncionarioService;

    public boolean funcionarioTemVinculoComDepartamento(Funcionario funcionario) {
        return responsavelDepartamentoService.existsByFuncionario(funcionario);
    }

    public Funcionario obterFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_FUNCINARIO_NAO_ENCONTRADO));
    }

    public List<Funcionario> listaTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public List<DetalhesFuncionarioDto> listaTodosDetalhesFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        return funcionarios.stream()
                .filter(funcionario -> funcionario.getDataSaida() == null || ValidatorUtil.isEmpty(funcionario.getDataSaida()))
                .map(funcionario -> {
                    ResponsavelDepartamento responsavelDepartamento = responsavelDepartamentoService
                            .findResponsavelDepartamentoByDepartamentoId(funcionario.getCargo().getDepartamento().getId());
                    return DetalhesFuncionarioDto.fromEntity(funcionario, responsavelDepartamento);
                })
                .collect(Collectors.toList());
    }
    public void deletaFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public void cadastrarFuncionario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa) {
        Cargo cargo = cargoService.getCargoById(cadastroUsuarioDto.getCargo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO));
        Vinculo vinculo = vinculoService.getVinculoById(cadastroUsuarioDto.getVinculo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_VINCULO_NAO_ENCONTRADO));
        Funcionario funcionario = new Funcionario();
        funcionario.setCargo(cargo);
        funcionario.setPessoa(pessoa);
        funcionario.setVinculo(vinculo);
        funcionario.setDataEntrada(cadastroUsuarioDto.getDataEntrada());

        funcionarioRepository.save(funcionario);
    }

    public void atualizaFuncionario(Long idFuncionario, CadastroUsuarioDto cadastroUsuarioDto) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_FUNCINARIO_NAO_ENCONTRADO));

        Cargo cargo = cargoService.getCargoById(cadastroUsuarioDto.getCargo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO));

        Vinculo vinculo = vinculoService.getVinculoById(cadastroUsuarioDto.getVinculo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_VINCULO_NAO_ENCONTRADO));

        funcionario.setCargo(cargo);
        funcionario.setDataEntrada(cadastroUsuarioDto.getDataEntrada());
        funcionario.setVinculo(vinculo);
        funcionarioRepository.save(funcionario);
    }

    public Funcionario buscaFuncionarioPorIdPessoa(Long idPessoa) {
        Optional<Funcionario> funcionarioOptional = Optional.ofNullable(funcionarioRepository.findByPessoaId(idPessoa));
        return funcionarioOptional
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_FUNCINARIO_NAO_ENCONTRADO));
    }


    @Transactional
    public void demitirFuncionario(Long idFuncionario, DemissaoDto demissaoDto) {
        LocalDate dataSaida = LocalDate.now();
        Funcionario funcionario = obterFuncionario(idFuncionario);

        validarDemitirFuncionario(funcionario);

        demitirResponsavelDepartamento(funcionario);

        desativarUsuarioEFuncionario(funcionario, dataSaida, demissaoDto);
    }

    private Funcionario obterFuncionario(Long idFuncionario) {
        return funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_FUNCINARIO_NAO_ENCONTRADO));
    }

    private void validarDemitirFuncionario(Funcionario funcionario) {
        if (ValidatorUtil.isNotEmpty(funcionario.getDataSaida())) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DATA_SAIDA_FUNCINARIO);
        }
    }

    private void demitirResponsavelDepartamento(Funcionario funcionario) {
        if (responsavelDepartamentoService.existsByFuncionario(funcionario)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_USUARIO_FUNCIONARIO_EH_RESPONSAVEL_DEPARTAMENTO);
        }
    }

    private void desativarUsuarioEFuncionario(Funcionario funcionario, LocalDate dataSaida, DemissaoDto demissaoDto) {
        Pessoa pessoa = pessoaRepository.findById(funcionario.getPessoa().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_PESSOA_NAO_ENCONTRADA));

        Usuario usuario = usuarioRepository.findByPessoa(pessoa)
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_USUARIO_NAO_ENCONTRADO));

        funcionario.setDataSaida(dataSaida);
        usuario.setAtivo(false);
        pessoa.setAtivo(false);
        demissaoFuncionarioService.salvarDemissao(funcionario, dataSaida, demissaoDto);

        pessoaRepository.save(pessoa);
        usuarioRepository.save(usuario);
        funcionarioRepository.save(funcionario);
    }
}