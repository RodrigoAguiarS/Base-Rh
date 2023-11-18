package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.ResetarSenhaToken;
import br.com.rodrigo.api.model.dto.EmailRecuperacaoDto;
import br.com.rodrigo.api.model.dto.SenhaRecuperacaoDto;
import br.com.rodrigo.api.service.ResetarEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class ResetarSenhaController {

    private final ResetarEmailService resetarEmailService;

    @PostMapping("/login-alterar")
    public ResponseEntity<?> solicitarRedefinicaoSenha(@RequestBody EmailRecuperacaoDto solicitacao) {
        resetarEmailService.solicitarRedefinicaoSenha(solicitacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-alterar/{uid}")
    public ResponseEntity<String> exibirPaginaRedefinicaoSenha(@PathVariable("uid") String uid) {
        Optional<ResetarSenhaToken> tokenOptional = resetarEmailService.buscarTokenAtivoPorUid(uid);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(uid);
        }
    }

    @PostMapping("/login-alterar/{uid}")
    public ResponseEntity<?> atualizarSenha(@PathVariable("uid") String uid,
                                            @RequestBody SenhaRecuperacaoDto solicitacao) {
        resetarEmailService.atualizarSenha(uid, solicitacao);
        return ResponseEntity.ok().build();
    }
}
