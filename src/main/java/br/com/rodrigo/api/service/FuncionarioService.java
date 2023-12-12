package br.com.rodrigo.api.service;


import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.DetalhesFuncionarioDto;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_CARGO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCINARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    private final CargoService cargoService;

    public boolean funcionarioTemVinculoComDepartamento(Funcionario funcionario) {
        return responsavelDepartamentoService.existsByFuncionario(funcionario);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public List<DetalhesFuncionarioDto> listarTodosDetalhesFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        return funcionarios.stream().map(funcionario -> {
                    ResponsavelDepartamento responsavelDepartamento = responsavelDepartamentoService
                            .findResponsavelDepartamentoByDepartamentoId(funcionario.getCargo().getDepartamento().getId());
                    return DetalhesFuncionarioDto.fromEntity(funcionario, responsavelDepartamento);
                })
                .collect(Collectors.toList());
    }
    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public void cadastrarFuncionario(CadastroUsuarioDto cadastroUsuarioDto, Pessoa pessoa) {
        Cargo cargo = cargoService.getCargoById(cadastroUsuarioDto.getCargo().getId())
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

        Cargo cargo = cargoService.getCargoById(cadastroUsuarioDto.getCargo().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO));

        funcionario.setCargo(cargo);
        funcionario.setDataEntrada(cadastroUsuarioDto.getDataEntrada());

        funcionarioRepository.save(funcionario);
    }

    public Funcionario buscarFuncionarioPorIdPessoa(Long idPessoa) {
        Optional<Funcionario> funcionarioOptional = Optional.ofNullable(funcionarioRepository.findByPessoaId(idPessoa));
        return funcionarioOptional
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_FUNCINARIO_NAO_ENCONTRADO));
    }
}