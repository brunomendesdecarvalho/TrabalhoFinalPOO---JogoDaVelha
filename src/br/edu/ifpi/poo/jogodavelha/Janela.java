package br.edu.ifpi.poo.jogodavelha;

import javax.swing.JFrame;

public class Janela {
	
	JFrame janela = new JFrame();
	
	public Janela(Graficos grafico, int largura, int altura) {
		janela.setTitle("Jogo da Velha");
		janela.setContentPane(grafico);
		janela.setSize(largura, altura);
		janela.setLocationRelativeTo(null);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setResizable(false);
		janela.setVisible(true);
		
	}
	
}
