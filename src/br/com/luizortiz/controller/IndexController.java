package br.com.luizortiz.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.luizortiz.io.TarefaIO;
import br.com.luizortiz.model.StatusTarefa;
import br.com.luizortiz.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndexController implements Initializable, ChangeListener<Tarefa> {

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

	@FXML
	private TableColumn<Tarefa, LocalDate> tcData;

	@FXML
	private TableColumn<Tarefa, String> tcTarefa;

	@FXML
	private TableView<Tarefa> tvTarefa;

	private List<Tarefa> tarefas;
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

			// TODO Inserir no Banco de Dados

			try {
				TarefaIO.insert(tarefa);
				// Limpar os campos
				limpar();
				// Carregar as Tarefas
				carregarTarefas();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao gravar: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

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
		btAdiar.setDisable(true);
		btConcluir.setDisable(true);
		btApagar.setDisable(true);
		tvTarefa.getSelectionModel().clearSelection();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Definir os parâmetros para as colunas do Tableview
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tcData.setCellFactory(call -> {
			return new TableCell<Tarefa, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
					if (!empty) {
						setText(item.format(fmt));
					} else {
						setText(" ");
					}
				}
			};

		});
		// Evento de seleção de item na tabela
		tvTarefa.getSelectionModel().selectedItemProperty().addListener(this);

		carregarTarefas();

	}

	public void carregarTarefas() {
		try {
			tarefas = TarefaIO.read();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar as tarefas: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void changed(ObservableValue<? extends Tarefa> observable, Tarefa oldValue, Tarefa newValue) {

		// Passo a referência para a variável global
		tarefa = newValue;
		if (tarefa != null) {
			tfTarefa.setText(tarefa.getDescricao());
			tfAutor.setText(tarefa.getAutor());
			taComentarios.setText(tarefa.getComentario());
			tfStatus.setText(tarefa.getStatus() + "");
			dpData.setValue(tarefa.getDataLimite());
			btAdiar.setDisable(false);
			btConcluir.setDisable(false);
			btApagar.setDisable(false);
			dpData.setDisable(true);
			dpData.setOpacity(1);

		}

	}
}
