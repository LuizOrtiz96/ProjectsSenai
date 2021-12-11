package br.com.luizortiz.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.luizortiz.io.TarefaIO;
import br.com.luizortiz.model.StatusTarefa;
import br.com.luizortiz.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IndexController implements Initializable, ChangeListener<Tarefa> {

	@FXML
	private TextField tfTarefa;

	@FXML
	private TextField tfCod;

	@FXML
	private TextArea taComentarios;

	@FXML
	private TextField tfAutor;

	@FXML
	private TextField tfStatus;

	@FXML
	private Label lbStatus;

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
		if (tarefa != null) {
			int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir a Tarefa" + tarefa.getId() + "?",
					"Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resposta == 0) {
				tarefas.remove(tarefa);

				try {
					TarefaIO.saveTarefas(tarefas);
					JOptionPane.showMessageDialog(null, "Sua tarefa foi excluída com sucesso ! ", "Informe",
							JOptionPane.INFORMATION_MESSAGE);
					carregarTarefas();
					limpar();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Erro ao excluir a tarefa: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}

	}

	@FXML
	void btExtendClick(ActionEvent event) {
		if (tarefa != null) {
			int dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Quantos dias você deseja Adiar ?",
					"Informe quantos dias", JOptionPane.QUESTION_MESSAGE));
			LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
			tarefa.setDataLimite(novaData);
			tarefa.setStatus(StatusTarefa.ADIADA);
			DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			try {
				TarefaIO.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null, "Sua tarefa foi adiada com sucesso",
						" A nova data será " + tarefa.getDataLimite().format(padraoData),
						JOptionPane.INFORMATION_MESSAGE);

				carregarTarefas();
				limpar();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar as tarefas: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

	}

	@FXML
	void btFinishedClick(ActionEvent event) {
		if (tarefa != null) {
			tarefa.setDataConcluida(LocalDate.now());
			tarefa.setStatus(StatusTarefa.CONCLUIDA);
			DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			try {
				TarefaIO.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null, "Sua tarefa foi concluída com sucesso", null,
						JOptionPane.INFORMATION_MESSAGE);

				carregarTarefas();
				limpar();

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao concluir a tarefa: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

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
			// verifica se a tarefa é nula pra alterar uma tarefa
			if (tarefa == null) {
				// Instanciando a Tarefa
				tarefa = new Tarefa();
				tarefa.setDataCriacao(LocalDate.now());
				tarefa.setStatus(StatusTarefa.ABERTA);
			}
			// Popular a tarefa
			tarefa.setDataLimite(dpData.getValue());
			tarefa.setAutor(tfAutor.getText());
			tarefa.setDescricao(tfTarefa.getText());
			tarefa.setComentario(taComentarios.getText());

			// TODO Inserir no Banco de Dados

			try {
				if (tarefa.getId() == 0) {
					TarefaIO.insert(tarefa);
				} else {
					TarefaIO.saveTarefas(tarefas);
					JOptionPane.showMessageDialog(null, "Sua tarefa foi alterada com sucesso ! ", "Informe",
							JOptionPane.INFORMATION_MESSAGE);
				}

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
		tfCod.clear();
		dpData.setDisable(false);
		dpData.requestFocus();
		tfAutor.setDisable(false);
		btAdiar.setDisable(true);
		btConcluir.setDisable(true);
		btApagar.setDisable(true);
		tvTarefa.getSelectionModel().clearSelection();
		tfTarefa.setDisable(false);
		taComentarios.setDisable(false);
		btSalvar.setDisable(false);
		tfCod.setText(null);

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

		tvTarefa.setRowFactory(call -> new TableRow<Tarefa>() {
			protected void updateItem(Tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle(" ");
				} else if (item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color:green");
				} else if (item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color:black");
				} else if (item.getStatus() == StatusTarefa.ADIADA) {
					setStyle("-fx-fill:red");
				} else {
					setStyle("-fx-text-fill:black;");
				}
			};
		});
		// Evento de seleção de item na tabela
		tvTarefa.getSelectionModel().selectedItemProperty().addListener(this);

		carregarTarefas();

		try {
			tfCod.setText(TarefaIO.showNextid() + "");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			tfCod.setText(tarefa.getId() + "");
			dpData.setValue(tarefa.getDataLimite());
			dpData.setDisable(true);
			dpData.setOpacity(1);

			if (tarefa.getStatus() == StatusTarefa.ADIADA) {
				btSalvar.setDisable(false);
				btAdiar.setDisable(true);
				btConcluir.setDisable(true);
				btApagar.setDisable(false);
				lbStatus.setText("Data prevista : ");

			} else if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
				btConcluir.setDisable(true);
				btAdiar.setDisable(true);
				btLimpar.setDisable(false);
				btSalvar.setDisable(true);
				btApagar.setDisable(false);
				lbStatus.setText("Data de realização: ");
				dpData.setValue(tarefa.getDataConcluida());
				return;

			} else {
				btSalvar.setDisable(false);
				btConcluir.setDisable(false);
				btAdiar.setDisable(false);
			}

		}

	}

	@FXML
	void miExport(ActionEvent event) {
		FileFilter filter = new FileNameExtensionFilter("Arquivos HTML", "html", "htm");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File arqSelecionado = chooser.getSelectedFile();
			if (!arqSelecionado.getAbsolutePath().endsWith(".html")) {
				arqSelecionado = new File(arqSelecionado + ".html");
			}
			try {
				TarefaIO.exportHtml(tarefas, arqSelecionado);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao exportar as tarefas:" + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	@FXML
	void miSair(ActionEvent event) {

		int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair ?", "Confirmar saída",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resposta == 0) {
			System.exit(0);
		}
	}

	@FXML
	void miSobre(ActionEvent event) {
		AnchorPane root = null;
		try {
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/br/com/luizortiz/view/About.fxml"));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets()
					.add(getClass().getResource("/br/com/luizortiz/view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
