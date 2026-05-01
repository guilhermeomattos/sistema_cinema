package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.dto.ReservaRequestDTO;
import br.edu.faculdade.cinema.service.ClienteService;
import br.edu.faculdade.cinema.service.ReservaService;
import br.edu.faculdade.cinema.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final ClienteService clienteService;
    private final SessaoService sessaoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listarTodas());
        return "reservas/list";
    }

    @GetMapping("/nova")
    public String novaForm(Model model) {
        model.addAttribute("reserva", new ReservaRequestDTO());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("sessoes", sessaoService.listarDisponiveisAbertas());
        return "reservas/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("reserva") ReservaRequestDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("sessoes", sessaoService.listarDisponiveisAbertas());
            return "reservas/form";
        }
        reservaService.salvar(dto);
        ra.addFlashAttribute("sucesso", "Reserva realizada com sucesso!");
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        reservaService.cancelar(id);
        ra.addFlashAttribute("sucesso", "Reserva cancelada com sucesso.");
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        reservaService.deletar(id);
        ra.addFlashAttribute("sucesso", "Reserva removida com sucesso.");
        return "redirect:/reservas";
    }
}
