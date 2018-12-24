/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package edu.uc3m.principal;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;


import Sprites.*;
import edu.uc3m.game.GameBoardGUI;
import java.awt.Color;

public class MiniGalaga {
	// lobby que espera a que el jugador introduzca el nombre y lo usa
	// posteriormente para ponerlo en la esquina posterior derecha
	private static String lobby(GameBoardGUI gui) {
		JOptionPane nom_jugador = new JOptionPane();
		gui.add(nom_jugador);
		return JOptionPane.showInputDialog(null, "Introduce el nombre del jugador para jugar/volver a jugar");
	}

	// boolean para saber si volver al lobby
	public static boolean repetir = false;
	public static String newName;

	// main
	public static void main(String[] args) throws InterruptedException, IOException {

		// definicion de GameBoard
		GameBoardGUI gui = new GameBoardGUI(Conf.WIDTH, Conf.HEIGHT);
		gui.setVisible(true);
		gui.gb_setGridColor(0, 0, 0); // 1uitando el grid por completo
		gui.setBackground(new Color(0f, 0f, 0f, 1f));

		// imagen del lobby
		gui.gb_addSprite(790, "lobby.png", true);
		gui.gb_setSpriteImage(790, "lobby.png", 510, 660);

		gui.gb_setPortraitPlayer("portrait.png");
		// musica
		musica_principal();
		// Fondo de bienvenida
		gui.gb_setSpriteVisible(790, true);
		// mensaje de bienvenida
		gui.gb_showMessageDialog(
				"Bienvenido a MiniGalaga, juego desarrollado por Cristina Pinia y Florinel Olteanu, \n\n Instrucciones:\n Mover:Flechas izquierda y derecha\n Dejar de mover:Flecha abajo\n Disparar:Espacio\n\n PowerUps:\n Azul: Invulnerable durante 10 segundos\n Verde: Si ha perdido alguna vida, se le sumara una\n Amarillo: 5000 puntos extra!\n Rojo: Le recomendamos no destruirlo :D\n\n Intente no morir y esperamos que se lo pase bien!");
		
		// bucle while true para que se juegue hasta que el jugador de al boton de salir
		while (true) {

			// imagen del lobby
			gui.gb_addSprite(790, "lobby.png", true);
			gui.gb_setSpriteImage(790, "lobby.png", 510, 660);
			gui.gb_setSpriteVisible(790, true);
			// imagen del background
			gui.gb_addSprite(789, "background.jpeg", true);
			gui.gb_setSpriteImage(789, "background.jpeg", 510, 660);
			gui.gb_setSpriteVisible(789, false);
			gui.gb_moveSpriteCoord(789, 85, 110);
			Player jugador;

			repetir = false;
			if (newName != null) {
				jugador = new Player(1, 85, 200, "player.png", 3, newName, gui, true, 0);
			} else {
				jugador = new Player(1, 85, 200, "player.png", 3, lobby(gui), gui, true, 0);
			}

			gui.gb_setSpriteVisible(790, false);
			gui.gb_setSpriteVisible(789, true);
			gui.gb_clearConsole();
			// bucle for para tenerlo mas compacto y ademas asi poder volver al lobby
			for (int i = 1; i <= 10 && !repetir; i++) {
				Level level = new Level(i, jugador, gui);
				level.ejecutarNivel();
			}

		}
	}

	// usa la libreria sound para dar musica al juego hasta que se acabe
	private static void musica_principal() {
		try {
			// agrega el archivo de sonido al programa
			File soundFile = new File(MiniGalaga.class.getResource("/sound/main_theme.wav").getFile());
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

			Clip clip = AudioSystem.getClip();
			// abre el archivo y lo ejecuta
			clip.open(audioIn);
			clip.start();
			
			// en caso de no tener el archivo de audio en el directorio adecuado
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

}
