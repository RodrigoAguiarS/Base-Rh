package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.TipoDesconto;
import br.com.rodrigo.api.model.dto.TipoDescontoDto;
import br.com.rodrigo.api.service.TipoDescontoService;
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
@RequestMapping("/api/descontos")
@RequiredArgsConstructor
public class TipoDescontoController {

    private final TipoDescontoService tipoDescontoService;

    @GetMapping
    public ResponseEntity<List<TipoDesconto>> listarTodos() {
        List<TipoDesconto> tipoDescontos = tipoDescontoService.getAllTipoDemissoes();
        return ResponseEntity.ok(tipoDescontos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDesconto> obterPorId(@PathVariable Long id) {
        return tipoDescontoService.getTipoDescontoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoDescontoDto> criarTipoDesconto(@Valid @RequestBody TipoDescontoDto tipoDescontoDto) {
        TipoDescontoDto novoTipoDescontoDto = tipoDescontoService.salvarTipoDesconto(tipoDescontoDto);
        return new ResponseEntity<>(novoTipoDescontoDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDescontoDto> atualizarTipoDesconto(@Valid @PathVariable Long id, @RequestBody TipoDescontoDto tipoDemissaoDto) {
        TipoDescontoDto tipoDescontoAtualizadoDto = tipoDescontoService.atualizarTipoDesconto(id, tipoDemissaoDto);
        return new ResponseEntity<>(tipoDescontoAtualizadoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTipoDesconto(@PathVariable Long id) {
        tipoDescontoService.deleteTipoDesconto(id);
        return ResponseEntity.noContent().build();
    }
}

