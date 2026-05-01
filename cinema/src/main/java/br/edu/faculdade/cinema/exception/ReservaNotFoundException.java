package br.edu.faculdade.cinema.exception;

public class ReservaNotFoundException extends CinemaNotFoundException {
    public ReservaNotFoundException(Long id) {
        super("Reserva não encontrado(a) com id: " + id);
    }
}
