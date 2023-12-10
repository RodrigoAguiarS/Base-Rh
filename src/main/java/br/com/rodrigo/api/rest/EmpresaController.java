package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.dto.EmpresaDto;
import br.com.rodrigo.api.service.EmpresaService;
import br.com.rodrigo.api.util.ValidatorUtil;
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

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaDto> createEmpresa(@Valid @RequestBody EmpresaDto empresaDto) {
        EmpresaDto createdEmpresa = empresaService.cadastrarEmpresa(empresaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmpresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> updateEmpresa(@PathVariable Long id, @RequestBody EmpresaDto empresaDto) {
        EmpresaDto updatedEmpresa = empresaService.atualizarEmpresa(id, empresaDto);
        if (ValidatorUtil.isNotEmpty(updatedEmpresa)) {
            return ResponseEntity.ok(updatedEmpresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDto>> getAllEmpresas() {
        List<EmpresaDto> empresas = empresaService.getAllEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDto> getEmpresaById(@PathVariable Long id) {
        EmpresaDto empresa = empresaService.getEmpresaById(id);
        if (ValidatorUtil.isNotEmpty(empresa)) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.deleteEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}
