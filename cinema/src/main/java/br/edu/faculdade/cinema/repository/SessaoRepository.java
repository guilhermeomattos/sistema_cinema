package br.edu.faculdade.cinema.repository;

import br.edu.faculdade.cinema.model.Sessao;
import br.edu.faculdade.cinema.model.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    List<Sessao> findByStatus(StatusSessao status);
    List<Sessao> findByFilmeId(Long filmeId);

    @Query("SELECT s FROM Sessao s WHERE s.status = 'ABERTA' AND s.dataHora > :agora")
    List<Sessao> findSessoesDisponiveisFuturas(@Param("agora") LocalDateTime agora);
}
