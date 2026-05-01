package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.dto.SessaoRequestDTO;
import br.edu.faculdade.cinema.service.FilmeService;
import br.edu.faculdade.cinema.service.SalaService;
import br.edu.faculdade.cinema.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;
    private final FilmeService filmeService;
    private final SalaService salaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("sessoes", sessaoService.listarTodas());
        return "sessoes/list";
    }

    @GetMapping("/nova")
    public String novaForm(Model model) {
        model.addAttribute("sessao", new SessaoRequestDTO());
        model.addAttribute("filmes", filmeService.listarEmCartaz());
        model.addAttribute("salas", salaService.listarTodas());
        return "sessoes/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("sessao") SessaoRequestDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("filmes", filmeService.listarEmCartaz());
            model.addAttribute("salas", salaService.listarTodas());
            return "sessoes/form";
        }
        sessaoService.salvar(dto);
        ra.addFlashAttribute("sucesso", "Sessão criada com sucesso!");
        return "redirect:/sessoes";
    }

    @PostMapping("/{id}/encerrar")
    public String encerrar(@PathVariable Long id, RedirectAttributes ra) {
        sessaoService.encerrar(id);
        ra.addFlashAttribute("sucesso", "Sessão encerrada com sucesso.");
        return "redirect:/sessoes";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        sessaoService.deletar(id);
        ra.addFlashAttribute("sucesso", "Sessão removida com sucesso.");
        return "redirect:/sessoes";
    }
}
