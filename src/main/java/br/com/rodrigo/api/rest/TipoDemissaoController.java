package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.TipoDemissao;
import br.com.rodrigo.api.model.dto.TipoDemissaoDto;
import br.com.rodrigo.api.service.TipoDemissaoService;
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
@RequestMapping("/api/tipoDemissao")
@RequiredArgsConstructor
public class TipoDemissaoController {

    private final TipoDemissaoService tipoDemissaoService;

    @GetMapping
    public ResponseEntity<List<TipoDemissao>> listarTodos() {
        List<TipoDemissao> tipoDemissaos = tipoDemissaoService.getAllTipoDemissoes();
        return ResponseEntity.ok(tipoDemissaos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDemissao> obterPorId(@PathVariable Long id) {
        return tipoDemissaoService.getTipoDemissaoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoDemissaoDto> criarTipoDemissao(@Valid @RequestBody TipoDemissaoDto tipoDemissaoDto) {
        TipoDemissaoDto novoTipoDemissaoDto = tipoDemissaoService.salvarTipoDemissao(tipoDemissaoDto);
        return new ResponseEntity<>(novoTipoDemissaoDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDemissaoDto> atualizarTipoDemissao(@Valid @PathVariable Long id, @RequestBody TipoDemissaoDto tipoDemissaoDto) {
        TipoDemissaoDto tipoDemissaoAtualizadoDto = tipoDemissaoService.atualizarTipoDemissao(id, tipoDemissaoDto);
        return new ResponseEntity<>(tipoDemissaoAtualizadoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTipoDemissao(@PathVariable Long id) {
        tipoDemissaoService.deleteTipoDemissao(id);
        return ResponseEntity.noContent().build();
    }
}
