package br.edu.faculdade.cinema.service;

import br.edu.faculdade.cinema.dto.ReservaRequestDTO;
import br.edu.faculdade.cinema.dto.ReservaResponseDTO;
import br.edu.faculdade.cinema.exception.RegraDeNegocioException;
import br.edu.faculdade.cinema.exception.ReservaNotFoundException;
import br.edu.faculdade.cinema.model.*;
import br.edu.faculdade.cinema.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteService clienteService;
    private final SessaoService sessaoService;

    @Transactional
    public ReservaResponseDTO salvar(ReservaRequestDTO dto) {
        Cliente cliente = clienteService.buscarEntidadePorId(dto.getClienteId());
        Sessao  sessao  = sessaoService.buscarEntidadePorId(dto.getSessaoId());

        // Regra 1: Sessão disponível
        if (!sessao.isDisponivel()) {
            throw new RegraDeNegocioException("A sessão selecionada não está disponível para reservas.");
        }
        // Regra 2: Verificar lotação
        long reservasAtivas = reservaRepository.countBySessaoIdAndStatusNot(sessao.getId(), StatusReserva.CANCELADA);
        if (reservasAtivas >= sessao.getSala().getCapacidade()) {
            throw new RegraDeNegocioException("Não há assentos disponíveis para esta sessão.");
        }
        // Regra 3: Meia-entrada para estudante ou idoso
        boolean meia = cliente.temMeiaEntrada();
        double valorTotal = meia ? sessao.getPreco() * 0.5 : sessao.getPreco();

        Reserva reserva = Reserva.builder()
            .cliente(cliente).sessao(sessao)
            .status(StatusReserva.CONFIRMADA).valorTotal(valorTotal)
            .build();

        Reserva salva = reservaRepository.save(reserva);
        log.info("Reserva id={} | cliente={} | sessão={} | meia={}", salva.getId(), cliente.getNome(), sessao.getId(), meia);
        return toResponseDTO(salva);
    }

    @Transactional
    public void cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException(id));
        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new RegraDeNegocioException("Esta reserva já foi cancelada.");
        }
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
        log.info("Reserva id={} cancelada", id);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public ReservaResponseDTO buscarPorId(Long id) {
        return toResponseDTO(reservaRepository.findById(id).orElseThrow(() -> new ReservaNotFoundException(id)));
    }

    @Transactional
    public void deletar(Long id) {
        if (!reservaRepository.existsById(id)) throw new ReservaNotFoundException(id);
        reservaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarTotal() { return reservaRepository.count(); }

    @Transactional(readOnly = true)
    public long contarConfirmadas() { return reservaRepository.findByStatus(StatusReserva.CONFIRMADA).size(); }

    @Transactional(readOnly = true)
    public double calcularReceitaTotal() {
        return reservaRepository.findByStatus(StatusReserva.CONFIRMADA)
            .stream().mapToDouble(Reserva::getValorTotal).sum();
    }

    private ReservaResponseDTO toResponseDTO(Reserva r) {
        return ReservaResponseDTO.builder()
            .id(r.getId()).nomeCliente(r.getCliente().getNome())
            .tituloFilme(r.getSessao().getFilme().getTitulo())
            .dataHoraSessao(r.getSessao().getDataHora())
            .dataReserva(r.getDataReserva()).status(r.getStatus())
            .valorTotal(r.getValorTotal()).meiaEntrada(r.getCliente().temMeiaEntrada())
            .build();
    }
}
