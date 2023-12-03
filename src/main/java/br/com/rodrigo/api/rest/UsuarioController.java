package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.SenhaRecuperacaoDto;
import br.com.rodrigo.api.model.dto.UsuarioFuncionarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.service.UsuarioService;
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
import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDto> criarUsuario(@RequestBody @Valid CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        UsuarioDto novoUsuarioDto = usuarioService.criarUsuario(cadastroUsuarioDto);
        return new ResponseEntity<>(novoUsuarioDto, HttpStatus.CREATED);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDto> atualizarUsuario(@Valid @PathVariable Long idUsuario, @RequestBody CadastroUsuarioDto cadastroUsuarioDto) throws ParseException {
        UsuarioDto usuarioAtualizadoDto = usuarioService.atualizarUsuario(idUsuario, cadastroUsuarioDto);
        if (ValidatorUtil.isNotEmpty(usuarioAtualizadoDto)) {
            return ResponseEntity.ok(usuarioAtualizadoDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> obterUsuarioPorId(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.obterUsuarioPorId(idUsuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/dados")
    public CadastroUsuarioDto obterUsuarioPorEmail(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.obterUsuarioPorEmail(email);
    }

    @GetMapping("/papel")
    public ResponseEntity<List<String>> getRoles(Authentication authentication) {
        String email = authentication.getName();
        List<String> roles = usuarioService.obterPerfis(email);
        return ResponseEntity.ok(roles);
    }

    @GetMapping()
    public ResponseEntity<List<Usuario>> listarUsuarios(Authentication authentication) {
        String email = authentication.getName();
        List<Usuario> usuarios = usuarioService.listarUsuarios(email);
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN_GERAL')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long idUsuario) {
        usuarioService.deletarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{usuarioId}/dados")
    public ResponseEntity<UsuarioFuncionarioDto> obterPessoaEFuncionarioCompleto(@PathVariable Long usuarioId) {
        UsuarioFuncionarioDto usuarioFuncionarioDto = usuarioService.obterUsuarioEhFuncionario(usuarioId);
        return ResponseEntity.ok(usuarioFuncionarioDto);
    }

    @PutMapping("/alterar-senha/{id}")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id,
                                               @RequestBody SenhaRecuperacaoDto senhaRecuperacaoDto) {
        usuarioService.alterarSenha(id, senhaRecuperacaoDto.getSenha());
        return ResponseEntity.ok().build();
    }
}
