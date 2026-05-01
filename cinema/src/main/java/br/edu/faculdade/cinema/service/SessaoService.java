package br.edu.faculdade.cinema.service;

import br.edu.faculdade.cinema.dto.SessaoRequestDTO;
import br.edu.faculdade.cinema.dto.SessaoResponseDTO;
import br.edu.faculdade.cinema.exception.RegraDeNegocioException;
import br.edu.faculdade.cinema.exception.SessaoNotFoundException;
import br.edu.faculdade.cinema.model.*;
import br.edu.faculdade.cinema.repository.ReservaRepository;
import br.edu.faculdade.cinema.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final FilmeService filmeService;
    private final SalaService salaService;
    private final ReservaRepository reservaRepository;

    @Transactional
    public SessaoResponseDTO salvar(SessaoRequestDTO dto) {
        Filme filme = filmeService.buscarEntidadePorId(dto.getFilmeId());
        Sala sala   = salaService.buscarEntidadePorId(dto.getSalaId());

        // Regra 1: Filme deve estar em cartaz
        if (!filme.isEmCartaz()) {
            throw new RegraDeNegocioException("Não é possível criar sessão para um filme fora de cartaz.");
        }
        // Regra 2: Data e hora deve ser no futuro
        if (dto.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("A data/hora da sessão deve ser no futuro.");
        }

        Sessao sessao = Sessao.builder()
            .filme(filme).sala(sala).dataHora(dto.getDataHora())
            .preco(dto.getPreco()).status(StatusSessao.ABERTA)
            .build();

        return toResponseDTO(sessaoRepository.save(sessao));
    }

    @Transactional
    public void encerrar(Long id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(() -> new SessaoNotFoundException(id));
        sessao.setStatus(StatusSessao.ENCERRADA);
        sessaoRepository.save(sessao);
        log.info("Sessão id={} encerrada", id);
    }

    @Transactional(readOnly = true)
    public List<SessaoResponseDTO> listarTodas() {
        return sessaoRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SessaoResponseDTO> listarDisponiveisAbertas() {
        return sessaoRepository.findSessoesDisponiveisFuturas(LocalDateTime.now())
            .stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public SessaoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(sessaoRepository.findById(id).orElseThrow(() -> new SessaoNotFoundException(id)));
    }

    @Transactional(readOnly = true)
    public Sessao buscarEntidadePorId(Long id) {
        return sessaoRepository.findById(id).orElseThrow(() -> new SessaoNotFoundException(id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!sessaoRepository.existsById(id)) throw new SessaoNotFoundException(id);
        sessaoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarTotal() { return sessaoRepository.count(); }

    @Transactional(readOnly = true)
    public long contarAbertas() { return sessaoRepository.findByStatus(StatusSessao.ABERTA).size(); }

    private SessaoResponseDTO toResponseDTO(Sessao s) {
        long reservasAtivas = reservaRepository.countBySessaoIdAndStatusNot(s.getId(), StatusReserva.CANCELADA);
        int disponiveis = Math.max(0, s.getSala().getCapacidade() - (int) reservasAtivas);
        return SessaoResponseDTO.builder()
            .id(s.getId()).tituloFilme(s.getFilme().getTitulo())
            .numeroSala(s.getSala().getNumero()).dataHora(s.getDataHora())
            .preco(s.getPreco()).status(s.getStatus())
            .capacidadeSala(s.getSala().getCapacidade())
            .reservasAtivas(reservasAtivas).assentosDisponiveis(disponiveis)
            .build();
    }
}
