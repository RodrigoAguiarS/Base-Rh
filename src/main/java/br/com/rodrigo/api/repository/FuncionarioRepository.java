package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.TipoDesconto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByCargoId(Long idCargo);

    boolean existsByPessoaId(Long idPessoa);

    List<Funcionario> findByPessoaAtivoTrue();

    boolean existsByPessoaIdAndIdNot(Long id, Long funcionarioId);

    Funcionario findByPessoaId(Long pessoaId);
}
