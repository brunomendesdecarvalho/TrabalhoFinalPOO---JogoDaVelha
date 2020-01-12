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
			tabuleiro = ImageIO.read(getClass().getResourceAsStream("/tabuleiro.png"));
			opX = ImageIO.read(getClass().getResourceAsStream("/opX.png"));
			opO = ImageIO.read(getClass().getResourceAsStream("/opO.png"));
			euX = ImageIO.read(getClass().getResourceAsStream("/euX.png"));
			euO = ImageIO.read(getClass().getResourceAsStream("/euO.png"));
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
			if (Servidor.meuTurno && !Servidor.falhaDeConexao && !Classico.vitoria && !Classico.derrota) {
				int x = e.getX() / Classico.tamEspaco;
				int y = e.getY() / Classico.tamEspaco;
				y *= 3;
				int posicao = x + y;

				if (Classico.espacos[posicao] == null) {
					if (!Servidor.cliente) { 
						Classico.espacos[posicao] = "X";
					}
					else {
						Classico.espacos[posicao] = "O";
					}
					Servidor.meuTurno = false;
					repaint();
					Toolkit.getDefaultToolkit().sync();

					try {
						Servidor.dos.writeInt(posicao);
						Servidor.dos.flush();
					} catch (IOException e1) {
						Classico.erros++;
						System.out.println("Falha na conexão.");
					}

					System.out.println("DADOS ENVIADOS.");
					Classico.checarVitoria();
					Classico.checarEmpate();

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
			g.drawString(stringSemConexao, Classico.LARGURA / 2 - stringWidth / 2, Classico.LARGURA / 2);

		}

		if (Servidor.requisicaoAceita) {
			for (int i = 0; i < Classico.espacos.length; i++) {
				if (Classico.espacos[i] != null) {
					if (Classico.espacos[i].equals("X")) {
						if (Servidor.cliente) {
							g.drawImage(opX, (i % 3) * Classico.tamEspaco + 7 * (i % 3),
									(int) (i / 3) * Classico.tamEspaco + 7 * (int) (i / 3), null);
						} else {
							g.drawImage(euX, (i % 3) * Classico.tamEspaco + 7 * (i % 3),
									(int) (i / 3) * Classico.tamEspaco + 7 * (int) (i / 3), null);
						}
					} else if (Classico.espacos[i].equals("O")) {
						if (Servidor.cliente) {
							g.drawImage(euO, (i % 3) * Classico.tamEspaco + 7 * (i % 3),
									(int) (i / 3) * Classico.tamEspaco + 7 * (int) (i / 3), null);
						} else {
							g.drawImage(opO, (i % 3) * Classico.tamEspaco + 7 * (i % 3),
									(int) (i / 3) * Classico.tamEspaco + 7 * (int) (i / 3), null);
						}
					}
				}
			}
			if (Classico.vitoria || Classico.derrota) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(4));
				g.setColor(Color.BLACK);
				g.drawLine(
						 Classico.primEspaco % 3 * Classico.tamEspaco + 7 * Classico.primEspaco % 3
								+ Classico.tamEspaco / 2,
						 (int) (Classico.primEspaco / 3) * Classico.tamEspaco
								+ 7 * (int) (Classico.primEspaco / 3) + Classico.tamEspaco / 2,
						 Classico.segEspaco % 3 * Classico.tamEspaco + 7 * Classico.segEspaco % 3
								+ Classico.tamEspaco / 2,
						 (int) (Classico.segEspaco / 3) * Classico.tamEspaco
								+ 7 * (int) (Classico.segEspaco / 3) + Classico.tamEspaco / 2);

				g.setColor(Color.GREEN);
				g.setFont(fonteMaior);
				if (Classico.vitoria) {
					int larguraString = g2.getFontMetrics().stringWidth(stringVitoria);
					g.drawString(stringVitoria, Classico.LARGURA / 2 - larguraString / 2, Classico.LARGURA / 2);
				} else if (Classico.derrota) {
					int larguraString = g2.getFontMetrics().stringWidth(stringDerrota);
					g.drawString(stringDerrota, Classico.LARGURA / 2 - larguraString / 2, Classico.LARGURA / 2);
				}
			}
			if (Classico.empate) {
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(Color.BLACK);
				g.setFont(fonteMaior);
				int larguraString = g2.getFontMetrics().stringWidth(stringEmpate);
				g.drawString(stringEmpate, Classico.LARGURA / 2 - larguraString / 2, Classico.LARGURA / 2);
			}
		} else {
			g.setColor(Color.GREEN);
			g.setFont(fonte);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int larguraString = g2.getFontMetrics().stringWidth(stringEsperando);
			g.drawString(stringEsperando, Classico.LARGURA / 2 - larguraString / 2, Classico.LARGURA / 2);
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



