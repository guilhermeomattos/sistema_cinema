package br.edu.faculdade.cinema.repository;

import br.edu.faculdade.cinema.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    List<Ingresso> findByReservaId(Long reservaId);
    boolean existsByReservaIdAndNumeroAssento(Long reservaId, Integer numeroAssento);
}
