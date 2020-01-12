package br.edu.ifpi.poo.jogodavelha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {
	
	private String ip;
	private int porta;
	static ServerSocket servidor;
	static Socket socket;
	static DataOutputStream dos;
	static DataInputStream dis;
	
	public static boolean meuTurno = false;
	public static boolean souCliente = true;
	public static boolean requisicaoAceita = false;
	public static boolean falhaDeConexao = false;
	
	public Servidor2() {
		
	}
	
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}
	
	public int getPorta() {
		return this.porta;
	}

	
	public void aceitarCliente() {
		Socket socket = null;
		try {
			socket = servidor.accept();
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			Servidor2.requisicaoAceita = true;
			System.out.println("O CLIENTE FOI ACEITO.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public boolean conectar() {
		try {
			socket = new Socket(ip, porta);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			Servidor2.requisicaoAceita = true;
		} catch (IOException e) {
			System.out.println("Servidor2: " + ip + ":" + porta + " não encontrado. Criando um novo servidor.");
			return false;
		}
		System.out.println("Conexão realizada com sucesso.");
		return true;
	}

	public void iniciarServidor() {
		try {
			servidor = new ServerSocket(porta, 8, InetAddress.getByName(ip));
		} catch (Exception e) {
			System.out.println("Erro no servidor.");
		}
		Servidor2.meuTurno = true;
		Servidor2.souCliente = false;
	}
}
