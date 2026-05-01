package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.dto.FilmeRequestDTO;
import br.edu.faculdade.cinema.dto.FilmeResponseDTO;
import br.edu.faculdade.cinema.service.FilmeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/filmes")
@RequiredArgsConstructor
public class FilmeController {

    private final FilmeService filmeService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("filmes", filmeService.listarTodos());
        return "filmes/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("filme", new FilmeRequestDTO());
        model.addAttribute("filmeId", null);
        return "filmes/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("filme") FilmeRequestDTO dto,
                         BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "filmes/form";
        filmeService.salvar(dto);
        ra.addFlashAttribute("sucesso", "Filme cadastrado com sucesso!");
        return "redirect:/filmes";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        FilmeResponseDTO f = filmeService.buscarPorId(id);
        FilmeRequestDTO dto = FilmeRequestDTO.builder()
            .titulo(f.getTitulo()).genero(f.getGenero()).duracao(f.getDuracao())
            .classificacao(f.getClassificacao()).sinopse(f.getSinopse()).emCartaz(f.isEmCartaz())
            .build();
        model.addAttribute("filme", dto);
        model.addAttribute("filmeId", id);
        return "filmes/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("filme") FilmeRequestDTO dto,
                            BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "filmes/form";
        filmeService.atualizar(id, dto);
        ra.addFlashAttribute("sucesso", "Filme atualizado com sucesso!");
        return "redirect:/filmes";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        filmeService.deletar(id);
        ra.addFlashAttribute("sucesso", "Filme removido com sucesso.");
        return "redirect:/filmes";
    }
}
