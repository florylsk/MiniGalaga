/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package edu.uc3m.principal;

import Sprites.*;
import edu.uc3m.game.GameBoardGUI;

public class Swarm {

	public static int dirX = 1;
	private Enemy[][] enemies = new Enemy[4][];
	private GameBoardGUI gui;
	private int levelNumber; // necesario para crear los diferentes id
	private int centerX = 85, centerY = 10; // Coordenadas del centro del enjambre
	private long lastActionTime; // Para el control de la velocidad
	private boolean isEntradaRealizada;

	public Swarm(GameBoardGUI gui, int levelNumber) {
		this.levelNumber = levelNumber;
		this.gui = gui;
		generarEnjambreNivel();
		this.lastActionTime = System.currentTimeMillis();
	}

	public boolean isEntradaRealizada() {
		return isEntradaRealizada;
	}

	public Enemy[][] getEnemies() {
		return enemies;
	}

	/*
	 * Los siguientes metodos son para crear los arrays de enemigo de los diferentes
	 * enjambres En el array de enemigos la fila superior esta ocupada por
	 * commanders, la siguiente por goei y las dos siguientes por zakos Los id de
	 * los enemigos se crearan con un numero ABCD, siendo A el numero del nivel y B
	 * el tipo de enemigo
	 */
	private void crearEnemigos(int cantCom, int cantGoei, int cantZako1, int cantZako2) {
		crearFilaCommanders(cantCom);
		crearFilaGoeis(cantGoei);
		crearFilasZakos(cantZako1, cantZako2);
	}

	private void crearFilaCommanders(int cantidad) {
		enemies[0] = new Enemy[cantidad];
		for (int i = 0; i < cantidad; i++) {
			enemies[0][i] = new Commander(levelNumber * 1000 + i + 100, 0, (Conf.HEIGHT + 1) * 10, "enemy100.png", gui,
					true);
			enemies[0][i].setYenjInicial(centerY);
		}
	}

	private void crearFilaGoeis(int cantidad) {
		enemies[1] = new Enemy[cantidad];
		for (int i = 0; i < cantidad; i++) {
			enemies[1][i] = new Goei(levelNumber * 1000 + i + 200, 0, (Conf.HEIGHT + 1) * 10, "enemy200.png", gui,
					true);
			enemies[1][i].setYenjInicial(centerY + 10);
		}
	}

	private void crearFilasZakos(int cantidad1, int cantidad2) {
		enemies[2] = new Enemy[cantidad1];
		for (int i = 0; i < cantidad1; i++) {
			enemies[2][i] = new Zako(levelNumber * 1000 + i + 300, 0, (Conf.HEIGHT + 1) * 10, "enemy300.png", gui,
					true);
			enemies[2][i].setYenjInicial(centerY + 20);
		}
		enemies[3] = new Enemy[cantidad2];
		for (int i = 0; i < cantidad2; i++) {
			enemies[3][i] = new Zako(levelNumber * 1000 + i + 350, 0, (Conf.HEIGHT + 1) * 10, "enemy300.png", gui,
					true);
			enemies[3][i].setYenjInicial(centerY + 30);
		}

	}

	// Metodo para que se mueva el enjambre en conjunto

	public void move() {

		if (System.currentTimeMillis() - this.lastActionTime > 40) { // Controla la VELOCIDAD de movimiento del
																		// enjambre

			if (this.centerX <= 60) {
				dirX = +1; // Cambio en el sentido del movimiento
				centerY = centerY + 1; // El enjambre baja

			} else if (this.centerX >= 110) {
				dirX = -1; // Cambia el sentido del movimiento
				centerY = centerY + 1; // El enjambre baja
			}

			this.centerX = this.centerX + dirX;

			// Como es una matriz irregular el movimiento lo hacemos ejecutando cuatro veces
			// el moveFila, uno por fila

			this.moveFila(enemies[0]);
			this.moveFila(enemies[1]);
			this.moveFila(enemies[2]);
			this.moveFila(enemies[3]);

			this.lastActionTime = System.currentTimeMillis();

		}
	}

	public void moveFila(Enemy[] enemigo) {
		int variable;
		if (enemigo == enemies[0]) { // Si se le da la primera fila
			variable = 0;
		} else if (enemigo == enemies[1]) { // Si se le da la segunda fila
			variable = 10;
		} else if (enemigo == enemies[2]) {
			variable = 20;
		} else {
			variable = 30;
		}

		for (int i = 0; i < enemigo.length; i++) {
			if (enemigo[i].getIsDentroEnjambre()) {
				enemigo[i].moveXY(enemigo[i].getX() + dirX, centerY + variable);
				enemigo[i].aletear(centerX);

			} else if (enemigo[i].isAlive()) { // Si el enemigo esta vivo pero no en el enjambre, se mueve su hueco
												// vacio junto al enjambre
				enemigo[i].setXYhueco(enemigo[i].getXYhueco()[0] + dirX, centerY + variable);
			}

		}
	}

