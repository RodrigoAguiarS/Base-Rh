package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.DepartamentoDto;
import br.com.rodrigo.api.model.dto.DetalhesDepartamentoDto;
import br.com.rodrigo.api.service.DepartamentoService;
import br.com.rodrigo.api.service.ResponsavelDepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        List<Departamento> departamentos = departamentoService.listaTodosDepartamentos();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @GetMapping("/semReponsavel")
    public ResponseEntity<List<Departamento>> getAllDepartamentosSemResponsavel() {
        List<Departamento> departamentos = departamentoService.listaDepartamentosSemResponsavel();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentoById(@PathVariable Long id) {
        Optional<Departamento> departamento = departamentoService.getDepartamentoById(id);
        return departamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Departamento> saveDepartamento(@Valid @RequestBody DepartamentoDto departamentoDto) {
        Departamento savedDepartamento = departamentoService.cadastrarDepartamento(departamentoDto);
        return new ResponseEntity<>(savedDepartamento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamento(@Valid @PathVariable Long id, @RequestBody DepartamentoDto departamentoDto) {
        Departamento updatedDepartamento = departamentoService.atualizaDepartamento(id, departamentoDto);
        return new ResponseEntity<>(updatedDepartamento, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        departamentoService.deleteDepartamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/responsavelAtual/{id}")
    public ResponseEntity<DetalhesDepartamentoDto> obterDetalhesDepartamento(@PathVariable Long id) {

        Departamento departamento = departamentoService.getDepartamentoById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO));

        ResponsavelDepartamento responsavelAtual = responsavelDepartamentoService.obterResponsavelAtual(id);

        DetalhesDepartamentoDto detalhesDto = DetalhesDepartamentoDto.fromEntity(departamento, responsavelAtual);

        return ResponseEntity.ok(detalhesDto);
    }
}