package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.TipoDemissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  TipoDemissaoRepository extends JpaRepository<TipoDemissao, Long> {

    // Consulta todos os tipos de demissão ativos
    List<TipoDemissao> findByAtivoTrue();

    // Consulta um tipo de demissão ativo por ID
    Optional<TipoDemissao> findByIdAndAtivoTrue(Long id);
}
