package br.edu.faculdade.cinema.repository;

import br.edu.faculdade.cinema.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    boolean existsByNumero(Integer numero);
}
