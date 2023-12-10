package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
