package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.FolhaPagamento;
import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.service.FuncionarioService;
import br.com.rodrigo.api.service.impl.salario.FolhaPagamentoService;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


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

        FolhaPagamento folhaPagamento = folhaPagamentoService.salvarFolhaPagamento(funcionario);
        return ResponseEntity.ok(folhaPagamento);
    }

    @PostMapping("/gerar-todos")
    public ResponseEntity<?> gerarFolhaPagamentoParaTodosFuncionarios() {
        try {
            List<FolhaPagamento> folhasPagamento = folhaPagamentoService.gerarFolhaPagamentoParaTodosFuncionarios();
            return ResponseEntity.ok(folhasPagamento);
        } catch (ViolocaoIntegridadeDadosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping(value = "/gerar-folha-pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> relatorio(@RequestParam(value = "ID_FUNCIONARIO",
            required = false, defaultValue = "") Long idFuncionario) throws SQLException {

        byte[] relatorioGerado = folhaPagamentoService.gerarRelatorio(idFuncionario);
        HttpHeaders headers = new HttpHeaders();
        String fileName = "relatorio-usuario.pdf";
        headers.setContentDispositionFormData("inline; filename=\"" + fileName + "\"", fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(relatorioGerado, headers, HttpStatus.OK);
    }
}

