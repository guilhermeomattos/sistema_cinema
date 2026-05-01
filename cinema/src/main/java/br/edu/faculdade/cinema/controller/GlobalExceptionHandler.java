package br.edu.faculdade.cinema.controller;

import br.edu.faculdade.cinema.exception.CinemaNotFoundException;
import br.edu.faculdade.cinema.exception.RegraDeNegocioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CinemaNotFoundException.class)
    public String handleNotFound(CinemaNotFoundException ex, Model model) {
        log.error("Recurso não encontrado: {}", ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        return "erro";
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public String handleRegra(RegraDeNegocioException ex, Model model) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        return "erro";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        model.addAttribute("erro", "Ocorreu um erro inesperado. Tente novamente.");
        return "erro";
    }
}
