package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.AtualizaCadastroResponsavelDepartamentoDto;
import br.com.rodrigo.api.model.dto.CadastroResponsavelDepartamentoDto;
import br.com.rodrigo.api.model.dto.ResponsavelDepartamentoDto;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.ResponsavelDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCINARIO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_RESPONSAVEL_DEPARTAMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class ResponsavelDepartamentoService {

    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoRepository responsavelDepartamentoRepository;

    private final DepartamentoRepository departamentoRepository;

    public ResponsavelDepartamentoDto vincularResponsavelDepartamento(CadastroResponsavelDepartamentoDto responsavelDepartamentoDto) {
        Funcionario funcionario = funcionarioRepository.findById(responsavelDepartamentoDto.getFuncionario())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_FUNCINARIO_NAO_ENCONTRADO));

        Departamento departamento = departamentoRepository.findById(responsavelDepartamentoDto.getDepartamento())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO));

        verificarResponsabilidadeExistente(funcionario.getId(), departamento.getId());
        verificarResponsavelDepartamentoExistente(departamento.getId());

        ResponsavelDepartamento responsavelDepartamento = new ResponsavelDepartamento();
        responsavelDepartamento.setFuncionario(funcionario);
        responsavelDepartamento.setDepartamento(departamento);

        ResponsavelDepartamento salvoResponsavelDepartamento = responsavelDepartamentoRepository.save(responsavelDepartamento);

        return ResponsavelDepartamentoDto.fromEntity(salvoResponsavelDepartamento);
    }

    public List<ResponsavelDepartamento> listarTodosResponsavelDepartamentos() {
        return responsavelDepartamentoRepository.findAll();

    }

    public void verificarResponsabilidadeExistente(Long idFuncionario, Long idDepartamento) {
        Optional<ResponsavelDepartamento> responsabilidadeExistente = responsavelDepartamentoRepository
                .findByFuncionarioIdAndDepartamentoId(idFuncionario, idDepartamento);
        if (responsabilidadeExistente.isPresent()) {
            throw new ViolocaoIntegridadeDadosException("O funcionário já é responsável por esta unidade.");
        }
    }

    public void verificarResponsavelDepartamentoExistente(Long idDepartamento) {
        if (responsavelDepartamentoRepository.existsByDepartamentoId(idDepartamento)) {
            throw new ViolocaoIntegridadeDadosException("Este departamento já possui um responsável.");
        }
    }

    public ResponsavelDepartamentoDto editarResponsavelDepartamento(Long id, AtualizaCadastroResponsavelDepartamentoDto
            responsavelDepartamentoDto) {

        ResponsavelDepartamento responsavelDepartamento = responsavelDepartamentoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_RESPONSAVEL_DEPARTAMENTO_NAO_ENCONTRADO));

        Funcionario funcionario = funcionarioRepository.findById(responsavelDepartamentoDto.getFuncionario().getId())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_FUNCINARIO_NAO_ENCONTRADO));

        Departamento departamento = departamentoRepository.findById(responsavelDepartamentoDto.getDepartamento().getId())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO));

        if (!responsavelDepartamento.getFuncionario().getId().equals(responsavelDepartamentoDto.getFuncionario().getId()) ||
                !responsavelDepartamento.getDepartamento().getId().equals(responsavelDepartamentoDto.getDepartamento().getId())) {

            verificarResponsabilidadeExistente(responsavelDepartamentoDto.getFuncionario().getId(), responsavelDepartamentoDto.getDepartamento().getId());

            responsavelDepartamento.setFuncionario(funcionario);
            responsavelDepartamento.setDepartamento(departamento);
        }

        ResponsavelDepartamento savedResponsavelDepartamento = responsavelDepartamentoRepository.save(responsavelDepartamento);
        return ResponsavelDepartamentoDto.fromEntity(savedResponsavelDepartamento);
    }

    public ResponsavelDepartamento buscarResponsavelDepartamentoPorId(Long id) {

        return responsavelDepartamentoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_RESPONSAVEL_DEPARTAMENTO_NAO_ENCONTRADO));
    }
}
