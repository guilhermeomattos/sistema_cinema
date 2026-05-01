package br.edu.faculdade.cinema.service;

import br.edu.faculdade.cinema.dto.SalaRequestDTO;
import br.edu.faculdade.cinema.dto.SalaResponseDTO;
import br.edu.faculdade.cinema.exception.RegraDeNegocioException;
import br.edu.faculdade.cinema.exception.SalaNotFoundException;
import br.edu.faculdade.cinema.model.Sala;
import br.edu.faculdade.cinema.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional
    public SalaResponseDTO salvar(SalaRequestDTO dto) {
        if (salaRepository.existsByNumero(dto.getNumero())) {
            throw new RegraDeNegocioException("Já existe uma sala com o número: " + dto.getNumero());
        }
        return toResponseDTO(salaRepository.save(toEntity(dto)));
    }

    @Transactional
    public SalaResponseDTO atualizar(Long id, SalaRequestDTO dto) {
        Sala sala = salaRepository.findById(id).orElseThrow(() -> new SalaNotFoundException(id));
        sala.setNumero(dto.getNumero());
        sala.setCapacidade(dto.getCapacidade());
        sala.setTipo(dto.getTipo());
        return toResponseDTO(salaRepository.save(sala));
    }

    @Transactional(readOnly = true)
    public List<SalaResponseDTO> listarTodas() {
        return salaRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public SalaResponseDTO buscarPorId(Long id) {
        return toResponseDTO(salaRepository.findById(id).orElseThrow(() -> new SalaNotFoundException(id)));
    }

    @Transactional(readOnly = true)
    public Sala buscarEntidadePorId(Long id) {
        return salaRepository.findById(id).orElseThrow(() -> new SalaNotFoundException(id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!salaRepository.existsById(id)) throw new SalaNotFoundException(id);
        salaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarTotal() { return salaRepository.count(); }

    private Sala toEntity(SalaRequestDTO dto) {
        return Sala.builder().numero(dto.getNumero()).capacidade(dto.getCapacidade()).tipo(dto.getTipo()).build();
    }

    private SalaResponseDTO toResponseDTO(Sala s) {
        return SalaResponseDTO.builder().id(s.getId()).numero(s.getNumero())
            .capacidade(s.getCapacidade()).tipo(s.getTipo()).build();
    }
}
