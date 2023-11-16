package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.CadastroUsuarioDto;
import br.com.rodrigo.api.model.dto.UsuarioDto;
import br.com.rodrigo.api.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
