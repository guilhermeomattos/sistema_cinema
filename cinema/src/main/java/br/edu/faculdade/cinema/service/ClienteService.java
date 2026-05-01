package br.edu.faculdade.cinema.service;

import br.edu.faculdade.cinema.dto.ClienteRequestDTO;
import br.edu.faculdade.cinema.dto.ClienteResponseDTO;
import br.edu.faculdade.cinema.exception.ClienteNotFoundException;
import br.edu.faculdade.cinema.exception.RegraDeNegocioException;
import br.edu.faculdade.cinema.model.Cliente;
import br.edu.faculdade.cinema.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        // Regra de negócio: CPF deve ser único
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CPF: " + dto.getCpf());
        }
        return toResponseDTO(clienteRepository.save(toEntity(dto)));
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
        if (!cliente.getCpf().equals(dto.getCpf()) && clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RegraDeNegocioException("Já existe um cliente com o CPF: " + dto.getCpf());
        }
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setTipoCliente(dto.getTipoCliente());
        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return toResponseDTO(clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id)));
    }

    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) throw new ClienteNotFoundException(id);
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarTotal() { return clienteRepository.count(); }

    private Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
            .nome(dto.getNome()).cpf(dto.getCpf())
            .dataNascimento(dto.getDataNascimento()).tipoCliente(dto.getTipoCliente())
            .build();
    }

    private ClienteResponseDTO toResponseDTO(Cliente c) {
        return ClienteResponseDTO.builder()
            .id(c.getId()).nome(c.getNome()).cpf(c.getCpf())
            .dataNascimento(c.getDataNascimento()).tipoCliente(c.getTipoCliente())
            .temMeiaEntrada(c.temMeiaEntrada())
            .build();
    }
}
