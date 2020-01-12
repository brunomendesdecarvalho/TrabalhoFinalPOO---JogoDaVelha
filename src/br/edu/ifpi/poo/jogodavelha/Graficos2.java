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

	public class Graficos2 extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		private Font fonte = new Font("Arial", Font.BOLD, 32);
		private Font fonteMenor = new Font("Arial", Font.BOLD, 20);
		private Font fonteMaior = new Font("Arial", Font.BOLD, 40);

		private String stringEsperando = "Aguardando outro jogador.";
		private String stringSemConexao = "Impossível conectar-se ao oponente.";
		private String stringVitoria = "Você venceu!";
		private String stringDerrota = "Oponente venceu!";

		BufferedImage tabuleiro;
		BufferedImage opX;
		BufferedImage euX;

		public void carregarImagens() {
			try {
				tabuleiro = ImageIO.read(getClass().getResourceAsStream("/tabuleiro.png"));
				opX = ImageIO.read(getClass().getResourceAsStream("/opX.png"));
				euX = ImageIO.read(getClass().getResourceAsStream("/euX.png"));
			} catch (IOException e) {
				System.out.println("Falha na conexão.");
			}
		}

		public Graficos2() {
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
			if (Servidor2.requisicaoAceita) {
				if (Servidor2.meuTurno && !Servidor2.falhaDeConexao && !Showdown.vitoria && !Showdown.derrota) {
					int x = e.getX() / Showdown.tamEspaco;
					int y = e.getY() / Showdown.tamEspaco;
					y *= 3;
					int posicao = x + y;

					if (Showdown.espacos[posicao] == null) {
						if (!Servidor2.souCliente) { 
							Showdown.espacos[posicao] = "X2";
						}
						else {
							Showdown.espacos[posicao] = "X1";
						}
						Servidor2.meuTurno = false;
						repaint();
						Toolkit.getDefaultToolkit().sync();

						try {
							Servidor2.dos.writeInt(posicao);
							Servidor2.dos.flush();
						} catch (IOException e1) {
							Showdown.erros++;
							System.out.println("Falha na conexão.");
						}

						System.out.println("DADOS ENVIADOS.");
						Showdown.checarDerrota();
					}
				}
			}
		}

		private void renderizar(Graphics g) {
			g.drawImage(tabuleiro, 0, 0, null);
			if (Servidor2.falhaDeConexao) {
				g.setColor(Color.GREEN);
				g.setFont(fonteMenor);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				int stringWidth = g2.getFontMetrics().stringWidth(stringSemConexao);
				g.drawString(stringSemConexao, Showdown.LARGURA / 2 - stringWidth / 2, Showdown.LARGURA / 2);

			}

			if (Servidor2.requisicaoAceita) {
				for (int i = 0; i < Showdown.espacos.length; i++) {
					if (Showdown.espacos[i] != null) {
						if (Showdown.espacos[i].equals("X2")) {
							if (Servidor2.souCliente) {
								g.drawImage(opX, (i % 3) * Showdown.tamEspaco + 7 * (i % 3),
										(int) (i / 3) * Showdown.tamEspaco + 7 * (int) (i / 3), null);
							} else {
								g.drawImage(euX, (i % 3) * Showdown.tamEspaco + 7 * (i % 3),
										(int) (i / 3) * Showdown.tamEspaco + 7 * (int) (i / 3), null);
							}
						} else if (Showdown.espacos[i].equals("X1")) {
							if (Servidor2.souCliente) {
								g.drawImage(euX, (i % 3) * Showdown.tamEspaco + 7 * (i % 3),
										(int) (i / 3) * Showdown.tamEspaco + 7 * (int) (i / 3), null);
							} else {
								g.drawImage(opX, (i % 3) * Showdown.tamEspaco + 7 * (i % 3),
										(int) (i / 3) * Showdown.tamEspaco + 7 * (int) (i / 3), null);
							}
						}
					}
				}
				if (Showdown.vitoria || Showdown.derrota) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(4));
					g.setColor(Color.BLACK);
					g.drawLine(
							 Showdown.primEspaco % 3 * Showdown.tamEspaco + 7 * Showdown.primEspaco % 3
									+ Showdown.tamEspaco / 2,
							 (int) (Showdown.primEspaco / 3) * Showdown.tamEspaco
									+ 7 * (int) (Showdown.primEspaco / 3) + Showdown.tamEspaco / 2,
							 Showdown.segEspaco % 3 * Showdown.tamEspaco + 7 * Showdown.segEspaco % 3
									+ Showdown.tamEspaco / 2,
							 (int) (Showdown.segEspaco / 3) * Showdown.tamEspaco
									+ 7 * (int) (Showdown.segEspaco / 3) + Showdown.tamEspaco / 2);

					g.setColor(Color.GREEN);
					g.setFont(fonteMaior);
					if (Showdown.vitoria) {
						int larguraString = g2.getFontMetrics().stringWidth(stringVitoria);
						g.drawString(stringVitoria, Showdown.LARGURA / 2 - larguraString / 2, Showdown.LARGURA / 2);
					} else if (Showdown.derrota) {
						int larguraString = g2.getFontMetrics().stringWidth(stringDerrota);
						g.drawString(stringDerrota, Showdown.LARGURA / 2 - larguraString / 2, Showdown.LARGURA / 2);
					}
				}

			} else {
				g.setColor(Color.GREEN);
				g.setFont(fonte);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				int larguraString = g2.getFontMetrics().stringWidth(stringEsperando);
				g.drawString(stringEsperando, Showdown.LARGURA / 2 - larguraString / 2, Showdown.LARGURA / 2);
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
