package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.dto.CadastroRegistroPontoDto;
import br.com.rodrigo.api.model.dto.RegistroPontoDto;
import br.com.rodrigo.api.service.RegistroPontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/registro-ponto")
@RequiredArgsConstructor
public class RegistroPontoController {

    private final RegistroPontoService registroPontoService;

    @PostMapping
    public ResponseEntity<RegistroPontoDto> registrarPonto(@RequestBody @Valid CadastroRegistroPontoDto cadastroRegistroPontoDto) {
        RegistroPontoDto novoRelogioPonto = registroPontoService.registrarPonto(cadastroRegistroPontoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRelogioPonto);
    }

    @GetMapping("/is-entrada")
    public boolean isEntrada() {
        return registroPontoService.isEntrada();
    }

    @GetMapping("/registros")
    public ResponseEntity<List<RegistroPontoDto>> listarTodos() {
        List<RegistroPontoDto> registros = registroPontoService.listarHorariosDeTrabalhoFuncionario();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroPontoDto> findById(@PathVariable Long id) {
        try {
            RegistroPontoDto registroPontoDto = registroPontoService.buscarPorIdRegistro(id);
            return ResponseEntity.ok(registroPontoDto);
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
