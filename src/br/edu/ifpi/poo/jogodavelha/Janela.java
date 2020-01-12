package br.edu.ifpi.poo.jogodavelha;

import javax.swing.JFrame;

public class Janela {
	
	JFrame janela = new JFrame();
	
	public Janela(Graficos grafico, int largura, int altura) {
		janela.setTitle("Jogo da Velha - Cl�ssico");
		janela.setContentPane(grafico);
		janela.setSize(largura, altura);
		janela.setLocationRelativeTo(null);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setResizable(false);
		janela.setVisible(true);
		
	}
	public Janela(Graficos2 grafico, int largura, int altura) {
		janela.setTitle("Jogo da Velha - Showdown");
		janela.setContentPane(grafico);
		janela.setSize(largura, altura);
		janela.setLocationRelativeTo(null);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setResizable(false);
		janela.setVisible(true);
		
	}
	
}
