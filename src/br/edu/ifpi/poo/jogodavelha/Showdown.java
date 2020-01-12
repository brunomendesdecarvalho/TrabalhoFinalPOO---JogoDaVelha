package br.edu.ifpi.poo.jogodavelha;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

public class Showdown implements Runnable {

	private Scanner sc = new Scanner(System.in);
	final static int LARGURA = 510;
	final static int ALTURA = 530;
	private Thread thread;

	Graficos2 grafico = new Graficos2();
	Servidor2 servidor = new Servidor2();
	static Som som = new Som();

	static String[] espacos = new String[9];

	static boolean vitoria = false;
	static boolean derrota = false;
	static String ultJogada = "";

	static int tamEspaco = 160;
	static int erros = 0;
	static int primEspaco = -1;
	static int segEspaco = -1;

	private static int[][] derrotas = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // derrotas horizontais
													{ 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // derrotas verticais
													{ 0, 4, 8 }, { 2, 4, 6 } }; // derrotas diagonais

	/** Espaços do tabuleiro organizados e enumerados abaixo:
	 * <pre>
	 * 0, 1, 2 
	 * 3, 4, 5 
	 * 6, 7, 8
	 * </pre>
	 */

	@SuppressWarnings("unused")
	public Showdown() {
		System.out.println("Digite o IP: ");
		servidor.setIp(sc.nextLine());
		System.out.println("Digite a porta: ");
		servidor.setPorta(sc.nextInt());
		while (servidor.getPorta() < 1 || servidor.getPorta() > 65535) { // Intervalo de portas válidas.
			System.out.println("Porta inválida. Digite outra porta: ");
			servidor.setPorta(sc.nextInt());
		}

		grafico.carregarImagens();
		grafico.setPreferredSize(new Dimension(LARGURA, ALTURA));

		if (!servidor.conectar()) {
			servidor.iniciarServidor();
		}
		
		Janela janela = new Janela(grafico, LARGURA, ALTURA);
		thread = new Thread(this, "Showdown");
		thread.start();
	}

	public void run() {
		while (true) {
			marcar();
			grafico.repaint();

			if (!Servidor2.souCliente && !Servidor2.requisicaoAceita) {
				servidor.aceitarCliente();
			}

		}
	}

	private void marcar() {
		if (erros >= 5) {
			Servidor2.falhaDeConexao = true;
		}

		if (!Servidor2.meuTurno && !Servidor2.falhaDeConexao) {
			try {
				int espaco = Servidor2.dis.readInt();
				if (Servidor2.souCliente) {
					espacos[espaco] = "X2";
					ultJogada = "X2";
					som.tocar("/floop.wav");
				}
				else {
					espacos[espaco] = "X1";
					ultJogada = "X1";
					som.tocar("/floop.wav");
				}
				checarVitoria();
				Servidor2.meuTurno = true;
			} catch (IOException e) {
				System.out.println("Falha na conexão.");;
				erros++;
			}
		}
	}

	public static void checarDerrota() {
		for (int i = 0; i < derrotas.length; i++) {
			if (Servidor2.souCliente) {
				if (espacos[derrotas[i][0]] != null && espacos[derrotas[i][1]] != null && espacos[derrotas[i][2]] != null && ultJogada.equals("X1")) {
					primEspaco = derrotas[i][0];
					segEspaco = derrotas[i][2];
					derrota = true;
					som.tocar("/fade.wav");
				}
			} else {
				if (espacos[derrotas[i][0]] != null && espacos[derrotas[i][1]] != null && espacos[derrotas[i][2]] != null && ultJogada.equals("X2")) {
					primEspaco = derrotas[i][0];
					segEspaco = derrotas[i][2];
					derrota = true;
					som.tocar("/fade.wav");
				}
			}
		}
	}

	public void checarVitoria() {
		for (int i = 0; i < derrotas.length; i++) {
			if (Servidor2.souCliente) {
				if (espacos[derrotas[i][0]] != null && espacos[derrotas[i][1]] != null && espacos[derrotas[i][2]] != null && ultJogada.equals("X2")) {
					primEspaco = derrotas[i][0];
					segEspaco = derrotas[i][2];
					vitoria = true;
					som.tocar("/applause.wav");
				}
			} else {
				if (espacos[derrotas[i][0]] != null && espacos[derrotas[i][1]] != null && espacos[derrotas[i][2]] != null && ultJogada.equals("X1")) {
					primEspaco = derrotas[i][0];
					segEspaco = derrotas[i][2];
					vitoria = true;
					som.tocar("/applause.wav");
				}
			}
		}
	}

}
