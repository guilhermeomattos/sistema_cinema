package br.edu.faculdade.cinema.exception;

public class ClienteNotFoundException extends CinemaNotFoundException {
    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado(a) com id: " + id);
    }
}
