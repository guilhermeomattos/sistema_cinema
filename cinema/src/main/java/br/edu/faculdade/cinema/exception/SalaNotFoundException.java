package br.edu.faculdade.cinema.exception;

public class SalaNotFoundException extends CinemaNotFoundException {
    public SalaNotFoundException(Long id) {
        super("Sala não encontrado(a) com id: " + id);
    }
}
