package br.edu.faculdade.cinema.exception;

public class FilmeNotFoundException extends CinemaNotFoundException {
    public FilmeNotFoundException(Long id) {
        super("Filme não encontrado(a) com id: " + id);
    }
}
