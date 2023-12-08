package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    Optional<RegistroPonto> findByFuncionarioAndDataRegistroAndPontoRegistrado(Funcionario funcionario,
                                                                               LocalDate dataAtual, boolean pontoRegistrado);

    List<RegistroPonto> findByFuncionarioOrderByDataRegistroDescHoraEntradaDesc(Funcionario funcionario);
}
