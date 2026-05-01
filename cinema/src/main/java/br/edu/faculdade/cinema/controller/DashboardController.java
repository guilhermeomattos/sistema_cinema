package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final FilmeService filmeService;
    private final ClienteService clienteService;
    private final SessaoService sessaoService;
    private final ReservaService reservaService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalFilmes",        filmeService.contarTotal());
        model.addAttribute("filmesEmCartaz",     filmeService.contarEmCartaz());
        model.addAttribute("totalClientes",      clienteService.contarTotal());
        model.addAttribute("totalSessoes",       sessaoService.contarTotal());
        model.addAttribute("sessoesAbertas",     sessaoService.contarAbertas());
        model.addAttribute("totalReservas",      reservaService.contarTotal());
        model.addAttribute("reservasConfirmadas",reservaService.contarConfirmadas());
        model.addAttribute("receitaTotal",       reservaService.calcularReceitaTotal());
        model.addAttribute("ultimasReservas",    reservaService.listarTodas());
        return "dashboard";
    }
}
