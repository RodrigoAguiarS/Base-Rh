package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.ResetarSenhaToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetarSenhaRepository extends JpaRepository<ResetarSenhaToken, Long> {
    Optional<ResetarSenhaToken> findByUidAndAtivo(String uid, boolean ativo);
}