	public boolean isDestruido() { // Metodo que comprueba si el enjambre entero ha sido destruido, es decir, si
									// cada enemigo tiene vida 0
		boolean isDestruido = true;

		for (int i = 0; i < enemies[0].length; i++) {
			if (enemies[0][i].getHealth() != 0) {
				isDestruido = false;
			}
		}
		for (int i = 0; i < enemies[1].length; i++) {
			if (enemies[1][i].getHealth() != 0) {
				isDestruido = false;
			}
		}
		for (int i = 0; i < enemies[2].length; i++) {
			if (enemies[2][i].getHealth() != 0) {
				isDestruido = false;
			}
		}
		for (int i = 0; i < enemies[3].length; i++) {
			if (enemies[3][i].getHealth() != 0) {
				isDestruido = false;
			}
		}

		return isDestruido;

	}

	public void comprobarEntradaCompletada() {
		this.isEntradaRealizada = true;
		for (int fila = 0; fila < this.enemies.length; fila++) {
			for (int i = 0; i < this.enemies[fila].length && this.isEntradaRealizada == true; i++) {
				if (this.enemies[fila][i].isAlive()) {
					if (this.enemies[fila][i].getX() != this.enemies[fila][i].getXenjInicial()
							|| this.enemies[fila][i].getY() != this.enemies[fila][i].getYenjInicial()) {
						this.isEntradaRealizada = false;
					}
				}
			}
		}
	}

