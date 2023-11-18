package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.service.EmailService;
import br.com.rodrigo.api.service.PessoaService;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import static br.com.rodrigo.api.util.EmailMensagensUtil.CONFIRMACAO_CADASTRO;
import static br.com.rodrigo.api.util.EmailMensagensUtil.getEmailCadastroTexto;


@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<UsuarioDto> criarUsuario(@RequestBody @Valid CadastroUsuarioDto cadastroUsuarioDto) {
        UsuarioDto novoUsuarioDto = pessoaService.criarUsuario(cadastroUsuarioDto);
        String mensagemEmail = getEmailCadastroTexto(cadastroUsuarioDto.getPessoa().getNome(),
                cadastroUsuarioDto.getEmail(), cadastroUsuarioDto.getSenha());
        emailService.sendEmail(cadastroUsuarioDto.getEmail(), CONFIRMACAO_CADASTRO, mensagemEmail);
        return new ResponseEntity<>(novoUsuarioDto, HttpStatus.CREATED);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDto> atualizarUsuario(@Valid @PathVariable Long idUsuario, @RequestBody CadastroUsuarioDto cadastroUsuarioDto) {
        UsuarioDto usuarioAtualizadoDto = pessoaService.atualizarUsuario(idUsuario, cadastroUsuarioDto);
        if (ValidatorUtil.isNotEmpty(usuarioAtualizadoDto)) {
            return ResponseEntity.ok(usuarioAtualizadoDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDto> obterUsuarioPorId(@PathVariable Long idUsuario) {
        UsuarioDto usuarioDto = pessoaService.obterUsuarioPorId(idUsuario);
        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }

    @GetMapping("/dados")
    public CadastroUsuarioDto obterUsuarioPorEmail(Authentication authentication) {
        String email = authentication.getName();
        return pessoaService.obterUsuarioPorEmail(email);
    }

    @GetMapping("/papel")
    public ResponseEntity<List<String>> getRoles(Authentication authentication) {
        String email = authentication.getName();
        List<String> roles = pessoaService.obterPerfis(email);
        return ResponseEntity.ok(roles);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN_GERAL')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long idUsuario) {
        pessoaService.deletarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
