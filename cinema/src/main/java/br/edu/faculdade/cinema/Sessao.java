package br.edu.faculdade.cinema;

import java.time.LocalDateTime;

public class Sessao {

	private int id;

	private LocalDateTime dataHora;

	private double preco;

	private int Status;

	public boolean isDisponivel() {
		return false;
	}

	public int getOcupacao() {
		return 0;
	}

	public void encerrar() {

	}

	public double percentualOcupacao() {
		return 0;
	}

}
