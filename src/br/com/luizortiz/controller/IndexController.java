package br.com.luizortiz.controller;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import br.com.luizortiz.model.StatusTarefa;
import br.com.luizortiz.model.Tarefa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class IndexController {

	@FXML
	private TextField tfTarefa;

	@FXML
	private TextArea taComentarios;

	@FXML
	private TextField tfAutor;

	@FXML
	private TextField tfStatus;

	@FXML
	private DatePicker dpData;

	@FXML
	private Button btLimpar;

	private Tarefa tarefa;

	@FXML
	private Button btConcluir;

	@FXML
	private Button btApagar;

	@FXML
	private Button btAdiar;

	@FXML
	private Button btSalvar;

	@FXML
	void btCleanerClick(ActionEvent event) {
		limpar();
	}

	@FXML
	void btExcludeClick(ActionEvent event) {

	}

	@FXML
	void btExtendClick(ActionEvent event) {

	}

	@FXML
	void btFinishedClick(ActionEvent event) {

	}

	@FXML
	void btSafeClick(ActionEvent event) {
		// Validando os Campos
		if (dpData.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Informe uma data valida", "Informe", JOptionPane.ERROR_MESSAGE);
			dpData.requestFocus();
		} else if (tfAutor.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o autor da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			tfAutor.requestFocus();
		} else if (tfTarefa.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe a descrição da tarefa", "Informe", JOptionPane.ERROR_MESSAGE);
			tfTarefa.requestFocus();
		} else if (dpData.getValue().isBefore(LocalDate.now())) {
			JOptionPane.showMessageDialog(null, "Informe uma data válida", "Informe", JOptionPane.ERROR_MESSAGE);
		} else {
			// Instanciando a Tarefa
			tarefa = new Tarefa();
			// Popular a tarefa
			tarefa.setDataCriacao(LocalDate.now());
			tarefa.setStatus(StatusTarefa.ABERTA);
			tarefa.setDataLimite(dpData.getValue());
			tarefa.setAutor(tfAutor.getText());
			tarefa.setDescricao(tfTarefa.getText());
			tarefa.setComentario(taComentarios.getText());

			System.out.println(tarefa.formattoSave());

			// TODO Inserir no Banco de Dados

			// Limpar os campos
			limpar();

		}

	}

	private void limpar() {
		tarefa = null;
		dpData.setValue(null);
		tfAutor.setText(null);
		tfTarefa.setText(null);
		taComentarios.setText(null);
		tfStatus.clear();
		dpData.requestFocus();

	}
}
