package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.TipoDesconto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoDescontoRepository extends JpaRepository<TipoDesconto, Long> {

    List<TipoDesconto> findByAtivoTrue();

    Optional<TipoDesconto> findByIdAndAtivoTrue(Long id);
}
