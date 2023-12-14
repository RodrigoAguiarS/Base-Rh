package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.Empresa;
import br.com.rodrigo.api.model.dto.DepartamentoDto;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_DEPARTAMENTO_RESPONSAVEL;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DELETAR_DEPARTAMENTO_CARGO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_EMPRESA_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    private final CargoService cargoService;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    private final EmpresaService empresaService;

    public List<Departamento> listaTodosDepartamentos() {
        return departamentoRepository.findAll();
    }

    public List<Departamento> listaDepartamentosSemResponsavel() {
        // Listar apenas departamentos que não possuem responsável
        return departamentoRepository.findAll()
                .stream()
                .filter(departamento -> !responsavelDepartamentoService.existsByDepartamentoId(departamento.getId()))
                .collect(Collectors.toList());
    }

    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Departamento cadastrarDepartamento(DepartamentoDto departamentoDto) {
        Empresa empresa = empresaService.ObterEmpresaPorId(departamentoDto.getEmpresa().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_EMPRESA_NAO_ENCONTRADO + departamentoDto.getEmpresa()));
        Departamento departamento = DepartamentoDto.toEntity(departamentoDto);
        departamento.setEmpresa(empresa);
        return departamentoRepository.save(departamento);
    }

    public Departamento atualizaDepartamento(Long id, DepartamentoDto departamentoDto) {
        Optional<Departamento> optionalDepartamento = departamentoRepository.findById(id);
        Empresa empresa = empresaService.ObterEmpresaPorId(departamentoDto.getEmpresa().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_EMPRESA_NAO_ENCONTRADO + departamentoDto.getEmpresa()));

        return optionalDepartamento.map(dep -> {
            dep.setNome(departamentoDto.getNome());
            dep.setDescricao(departamentoDto.getDescricao());
            dep.setEmpresa(empresa);
            return departamentoRepository.save(dep);
        }).orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO + id));
    }

    public void deleteDepartamento(Long id) {

        if (cargoService.existsByDepartamentoId(id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_DEPARTAMENTO_CARGO);
        }
        if (responsavelDepartamentoService.existsByDepartamentoId(id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_DELETAR_DEPARTAMENTO_RESPONSAVEL);
        }
        departamentoRepository.deleteById(id);
    }
}
