package br.com.luizortiz.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa implements Comparable<Tarefa> {

	private long id;
	private LocalDate dataCriacao;
	private LocalDate dataLimite;
	private LocalDate dataConcluida;
	private String autor;
	private String descricao;
	private String comentario;
	private StatusTarefa status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}

	public LocalDate getDataConcluida() {
		return dataConcluida;
	}

	public void setDataConcluida(LocalDate dataConcluida) {
		this.dataConcluida = dataConcluida;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public StatusTarefa getStatus() {
		return status;
	}

	public void setStatus(StatusTarefa status) {
		this.status = status;
	}

	public String formattoSave() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getId() + ";");
		DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		builder.append(this.getDataCriacao().format(padraoData) + ";");
		builder.append(this.getDataLimite().format(padraoData) + ";");
		if (this.getDataConcluida() != null) {
			builder.append(this.getDataConcluida().format(padraoData));
		}
		builder.append(";");
		builder.append(this.getDescricao() + ";");
		builder.append(this.getAutor() + ";");
		builder.append(this.getComentario() + ";");
		builder.append(this.getStatus().ordinal() + "\n");
		return builder.toString();
	}

	@Override
	public int compareTo(Tarefa o) {
		if (this.getDataLimite().isBefore(o.getDataLimite())) {
			return -1;
		} else if (this.getDataLimite().isAfter(o.getDataLimite())) {
			return 1;
		} else {
			return this.getDescricao().compareTo(o.getDescricao());
		}

	}
}
