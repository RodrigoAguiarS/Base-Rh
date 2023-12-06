package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.dto.RegistroPontoDto;
import br.com.rodrigo.api.service.RegistroPontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/registro-ponto")
@RequiredArgsConstructor
public class RegistroPontoController {

    private final RegistroPontoService registroPontoService;

    @PostMapping
    public ResponseEntity<RegistroPontoDto> registrarPonto(@RequestBody @Valid RegistroPontoDto registroPontoDto) {
        RegistroPontoDto novoRelogioPonto = registroPontoService.registrarPonto(registroPontoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRelogioPonto);
    }

    @GetMapping("/is-entrada")
    public boolean isEntrada() {
        return registroPontoService.isEntrada();
    }
}