	// El siguiente metodo crea y coloca en sus respectivas posiciones a los
	// enemigos segun el nivel
	private void generarEnjambreNivel() {
		switch (this.levelNumber) {
		case 1:

			// Crea los enemigos del enjambre, los parametro son cantidad de:
			// Commander, Goei, Zako1, Zako2
			crearEnemigos(1, 6, 7, 6);

			// Colocamos a los Commanders, primera fila
			enemies[0][0].setXenjInicial(centerX);

			// Colocamos a los Goeis, segunda fila
			for (int i = 0; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(i * 10 + 50 + i / 2 * 10);
			}

			// Colocamos a los Zakos de la tercera fila
			for (int i = 0; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(i * 10 + 50 + i / 4 * 10);
				if (i == 0 || i == 6)
					enemies[2][i].setXenjInicial(i * 15 + 40);
				if (i == 3)
					enemies[2][i].setXenjInicial(85);
			}

			// Colocamos a los Zakos de la cuarta fila
			enemies[3][0].setXenjInicial(50);
			enemies[3][enemies[3].length - 1].setXenjInicial(120);
			for (int i = 1; i < enemies[3].length - 1; i++) {
				enemies[3][i].setXenjInicial(60 + i * 10);
			}

			break;

		case 2:

			// creando los enemigos del enjambre
			crearEnemigos(2, 8, 6, 6);

			// Colocando los commanders
			enemies[0][0].setXenjInicial((centerX) - 20);
			enemies[0][1].setXenjInicial((centerX) + 20);
			// colocando los goei
			for (int i = 0; i < enemies[1].length; i++) {
				int var = ((enemies[1].length) / 2) - 1;
				if (i <= var) {
					enemies[1][i].setXenjInicial(centerX - (10 * i + 20));
				} else if (i > var) {
					enemies[1][i].setXenjInicial(centerX + (10 * i - 20));
				}

			}
			// colocando los zakos de la tercera fila
			enemies[2][0].setXenjInicial(60);
			for (int i = 0; i < enemies[2].length; i++) {

				enemies[2][i].setXenjInicial(enemies[2][0].getXenjInicial() + (10 * i));
			}
			// colocando los zajos de la 4a fila
			enemies[3][0].setXenjInicial(35);
			for (int i = 0; i < enemies[3].length; i++) {

				enemies[3][i].setXenjInicial(enemies[3][0].getXenjInicial() + (20 * i));
			}

			break;

		case 3:
			crearEnemigos(3, 5, 6, 6);

			enemies[0][0].setXenjInicial(60);
			enemies[0][1].setXenjInicial(85);
			enemies[0][2].setXenjInicial(110);

			for (int i = 0; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(40 + 20 * i + i / 3 * 10);
				if (i == 2)
					enemies[1][i].setXenjInicial(85);
			}

			for (int i = 0; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(60 + 10 * i);
			}
			for (int i = 0; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(40 + 10 * i + (i / 2) * 20);
			}

			break;

		case 4:
			// enemigos del enjambre
			crearEnemigos(4, 6, 7, 7);
			// comandantes
			enemies[0][0].setXenjInicial(45);
			enemies[0][1].setXenjInicial(70);
			enemies[0][2].setXenjInicial(95);
			enemies[0][3].setXenjInicial(120);
			// goeis
			enemies[1][0].setXenjInicial(38);
			for (int i = 1; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(enemies[1][0].getXenjInicial() + (18 * i));
			}
			// zakos 3a fila
			enemies[2][0].setXenjInicial(35);
			for (int i = 1; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(enemies[2][0].getXenjInicial() + (16 * i));
			}
			// zakos 4a fila
			enemies[3][0].setXenjInicial(50);
			for (int i = 1; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(enemies[3][0].getXenjInicial() + (11 * i));
			}
			break;

		case 5:
			crearEnemigos(5, 6, 5, 4);

			for (int i = 0; i < enemies[0].length; i++) {
				enemies[0][i].setXenjInicial(50 + 20 * i - (i / 3) * 10);
			}
			enemies[0][2].setXenjInicial(85);

			for (int i = 0; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(50 + 10 * i + i / 3 * 20);
			}

			for (int i = 0; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(40 + 20 * i + i / 3 * 10);
			}
			enemies[2][2].setXenjInicial(85);

			for (int i = 0; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(70 + 10 * i);
			}

			break;

		case 6:

			crearEnemigos(6, 3, 10, 6);
			enemies[0][0].setXenjInicial(40);
			for (int i = 1; i < enemies[0].length; i++) {
				enemies[0][i].setXenjInicial(enemies[0][0].getXenjInicial() + 18 * i);
			}
			enemies[1][0].setXenjInicial(60);
			for (int i = 1; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(enemies[1][0].getXenjInicial() + (25 * i));
			}
			enemies[2][0].setXenjInicial(40);
			for (int i = 1; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(enemies[2][0].getXenjInicial() + (10 * i));
			}
			enemies[3][0].setXenjInicial(40);
			for (int i = 1; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(enemies[3][0].getXenjInicial() + (18 * i));
			}

			break;

		case 7:
			crearEnemigos(7, 3, 4, 4);

			for (int i = 0; i < enemies[0].length; i++) {
				if (i == 0 || i == 1)
					enemies[0][i].setXenjInicial(40 + 20 * i);
				if (i == 2 || i == 3 || i == 4)
					enemies[0][i].setXenjInicial(55 + i * 10);
				if (i == 5 || i == 6)
					enemies[0][i].setXenjInicial(10 + 20 * i);
			}

			enemies[1][0].setXenjInicial(50);
			enemies[1][1].setXenjInicial(85);
			enemies[1][2].setXenjInicial(120);

			for (int i = 0; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(60 + 20 * i - i / 2 * 10);
			}

			for (int i = 0; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(70 + 10 * i);
			}

			break;
		case 8:

			crearEnemigos(8, 6, 4, 4);
			enemies[0][0].setXenjInicial(43);
			for (int i = 1; i < enemies[0].length; i++) {
				enemies[0][i].setXenjInicial(enemies[0][0].getXenjInicial() + (12 * i));
			}
			enemies[1][0].setXenjInicial(47);
			for (int i = 1; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(enemies[1][0].getXenjInicial() + (15 * i));
			}
			enemies[2][0].setXenjInicial(52);
			for (int i = 1; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(enemies[2][0].getXenjInicial() + (22 * i));
			}
			enemies[3][0].setXenjInicial(40);
			for (int i = 1; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(enemies[3][0].getXenjInicial() + (30 * i));
			}
			break;

		case 9:
			crearEnemigos(9, 6, 6, 4);

			for (int i = 0; i < enemies[0].length; i++) {
				enemies[0][i].setXenjInicial(40 + 10 * i + i / 5 * 10);
			}
			enemies[0][4].setXenjInicial(85);

			for (int i = 1; i < enemies[1].length - 1; i++) {
				enemies[1][i].setXenjInicial(60 + i * 10);
			}
			enemies[1][0].setXenjInicial(50);
			enemies[1][5].setXenjInicial(120);

			for (int i = 0; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(40 + 10 * i + i / 2 * 10 + i / 3 * 20);
			}

			for (int i = 0; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(60 + 20 * i - i / 2 * 10);
			}

			break;

		case 10:
			crearEnemigos(10, 4, 6, 10);
			enemies[0][0].setXenjInicial(40);
			for (int i = 1; i < enemies[0].length; i++) {
				enemies[0][i].setXenjInicial(enemies[0][0].getXenjInicial() + (10 * i));
			}
			enemies[1][0].setXenjInicial(47);
			for (int i = 1; i < enemies[1].length; i++) {
				enemies[1][i].setXenjInicial(enemies[1][0].getXenjInicial() + (25 * i));
			}
			enemies[2][0].setXenjInicial(42);
			for (int i = 1; i < enemies[2].length; i++) {
				enemies[2][i].setXenjInicial(enemies[2][0].getXenjInicial() + (17 * i));
			}
			enemies[3][0].setXenjInicial(40);
			for (int i = 1; i < enemies[3].length; i++) {
				enemies[3][i].setXenjInicial(enemies[3][0].getXenjInicial() + (10 * i));
			}

			break;
		}
	}

}
