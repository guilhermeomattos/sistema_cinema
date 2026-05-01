package br.edu.faculdade.cinema.repository;

import br.edu.faculdade.cinema.model.Reserva;
import br.edu.faculdade.cinema.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);
    List<Reserva> findBySessaoId(Long sessaoId);
    List<Reserva> findByStatus(StatusReserva status);
    long countBySessaoIdAndStatusNot(Long sessaoId, StatusReserva status);
}
