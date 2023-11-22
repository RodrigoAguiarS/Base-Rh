package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.DepartamentoDto;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import br.com.rodrigo.api.repository.ResponsavelDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_FUNCINARIO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    private final FuncionarioRepository funcionarioRepository;

    private final ResponsavelDepartamentoRepository responsavelDepartamentoRepository;

    public List<Departamento> listarTodosDepartamentos() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Departamento saveDepartamento(DepartamentoDto departamentoDto) {
        Departamento departamento = new Departamento();
        departamento.setNome(departamentoDto.getNome());
        departamento.setDescricao(departamentoDto.getDescricao());
        return departamentoRepository.save(departamento);
    }

    public Departamento atualizarDepartamento(Long id, DepartamentoDto departamentoDto) {
        Optional<Departamento> optionalDepartamento = departamentoRepository.findById(id);

        Departamento departamento = optionalDepartamento.map(dep -> {
            dep.setNome(departamentoDto.getNome());
            dep.setDescricao(departamentoDto.getDescricao());
            return departamentoRepository.save(dep);
        }).orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO + id));

        return departamento;
    }

    public void deleteDepartamento(Long id) {
        departamentoRepository.deleteById(id);
    }

    public void vincularResponsavel(Long idDepartamento, Long idFuncionario) {
        Departamento departamento = departamentoRepository.findById(idDepartamento)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO));

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_FUNCINARIO_NAO_ENCONTRADO));

        ResponsavelDepartamento responsavelDepartamento = new ResponsavelDepartamento();
        responsavelDepartamento.setDepartamento(departamento);
        responsavelDepartamento.setFuncionario(funcionario);

        responsavelDepartamentoRepository.save(responsavelDepartamento);
    }
}
