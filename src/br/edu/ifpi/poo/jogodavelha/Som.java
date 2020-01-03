package br.edu.ifpi.poo.jogodavelha;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Som {
	private Clip audio;
	
	public Som() {
		
	}
	
	
	public void tocar(String path) {
	    try {
	    	AudioInputStream ai = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
	        audio = AudioSystem.getClip();
	        audio.open(ai);
	        audio.start();
	    } catch (FileNotFoundException e) {
	        System.out.println("Arquivo de �udio n�o encontrado.");
	    } catch (IOException e) {
	    	System.out.println("Erro inesperado no �udio.");
	    } catch (LineUnavailableException e) {
			System.out.println("Erro de �udio.");
		} catch (UnsupportedAudioFileException e) {
			System.out.println("Formato de �udio n�o suportado."); }

	}
}
