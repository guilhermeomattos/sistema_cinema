package br.edu.faculdade.cinema.repository;

import br.edu.faculdade.cinema.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByEmCartazTrue();
    List<Filme> findByTituloContainingIgnoreCase(String titulo);
    List<Filme> findByGenero(String genero);
}
