package br.edu.faculdade.cinema.exception;

public class SessaoNotFoundException extends CinemaNotFoundException {
    public SessaoNotFoundException(Long id) {
        super("Sessao não encontrado(a) com id: " + id);
    }
}
