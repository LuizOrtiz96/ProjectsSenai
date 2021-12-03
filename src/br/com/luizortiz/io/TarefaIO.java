package br.com.luizortiz.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.luizortiz.model.StatusTarefa;
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
		long proxID = tarefa.getId() + 1;
		writer.write(proxID + "");
		writer.close();

	}

	public static List<Tarefa> read() throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		List<Tarefa> tarefas = new ArrayList<>();
		FileReader reader = new FileReader(arqTarefa);
		BufferedReader buff = new BufferedReader(reader);
		String linha;
		while ((linha = buff.readLine()) != null) {
			String[] vetor = linha.split(";");
			Tarefa t = new Tarefa();
			t.setId(Long.parseLong(vetor[0]));
			DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			t.setDataCriacao(LocalDate.parse(vetor[1], padraoData));
			t.setDataLimite(LocalDate.parse(vetor[2], padraoData));
			if (!vetor[3].isEmpty()) {
				t.setDataConcluida(LocalDate.parse(vetor[3], padraoData));
			}
			t.setDescricao(vetor[4]);
			t.setAutor(vetor[5]);
			t.setComentario(vetor[6]);
			int indStatus = Integer.parseInt(vetor[7]);
			t.setStatus(StatusTarefa.values()[indStatus]);
			tarefas.add(t);

		}
		reader.close();
		buff.close();
		return tarefas;

	}

	public static void saveTarefas(List<Tarefa> tarefas) throws IOException {
		File arqTarefas = new File(FILE_TAREFA);
		FileWriter writer = new FileWriter(arqTarefas);
		for (Tarefa t : tarefas) {
			writer.write(t.formattoSave());

		}
		writer.close();

	}

}
