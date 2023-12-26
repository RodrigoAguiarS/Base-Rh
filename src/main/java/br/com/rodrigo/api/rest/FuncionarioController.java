package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.dto.DemissaoDto;
import br.com.rodrigo.api.model.dto.DetalhesFuncionarioDto;
import br.com.rodrigo.api.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping()
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.listaTodosFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Long id) {
        funcionarioService.deletaFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detalhes")
    public List<DetalhesFuncionarioDto> listarTodosDetalhesFuncionarios() {
        return funcionarioService.listaTodosDetalhesFuncionarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> obterFuncionarioPorId(@PathVariable Long id) {
        try {
            Funcionario funcionario = funcionarioService.obterFuncionarioPorId(id);
            return new ResponseEntity<>(funcionario, HttpStatus.OK);
        } catch (ViolocaoIntegridadeDadosException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{funcionarioId}/atribuir-tipos-desconto")
    public ResponseEntity<Funcionario> atribuirTiposDesconto(
            @PathVariable Long funcionarioId,
            @RequestBody ArrayList<Long> tiposDescontoDto) {

        Funcionario funcionario = funcionarioService.atribuirTiposDesconto(funcionarioId, tiposDescontoDto);
        return ResponseEntity.ok(funcionario);
    }

    @PostMapping("/demitir/{id}")
    public ResponseEntity<String> demitirFuncionario(@PathVariable Long id, @RequestBody DemissaoDto demissaoDto) {
        try {
            funcionarioService.demitirFuncionario(id, demissaoDto);
            return ResponseEntity.noContent().build();
        } catch (ViolocaoIntegridadeDadosException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
