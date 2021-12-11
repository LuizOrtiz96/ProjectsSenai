package br.com.luizortiz.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

	@FXML
	private Button btOK;

	@FXML
	public void btOKClick() {
		Stage stage = (Stage) btOK.getScene().getWindow();
		stage.close();

	}

	@FXML
	private Button btSaibaMais;

	@FXML
	private void btSaibaMaisClick() throws Exception {

		try {
			URI link = new URI("https://www.any.do/pt-br/");
			Desktop.getDesktop().browse(link);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro 404 Not Found", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}
}
