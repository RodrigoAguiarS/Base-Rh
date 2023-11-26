package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByCargoId(Long idCargo);
}
