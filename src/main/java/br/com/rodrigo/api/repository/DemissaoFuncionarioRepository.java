package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.DemissaoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemissaoFuncionarioRepository extends JpaRepository<DemissaoFuncionario, Long> {
}
