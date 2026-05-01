package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.dto.ClienteRequestDTO;
import br.edu.faculdade.cinema.dto.ClienteResponseDTO;
import br.edu.faculdade.cinema.model.TipoCliente;
import br.edu.faculdade.cinema.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        model.addAttribute("tiposCliente", TipoCliente.values());
        model.addAttribute("clienteId", null);
        return "clientes/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("cliente") ClienteRequestDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("tiposCliente", TipoCliente.values());
            return "clientes/form";
        }
        clienteService.salvar(dto);
        ra.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        ClienteResponseDTO c = clienteService.buscarPorId(id);
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
            .nome(c.getNome()).cpf(c.getCpf())
            .dataNascimento(c.getDataNascimento()).tipoCliente(c.getTipoCliente())
            .build();
        model.addAttribute("cliente", dto);
        model.addAttribute("clienteId", id);
        model.addAttribute("tiposCliente", TipoCliente.values());
        return "clientes/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("cliente") ClienteRequestDTO dto,
                            BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("tiposCliente", TipoCliente.values());
            return "clientes/form";
        }
        clienteService.atualizar(id, dto);
        ra.addFlashAttribute("sucesso", "Cliente atualizado com sucesso!");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        clienteService.deletar(id);
        ra.addFlashAttribute("sucesso", "Cliente removido com sucesso.");
        return "redirect:/clientes";
    }
}
