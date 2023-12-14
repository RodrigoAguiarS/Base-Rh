package br.com.rodrigo.api.service;


import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.Vinculo;
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
import static br.com.rodrigo.api.exception.ValidationError.ERRO_VINCULO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    private final CargoService cargoService;

    private final VinculoService vinculoService;

    public boolean funcionarioTemVinculoComDepartamento(Funcionario funcionario) {
        return responsavelDepartamentoService.existsByFuncionario(funcionario);
    }

    public List<Funcionario> listaTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public List<DetalhesFuncionarioDto> listaTodosDetalhesFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        return funcionarios.stream().map(funcionario -> {
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
}