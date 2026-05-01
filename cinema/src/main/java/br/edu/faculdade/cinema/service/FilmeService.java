package br.edu.faculdade.cinema.service;

import br.edu.faculdade.cinema.dto.FilmeRequestDTO;
import br.edu.faculdade.cinema.dto.FilmeResponseDTO;
import br.edu.faculdade.cinema.exception.FilmeNotFoundException;
import br.edu.faculdade.cinema.model.Filme;
import br.edu.faculdade.cinema.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmeService {

    private final FilmeRepository filmeRepository;

    @Transactional
    public FilmeResponseDTO salvar(FilmeRequestDTO dto) {
        log.info("Salvando filme: {}", dto.getTitulo());
        return toResponseDTO(filmeRepository.save(toEntity(dto)));
    }

    @Transactional
    public FilmeResponseDTO atualizar(Long id, FilmeRequestDTO dto) {
        Filme filme = filmeRepository.findById(id).orElseThrow(() -> new FilmeNotFoundException(id));
        filme.setTitulo(dto.getTitulo());
        filme.setGenero(dto.getGenero());
        filme.setDuracao(dto.getDuracao());
        filme.setClassificacao(dto.getClassificacao());
        filme.setSinopse(dto.getSinopse());
        filme.setEmCartaz(dto.isEmCartaz());
        return toResponseDTO(filmeRepository.save(filme));
    }

    @Transactional(readOnly = true)
    public List<FilmeResponseDTO> listarTodos() {
        return filmeRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<FilmeResponseDTO> listarEmCartaz() {
        return filmeRepository.findByEmCartazTrue().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public FilmeResponseDTO buscarPorId(Long id) {
        return toResponseDTO(filmeRepository.findById(id).orElseThrow(() -> new FilmeNotFoundException(id)));
    }

    @Transactional(readOnly = true)
    public Filme buscarEntidadePorId(Long id) {
        return filmeRepository.findById(id).orElseThrow(() -> new FilmeNotFoundException(id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!filmeRepository.existsById(id)) throw new FilmeNotFoundException(id);
        filmeRepository.deleteById(id);
        log.info("Filme id={} removido", id);
    }

    @Transactional(readOnly = true)
    public long contarTotal() { return filmeRepository.count(); }

    @Transactional(readOnly = true)
    public long contarEmCartaz() { return filmeRepository.findByEmCartazTrue().size(); }

    private Filme toEntity(FilmeRequestDTO dto) {
        return Filme.builder()
            .titulo(dto.getTitulo()).genero(dto.getGenero())
            .duracao(dto.getDuracao()).classificacao(dto.getClassificacao())
            .sinopse(dto.getSinopse()).emCartaz(dto.isEmCartaz())
            .build();
    }

    private FilmeResponseDTO toResponseDTO(Filme f) {
        return FilmeResponseDTO.builder()
            .id(f.getId()).titulo(f.getTitulo()).genero(f.getGenero())
            .duracao(f.getDuracao()).classificacao(f.getClassificacao())
            .sinopse(f.getSinopse()).emCartaz(f.isEmCartaz())
            .build();
    }
}
