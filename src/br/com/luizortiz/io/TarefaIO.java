package br.com.luizortiz.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import br.com.luizortiz.model.Tarefa;

public class TarefaIO {

	private static final String FOLDER = System.getProperty("user.home") + "/todolist";
	private static final String FILE_ID = FOLDER + "/id.csv";
	private static final String FILE_TAREFA = FOLDER + "/tarefas.csv";

	public static void createFiles() {
		try {
			File folder = new File(FOLDER);
			File fileID = new File(FILE_ID);
			File fileTarefa = new File(FILE_TAREFA);

			if (!folder.exists()) {
				folder.mkdir();
				fileTarefa.createNewFile();
				fileID.createNewFile();
				FileWriter writer = new FileWriter(fileID);
				writer.write("1");
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void insert(Tarefa tarefa) throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		File arqID = new File(FILE_ID);
		Scanner sc = new Scanner(arqID);
		tarefa.setId(sc.nextLong());
		sc.close();
		FileWriter writer = new FileWriter(arqTarefa, true);
		writer.append(tarefa.formattoSave());
		writer.close();
		// GRAVAR O NOVO ID NO ARQUIVO DE ID

		writer = new FileWriter(arqID);
		writer.write((tarefa.getId() + 1) + "");
		writer.close();

	}
}
