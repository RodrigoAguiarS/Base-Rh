package br.com.rodrigo.api.service;


import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Pessoa;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.DetalhesFuncionarioDto;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.ResponsavelDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_PESSOA_EH_FUNCIONARIO;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoRepository responsavelDepartamentoRepository;

    public boolean funcionarioTemVinculoComDepartamento(Funcionario funcionario) {
        return responsavelDepartamentoRepository.existsByFuncionario(funcionario);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public List<DetalhesFuncionarioDto> listarTodosDetalhesFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        return funcionarios.stream().map(funcionario -> {
                    ResponsavelDepartamento responsavelDepartamento = responsavelDepartamentoRepository
                            .findResponsavelDepartamentoByDepartamentoId(funcionario.getCargo().getDepartamento().getId());
                    return DetalhesFuncionarioDto.fromEntity(funcionario, responsavelDepartamento);
                })
                .collect(Collectors.toList());
    }
    public void excluirFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public void verificarPessoaFuncionario(Pessoa pessoa) {
        boolean isPessoaFuncionario = funcionarioRepository.existsByPessoaId(pessoa.getId());

        if (isPessoaFuncionario) {
            throw new ViolocaoIntegridadeDadosException(ERRO_PESSOA_EH_FUNCIONARIO);
        }
    }
}