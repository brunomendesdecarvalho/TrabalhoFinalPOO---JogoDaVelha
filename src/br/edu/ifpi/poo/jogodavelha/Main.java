package br.edu.ifpi.poo.jogodavelha;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException, IOException {
		String op = "";
		boolean valida = false;
		Scanner sc = new Scanner(System.in);
		
		while(!valida) {
	        
			System.out.println("------------ JOGO DA VELHA - BRUNO MENDES DE CARVALHO CASTELO BRANCO ------------");
			System.out.println("Escolha uma opção:\n"
					+ "1- Jogo clássico\n"
					+ "2- Showdown (apenas X, vence quem não fizer jogo, BETA)\n"
					+ "0- Sair");
			System.out.print(">>> ");
			op = sc.nextLine();
			
			switch (op){
			case "1":
				Classico jogo1 = new Classico();
				valida = true;
				sc.close();
				break;
			case "2":
				Showdown jogo2 = new Showdown();
				valida = true;
				sc.close();
				break;
			case "0":
				System.out.println("Encerrando...");
				sc.close();
				System.exit(0);
				break;
			default:
				System.out.println("Opção inválida.");
				break;
			}
		}
	}
}
