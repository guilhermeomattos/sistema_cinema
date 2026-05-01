package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.dto.SalaRequestDTO;
import br.edu.faculdade.cinema.dto.SalaResponseDTO;
import br.edu.faculdade.cinema.model.TipoSala;
import br.edu.faculdade.cinema.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("salas", salaService.listarTodas());
        return "salas/list";
    }

    @GetMapping("/nova")
    public String novaForm(Model model) {
        model.addAttribute("sala", new SalaRequestDTO());
        model.addAttribute("tiposSala", TipoSala.values());
        model.addAttribute("salaId", null);
        return "salas/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("sala") SalaRequestDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("tiposSala", TipoSala.values());
            return "salas/form";
        }
        salaService.salvar(dto);
        ra.addFlashAttribute("sucesso", "Sala cadastrada com sucesso!");
        return "redirect:/salas";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        SalaResponseDTO s = salaService.buscarPorId(id);
        SalaRequestDTO dto = SalaRequestDTO.builder()
            .numero(s.getNumero()).capacidade(s.getCapacidade()).tipo(s.getTipo()).build();
        model.addAttribute("sala", dto);
        model.addAttribute("salaId", id);
        model.addAttribute("tiposSala", TipoSala.values());
        return "salas/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("sala") SalaRequestDTO dto,
                            BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("tiposSala", TipoSala.values());
            return "salas/form";
        }
        salaService.atualizar(id, dto);
        ra.addFlashAttribute("sucesso", "Sala atualizada com sucesso!");
        return "redirect:/salas";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        salaService.deletar(id);
        ra.addFlashAttribute("sucesso", "Sala removida com sucesso.");
        return "redirect:/salas";
    }
}
