package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.ResponsavelDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelDepartamentoRepository extends JpaRepository<ResponsavelDepartamento, Long> {
}
