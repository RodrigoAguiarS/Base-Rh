package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
