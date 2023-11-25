package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.AtualizaCadastroResponsavelDepartamentoDto;
import br.com.rodrigo.api.model.dto.CadastroResponsavelDepartamentoDto;
import br.com.rodrigo.api.model.dto.ResponsavelDepartamentoDto;
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

@RestController
@RequestMapping("/api/responsavel_departamento")
@RequiredArgsConstructor
public class ResponsavelDepartamentoController {

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    @PostMapping("/vincular")
    public ResponseEntity<ResponsavelDepartamentoDto> vincularResponsavel(@Valid @RequestBody CadastroResponsavelDepartamentoDto responsavelDepartamentoDto) {
        ResponsavelDepartamentoDto responsavelDto = responsavelDepartamentoService.vincularResponsavelDepartamento(responsavelDepartamentoDto);
        return new ResponseEntity<>(responsavelDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ResponsavelDepartamento>> listarUsuarios() {
        List<ResponsavelDepartamento> responsavelDepartamentos =
                responsavelDepartamentoService.listarTodosResponsavelDepartamentos();
        return ResponseEntity.ok(responsavelDepartamentos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelDepartamentoDto> editarResponsavelDepartamento(@PathVariable Long id,
                                                                                    @RequestBody AtualizaCadastroResponsavelDepartamentoDto
                                                                                            responsavelDepartamentoDto) {
        ResponsavelDepartamentoDto responsavelAtualizado = responsavelDepartamentoService
                .editarResponsavelDepartamento(id, responsavelDepartamentoDto);
        return new ResponseEntity<>(responsavelAtualizado, HttpStatus.OK);
    }

    @GetMapping("/responsaveis/{id}")
    public ResponseEntity<ResponsavelDepartamento> buscarResponsavelDepartamentoPorId(@PathVariable Long id) {
        ResponsavelDepartamento responsavelDepartamento = responsavelDepartamentoService
                .buscarResponsavelDepartamentoPorId(id);
        return new ResponseEntity<>(responsavelDepartamento, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        responsavelDepartamentoService.deletaResponsabilidade(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
