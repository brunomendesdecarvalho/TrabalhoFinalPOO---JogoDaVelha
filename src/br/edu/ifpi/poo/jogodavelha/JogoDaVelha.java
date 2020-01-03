package br.edu.ifpi.poo.jogodavelha;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

public class JogoDaVelha implements Runnable {

	private Scanner sc = new Scanner(System.in);
	final static int LARGURA = 506;
	final static int ALTURA = 527;
	private Thread thread;

	Graficos grafico = new Graficos();
	Servidor servidor = new Servidor();
	static Som som = new Som();

	static String[] espacos = new String[9];

	static boolean vitoria = false;
	static boolean derrota = false;
	static boolean empate = false;

	static int tamEspaco = 160;
	static int erros = 0;
	static int primEspaco = -1;
	static int segEspaco = -1;

	private static int[][] vitorias = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // vit�rias horizontais
													{ 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // vit�rias verticais
													{ 0, 4, 8 }, { 2, 4, 6 } }; // vit�rias diagonais

	/** Espa�os do tabuleiro organizados e enumerados abaixo:
	 * <pre>
	 * 0, 1, 2 
	 * 3, 4, 5 
	 * 6, 7, 8
	 * </pre>
	 */

	@SuppressWarnings("unused")
	public JogoDaVelha() {
		System.out.println("Digite o IP: ");
		servidor.setIp(sc.nextLine());
		System.out.println("Digite a porta: ");
		servidor.setPorta(sc.nextInt());
		while (servidor.getPorta() < 1 || servidor.getPorta() > 65535) { // Intervalo de portas v�lidas.
			System.out.println("Porta inv�lida. Digite outra porta: ");
			servidor.setPorta(sc.nextInt());
		}

		grafico.carregarImagens();
		grafico.setPreferredSize(new Dimension(LARGURA, ALTURA));

		if (!servidor.conectar()) {
			servidor.iniciarServidor();
		}
		
		Janela janela = new Janela(grafico, LARGURA, ALTURA);
		thread = new Thread(this, "JogoDaVelha");
		thread.start();
	}

	public void run() {
		while (true) {
			marcar();
			grafico.repaint();

			if (!Servidor.circulo && !Servidor.requisicaoAceita) {
				servidor.aceitarCliente();
			}

		}
	}

	private void marcar() {
		if (erros >= 5) {
			Servidor.falhaDeConexao = true;
		}

		if (!Servidor.meuTurno && !Servidor.falhaDeConexao) {
			try {
				int espaco = Servidor.dis.readInt();
				if (Servidor.circulo) {
					espacos[espaco] = "X";
					som.tocar("/floop.wav");
				}
				else {
					espacos[espaco] = "O";
					som.tocar("/floop.wav");
				}
				checarDerrota();
				checarEmpate();
				Servidor.meuTurno = true;
			} catch (IOException e) {
				System.out.println("Falha na conex�o.");;
				erros++;
			}
		}
	}

	public static void checarVitoria() {
		for (int i = 0; i < vitorias.length; i++) {
			if (Servidor.circulo) {
				if (espacos[vitorias[i][0]] == "O" && espacos[vitorias[i][1]] == "O" && espacos[vitorias[i][2]] == "O") {
					primEspaco = vitorias[i][0];
					segEspaco = vitorias[i][2];
					vitoria = true;
					som.tocar("/applause.wav");
				}
			} else {
				if (espacos[vitorias[i][0]] == "X" && espacos[vitorias[i][1]] == "X" && espacos[vitorias[i][2]] == "X") {
					primEspaco = vitorias[i][0];
					segEspaco = vitorias[i][2];
					vitoria = true;
					som.tocar("/applause.wav");
				}
			}
		}
	}

	public void checarDerrota() {
		for (int i = 0; i < vitorias.length; i++) {
			if (Servidor.circulo) {
				if (espacos[vitorias[i][0]] == "X" && espacos[vitorias[i][1]] == "X" && espacos[vitorias[i][2]] == "X") {
					primEspaco = vitorias[i][0];
					segEspaco = vitorias[i][2];
					derrota = true;
					som.tocar("/fade.wav");
				}
			} else {
				if (espacos[vitorias[i][0]] == "O" && espacos[vitorias[i][1]] == "O" && espacos[vitorias[i][2]] == "O") {
					primEspaco = vitorias[i][0];
					segEspaco = vitorias[i][2];
					derrota = true;
					som.tocar("/fade.wav");
				}
			}
		}
	}

	public static void checarEmpate() {
		for (int i = 0; i < espacos.length; i++) {
			if (espacos[i] == null) {
				return;
			}
		}
		empate = true;
		som.tocar("/gasp.wav");
	}

}
