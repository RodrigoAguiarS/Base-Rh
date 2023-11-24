package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.dto.DepartamentoDto;
import br.com.rodrigo.api.repository.CargoRepository;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import br.com.rodrigo.api.repository.ResponsavelDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_DERPARTAMENTO_CARGO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    private final CargoRepository cargoRepository;

    private final ResponsavelDepartamentoRepository responsavelDepartamentoRepository;

    public List<Departamento> listarTodosDepartamentos() {
        return departamentoRepository.findAll();
    }

    public List<Departamento> listarDepartamentosSemResponsavel() {
        // Listar apenas departamentos que não possuem responsável
        return departamentoRepository.findAll()
                .stream()
                .filter(departamento -> !responsavelDepartamentoRepository.existsByDepartamentoId(departamento.getId()))
                .collect(Collectors.toList());
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

        return optionalDepartamento.map(dep -> {
            dep.setNome(departamentoDto.getNome());
            dep.setDescricao(departamentoDto.getDescricao());
            return departamentoRepository.save(dep);
        }).orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO + id));
    }

    public void deleteDepartamento(Long id) {

        if (cargoRepository.existsByDepartamentoId(id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_DERPARTAMENTO_CARGO);
        }
        departamentoRepository.deleteById(id);
    }
}
