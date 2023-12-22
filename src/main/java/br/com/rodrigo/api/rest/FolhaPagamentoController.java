package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.FolhaPagamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.service.FuncionarioService;
import br.com.rodrigo.api.service.impl.salario.FolhaPagamentoService;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folhapagamentos")
@RequiredArgsConstructor
public class FolhaPagamentoController {

    private final FolhaPagamentoService folhaPagamentoService;

    private final FuncionarioService funcionarioService;

    @PostMapping("/gerar/{funcionarioId}")
    public ResponseEntity<FolhaPagamento> gerarFolhaPagamento(@PathVariable Long funcionarioId) {
        Funcionario funcionario = funcionarioService.obterFuncionarioPorId(funcionarioId);
        if (ValidatorUtil.isEmpty(funcionario)) {
            return ResponseEntity.notFound().build();
        }

        FolhaPagamento folhaPagamento = folhaPagamentoService.gerarFolhaPagamento(funcionario);
        return ResponseEntity.ok(folhaPagamento);
    }
}
