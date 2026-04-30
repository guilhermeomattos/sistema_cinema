package br.edu.faculdade.cinema;

import java.time.LocalDateTime;

public class Cliente {

	private int id;

	private String Nome;

	private String CPF;

	private LocalDateTime dataNascimento;

	private int tipoCliente;

	public boolean temMeiaEntrada() {
		return false;
	}

}
