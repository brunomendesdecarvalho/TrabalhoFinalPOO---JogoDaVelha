package br.edu.ifpi.poo.jogodavelha;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Graficos extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	private Font fonte = new Font("Arial", Font.BOLD, 32);
	private Font fonteMenor = new Font("Arial", Font.BOLD, 20);
	private Font fonteMaior = new Font("Arial", Font.BOLD, 40);

	private String stringEsperando = "Aguardando outro jogador.";
	private String stringSemConexao = "Impossível conectar-se ao oponente.";
	private String stringVitoria = "Você venceu!";
	private String stringDerrota = "Oponente venceu!";
	private String stringEmpate = "Empate!";

	BufferedImage tabuleiro;
	BufferedImage opX;
	BufferedImage opO;
	BufferedImage euX;
	BufferedImage euO;

	public void carregarImagens() {
		try {
			tabuleiro = ImageIO.read(getClass().getResourceAsStream("/board.png"));
			opX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
			opO = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
			euX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
			euO = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
		} catch (IOException e) {
			System.out.println("Falha na conexão.");
		}
	}

	public Graficos() {
		setFocusable(true);
		requestFocus();
		setBackground(Color.WHITE);
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderizar(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (Servidor.requisicaoAceita) {
			if (Servidor.meuTurno && !Servidor.falhaDeConexao && !JogoDaVelha.vitoria && !JogoDaVelha.derrota) {
				int x = e.getX() / JogoDaVelha.tamEspaco;
				int y = e.getY() / JogoDaVelha.tamEspaco;
				y *= 3;
				int posicao = x + y;

				if (JogoDaVelha.espacos[posicao] == null) {
					if (!Servidor.circulo) { 
						JogoDaVelha.espacos[posicao] = "X";
					}
					else {
						JogoDaVelha.espacos[posicao] = "O";
					}
					Servidor.meuTurno = false;
					repaint();
					Toolkit.getDefaultToolkit().sync();

					try {
						Servidor.dos.writeInt(posicao);
						Servidor.dos.flush();
					} catch (IOException e1) {
						JogoDaVelha.erros++;
						System.out.println("Falha na conexão.");
					}

					System.out.println("DADOS ENVIADOS.");
					JogoDaVelha.checarVitoria();
					JogoDaVelha.checarEmpate();

				}
			}
		}
	}

	private void renderizar(Graphics g) {
		g.drawImage(tabuleiro, 0, 0, null);
		if (Servidor.falhaDeConexao) {
			g.setColor(Color.GREEN);
			g.setFont(fonteMenor);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int stringWidth = g2.getFontMetrics().stringWidth(stringSemConexao);
			g.drawString(stringSemConexao, JogoDaVelha.LARGURA / 2 - stringWidth / 2, JogoDaVelha.LARGURA / 2);

		}

		if (Servidor.requisicaoAceita) {
			for (int i = 0; i < JogoDaVelha.espacos.length; i++) {
				if (JogoDaVelha.espacos[i] != null) {
					if (JogoDaVelha.espacos[i].equals("X")) {
						if (Servidor.circulo) {
							g.drawImage(opX, (i % 3) * JogoDaVelha.tamEspaco + 10 * (i % 3),
									(int) (i / 3) * JogoDaVelha.tamEspaco + 10 * (int) (i / 3), null);
						} else {
							g.drawImage(euX, (i % 3) * JogoDaVelha.tamEspaco + 10 * (i % 3),
									(int) (i / 3) * JogoDaVelha.tamEspaco + 10 * (int) (i / 3), null);
						}
					} else if (JogoDaVelha.espacos[i].equals("O")) {
						if (Servidor.circulo) {
							g.drawImage(euO, (i % 3) * JogoDaVelha.tamEspaco + 10 * (i % 3),
									(int) (i / 3) * JogoDaVelha.tamEspaco + 10 * (int) (i / 3), null);
						} else {
							g.drawImage(opO, (i % 3) * JogoDaVelha.tamEspaco + 10 * (i % 3),
									(int) (i / 3) * JogoDaVelha.tamEspaco + 10 * (int) (i / 3), null);
						}
					}
				}
			}
			if (JogoDaVelha.vitoria || JogoDaVelha.derrota) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(10));
				g.setColor(Color.BLACK);
				g.drawLine(
						10 + JogoDaVelha.primEspaco % 3 * JogoDaVelha.tamEspaco + 10 * JogoDaVelha.primEspaco % 3
								+ JogoDaVelha.tamEspaco / 2,
						10 + (int) (JogoDaVelha.primEspaco / 3) * JogoDaVelha.tamEspaco
								+ 10 * (int) (JogoDaVelha.primEspaco / 3) + JogoDaVelha.tamEspaco / 2,
						10 + JogoDaVelha.segEspaco % 3 * JogoDaVelha.tamEspaco + 10 * JogoDaVelha.segEspaco % 3
								+ JogoDaVelha.tamEspaco / 2,
						10 + (int) (JogoDaVelha.segEspaco / 3) * JogoDaVelha.tamEspaco
								+ 10 * (int) (JogoDaVelha.segEspaco / 3) + JogoDaVelha.tamEspaco / 2);

				g.setColor(Color.GREEN);
				g.setFont(fonteMaior);
				if (JogoDaVelha.vitoria) {
					int larguraString = g2.getFontMetrics().stringWidth(stringVitoria);
					g.drawString(stringVitoria, JogoDaVelha.LARGURA / 2 - larguraString / 2, JogoDaVelha.LARGURA / 2);
				} else if (JogoDaVelha.derrota) {
					int larguraString = g2.getFontMetrics().stringWidth(stringDerrota);
					g.drawString(stringDerrota, JogoDaVelha.LARGURA / 2 - larguraString / 2, JogoDaVelha.LARGURA / 2);
				}
			}
			if (JogoDaVelha.empate) {
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(Color.BLACK);
				g.setFont(fonteMaior);
				int larguraString = g2.getFontMetrics().stringWidth(stringEmpate);
				g.drawString(stringEmpate, JogoDaVelha.LARGURA / 2 - larguraString / 2, JogoDaVelha.LARGURA / 2);
			}
		} else {
			g.setColor(Color.GREEN);
			g.setFont(fonte);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int larguraString = g2.getFontMetrics().stringWidth(stringEsperando);
			g.drawString(stringEsperando, JogoDaVelha.LARGURA / 2 - larguraString / 2, JogoDaVelha.LARGURA / 2);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}



