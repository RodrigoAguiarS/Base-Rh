package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.Vinculo;
import br.com.rodrigo.api.model.dto.VinculoDto;
import br.com.rodrigo.api.service.VinculoService;
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
@RequestMapping("/api/vinculos")
@RequiredArgsConstructor
public class VinculoController {

    private final VinculoService vinculoService;

    @GetMapping
    public ResponseEntity<List<Vinculo>> listarTodos() {
        List<Vinculo> vinculos = vinculoService.getAllVinculos();
        return ResponseEntity.ok(vinculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vinculo> obterPorId(@PathVariable Long id) {
        return vinculoService.getVinculoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VinculoDto> criarVinculo(@Valid @RequestBody VinculoDto vinculoDto) {
        VinculoDto novoVinculoDto = vinculoService.salvarVinculo(vinculoDto);
        return new ResponseEntity<>(novoVinculoDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinculoDto> atualizarVinculo(@Valid @PathVariable Long id, @RequestBody VinculoDto vinculoDto) {
        VinculoDto vinculoAtualizadoDto = vinculoService.atualizarVinculo(id, vinculoDto);
        return new ResponseEntity<>(vinculoAtualizadoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVinculo(@PathVariable Long id) {
        vinculoService.deleteVinculo(id);
        return ResponseEntity.noContent().build();
    }
}
