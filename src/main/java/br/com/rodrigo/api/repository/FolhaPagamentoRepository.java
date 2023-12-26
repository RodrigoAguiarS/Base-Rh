package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.FolhaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long> {
    @Query("SELECT COUNT(f) > 0 FROM FolhaPagamento f WHERE MONTH(f.dataPagamento) = :mesAtual AND YEAR(f.dataPagamento) = :anoAtual")
    boolean existsByMesAndAno(@Param("mesAtual") int mesAtual, @Param("anoAtual") int anoAtual);
}
