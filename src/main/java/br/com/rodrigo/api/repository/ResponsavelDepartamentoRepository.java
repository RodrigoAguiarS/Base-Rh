package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.ResponsavelDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponsavelDepartamentoRepository extends JpaRepository<ResponsavelDepartamento, Long> {
    Optional<ResponsavelDepartamento> findByFuncionarioIdAndDepartamentoId(Long idFuncionario, Long idDepartamento);

    boolean existsByDepartamentoId(Long idDepartamento);

    ResponsavelDepartamento findResponsavelDepartamentoByDepartamentoId(Long idDepartamento);

    boolean existsByFuncionario(Funcionario funcionario);
}
