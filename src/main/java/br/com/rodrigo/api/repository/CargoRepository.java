package br.com.rodrigo.api.repository;

import br.com.rodrigo.api.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

}
