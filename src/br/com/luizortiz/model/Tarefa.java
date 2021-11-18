package br.com.luizortiz.model;

import java.time.LocalDate;

public class Tarefa {

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

}
