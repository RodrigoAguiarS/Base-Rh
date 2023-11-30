package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import br.com.rodrigo.api.model.dto.CargoDto;
import br.com.rodrigo.api.model.dto.DetalhesCargoDto;
import br.com.rodrigo.api.service.CargoService;
import br.com.rodrigo.api.service.ResponsavelDepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    private final ResponsavelDepartamentoService responsavelDepartamentoService;

    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoService.getAllCargos();
        return new ResponseEntity<>(cargos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable Long id) {
        Optional<Cargo> cargo = cargoService.getCargoById(id);
        return cargo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CargoDto> saveCargo(@Valid @RequestBody CargoDto cargoDto) {
        CargoDto savedCargoDto = cargoService.salvarCargo(cargoDto);
        return new ResponseEntity<>(savedCargoDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoDto> updateCargo(@Valid @PathVariable Long id, @RequestBody CargoDto cargoDto) {
        CargoDto updatedCargoDto = cargoService.atualizarCargo(id, cargoDto);
        return new ResponseEntity<>(updatedCargoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id) {
        cargoService.deleteCargo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/detalhes/{id}")
    public ResponseEntity<DetalhesCargoDto> obterDetalhesDepartamento(@PathVariable Long id) {

        Cargo cargo = cargoService.getCargoById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO));

        ResponsavelDepartamento responsavelAtual = responsavelDepartamentoService.obterResponsavelAtual(cargo.getDepartamento().getId());

        DetalhesCargoDto detalhesDto = DetalhesCargoDto.fromEntity(cargo, responsavelAtual);

        return ResponseEntity.ok(detalhesDto);
    }
}
