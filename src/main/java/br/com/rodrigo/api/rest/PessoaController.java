package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.service.PessoaService;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<UsuarioDto> criarUsuario(@RequestBody @Valid CadastroUsuarioDto cadastroUsuarioDto) {
        UsuarioDto novoUsuarioDto = pessoaService.criarUsuario(cadastroUsuarioDto);
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
}
