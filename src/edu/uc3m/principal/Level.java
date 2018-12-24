/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package edu.uc3m.principal;

import Sprites.*;
import edu.uc3m.game.*;

import java.io.*;

import javax.sound.sampled.*;

public class Level {

	private int nivel;
	private Swarm enjambre;
	private Player jugador;
	private GameBoardGUI gui;
	private long lastActionTime; // para el control de las velocidades
	private boolean key_left_pressed = false;
	private boolean key_right_pressed = false;
	private PowerUp[] PowerUps = new PowerUp[10];
	private int num_powerUps = 1;
	private long contador_PU; // contador para el efecto de powerups

	private boolean exit = false;

	public Level(int nivel, Player jugador, GameBoardGUI gui) {
		this.nivel = nivel;
		this.jugador = jugador;
		this.gui = gui;
		this.enjambre = new Swarm(this.gui, this.nivel);
		this.lastActionTime = System.currentTimeMillis();
		this.contador_PU = 0;

	}

	// Usando la libreria sound para producir un sonido en cada disparo
	private void sonido_disparo() {
		try {
			// agrega el archivo de sonido al programa
			File soundFile = new File(getClass().getResource("/sound/laser_sound.wav").getFile());
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

	private void move_jugador() {
		String accion = this.gui.gb_getLastAction();
		// Movimiento hacia la derecha (continuo)
		if (accion == "left") {
			key_left_pressed = true;
			key_right_pressed = false;
			// Movimiento hacia la izquierda (continuo)
		} else if (accion == "right") {
			key_right_pressed = true;
			key_left_pressed = false;
			// Disparo
		} else if (accion == "space") {
			key_right_pressed = false;
			key_left_pressed = false;
			this.jugador.dispararTorpedos();
			sonido_disparo();
			this.gui.gb_clearCommandBar();
			// Pausa del movimiento derecha/izquierda
		} else if (accion == "down") {
			key_right_pressed = false;
			key_left_pressed = false;
		}
		// boton salir
		else if (accion == "exit game") {
			System.exit(1);
		} // boton nueva partida
		else if (accion.contains("new game")) {
			MiniGalaga.repetir = true;
			exit = true;
			MiniGalaga.newName = accion.substring(9);
			this.gui.gb_clearSprites();

			// Modo invencible
		} else if (accion.equals("command god")) {
			if (this.jugador.isGod() == false) {
				this.gui.gb_println("Modo invencible activado");
				this.jugador.setIsGod(true);
			} else {
				this.gui.gb_println("Modo invencible desactivado");
				this.jugador.setIsGod(false);

			}

			this.gui.gb_clearCommandBar();
		}

		this.jugador.moveLeft(key_left_pressed);
		this.jugador.moveRight(key_right_pressed);
		// posiciona al jugador
		gui.gb_moveSpriteCoord(jugador.getId(), jugador.getX(), jugador.getY());
	}

	// crea los powerups sin enseniarlos
	private void crearPowerUps() {
		double rnd = Math.random();
		int rndx = (int) (Math.random() * 220);
		int rndy = (int) (Math.random() * 85);
		if (rnd >= 0.995) {
			double rndTipo = Math.random();
			if (rndTipo >= 0 && rndTipo < 0.25) {
				if (num_powerUps <= 10) {
					BluePU blue = new BluePU(999 * num_powerUps, rndx, rndy, "blue1.png", this.gui, true);
					gui.gb_moveSpriteCoord(blue.getId(), blue.getX(), blue.getY());
					gui.gb_setSpriteVisible(blue.getId(), false);
					PowerUps[num_powerUps - 1] = blue;
					num_powerUps++;
				}
			} else if (rndTipo >= 0.25 && rndTipo < 0.5) {
				if (num_powerUps <= 10) {
					YellowPU yellow = new YellowPU(1892 * num_powerUps, rndx, rndy, "yellow1.png", this.gui, true);
					gui.gb_moveSpriteCoord(yellow.getId(), yellow.getX(), yellow.getY());
					gui.gb_setSpriteVisible(yellow.getId(), false);
					PowerUps[num_powerUps - 1] = yellow;
					num_powerUps++;
				}

			} else if (rndTipo >= 0.5 && rndTipo < 0.75) {
				if (num_powerUps <= 10) {
					RedPU red = new RedPU(7854 * num_powerUps, rndx, rndy, "red1.png", this.gui, true);
					gui.gb_moveSpriteCoord(red.getId(), red.getX(), red.getY());
					gui.gb_setSpriteVisible(red.getId(), false);
					PowerUps[num_powerUps - 1] = red;
					num_powerUps++;
				}

			} else {
				if (num_powerUps <= 10) {
					GreenPU green = new GreenPU(4251 * num_powerUps, rndx, rndy, "green1.png", this.gui, true);
					gui.gb_moveSpriteCoord(green.getId(), green.getX(), green.getY());
					gui.gb_setSpriteVisible(green.getId(), false);
					PowerUps[num_powerUps - 1] = green;
					num_powerUps++;
				}

			}

		}
	}

	// ensenia los powerups a la gameboard
	private void SpawnPowerUps() {

		if (num_powerUps > 1) {
			for (int j = 0; j < num_powerUps - 1; j++) {

				if (PowerUps[j].getExists()) {
					gui.gb_setSpriteVisible(PowerUps[j].getId(), true);
					PowerUps[j].cambiarImagen();
					gui.gb_setSpriteImage(PowerUps[j].getId(), PowerUps[j].getImagen());
				}

			}
		}

	}

	public void ejecutarNivel() throws InterruptedException {

		gui.gb_setValueLevel(this.nivel);

		while (!exit) {
			for (int i = 0; i < jugador.getDisparos().length; i++) { // Comprueba si hay alguna bala del jugafor en
																		// movimiento y si
																		// algun enemigo es alcanzado
				if (jugador.getDisparos()[i].getExists()) {
					jugador.getDisparos()[i].moveJugador();
					enemigoAlcanzado(jugador.getDisparos()[i]);
				}
			}

			explotarEnemigos(); // Controla las explosiones de los enemigos

			if (this.enjambre.isEntradaRealizada() == false) {
				Entrada();
				this.enjambre.comprobarEntradaCompletada();
			} else {
				// creacion,spawn y destruccion de powerups
				crearPowerUps();
				SpawnPowerUps();

				for (int i = 0; i < jugador.getDisparos().length; i++) {
					PowerUpDestruido(jugador.getDisparos()[i]);
				}

				long intervaloTiempo = System.currentTimeMillis() - this.contador_PU;
				if (intervaloTiempo > 9500 && intervaloTiempo < 10500) {
					this.jugador.setIsGod(false);
					this.gui.gb_showMessageDialog("Se le ha acabado el efecto godmode!");
					this.contador_PU = 0;
				}

				enjambre.move(); // Mueve al enjambre
				atacarAlJugador(); // Los enemigos atacan al jugador de forma aleatoria
				// volver al lobby y romper el bucle true

				if (this.jugador.isGod() == false && (isJugadorColisionado() || this.jugador.getLifes() == 0)) {
					this.jugador.setImagen("explosion13.png");
					this.gui.gb_setSpriteImage(this.jugador.getId(), this.jugador.getImagen());
					if (isJugadorColisionado())
						this.gui.gb_showMessageDialog("HAN COLISIONADO CONTIGO. HAS PERDIDO LA PARTIDA");
					if (this.jugador.getLifes() == 0)
						this.gui.gb_showMessageDialog("TE HAS QUEDADO SIN VIDAS. HAS PERDIDO LA PARTIDA");
					this.jugador.setLifes(0);
					this.gui.gb_setValueHealthCurrent(this.jugador.getLifes());

					MiniGalaga.newName = null;
					MiniGalaga.repetir = true;
					exit = true;

				}

			}

			// movimiento del jugador
			move_jugador();

			Thread.sleep(1000 / 60); // Frames por segundo

			if (enjambre.isDestruido() == true) { // Comprueba si el enjambre esta destruido
				pasarDeNivel();
				exit = true; // Sale del nivel actual
			}

		}

	}

	private void Entrada() {
		if (System.currentTimeMillis() - this.lastActionTime > 120) { // Retardo de salida entre los enemigos
			comienzoEntradaPorFila(this.enjambre.getEnemies()[0]);
			comienzoEntradaPorFila(this.enjambre.getEnemies()[1]);
			comienzoEntradaPorFila(this.enjambre.getEnemies()[2]);
			comienzoEntradaPorFila(this.enjambre.getEnemies()[3]);
			this.lastActionTime = System.currentTimeMillis();
		}

		movEntradaPorFila(this.enjambre.getEnemies()[0]); // Fila Commanders
		movEntradaPorFila(this.enjambre.getEnemies()[1]); // Fila Goeis
		movEntradaPorFila(this.enjambre.getEnemies()[2]); // Fila superior Zakos
		movEntradaPorFila(this.enjambre.getEnemies()[3]); // Fila inferior Zakos
	}

	private void comienzoEntradaPorFila(Enemy[] filaEnemigos) {
		boolean encontrado = false;
		for (int i = 0; i < (int) (filaEnemigos.length / 2) && encontrado == false; i++) {
			if (filaEnemigos[i].getContadorEntrada() == 0) {
				filaEnemigos[i].setContadorEntrada(1);
				this.gui.gb_setSpriteVisible(filaEnemigos[i].getId(), true);
				encontrado = true;
			}
		}

		encontrado = false;
		for (int i = (int) (filaEnemigos.length / 2); i < filaEnemigos.length && encontrado == false; i++) {
			if (filaEnemigos[i].getContadorEntrada() == 0) {
				filaEnemigos[i].setContadorEntrada(1);
				this.gui.gb_setSpriteVisible(filaEnemigos[i].getId(), true);
				encontrado = true;
			}
		}
	}

	private void movEntradaPorFila(Enemy[] filaEnemigos) {
		// Movimiento Commanders
		for (int i = 0; i < (int) (filaEnemigos.length / 2); i++) {
			if (filaEnemigos[i].getContadorEntrada() > 0 && filaEnemigos[i].getExists()) {
				filaEnemigos[i].realizarMovEntrada("izquierda");

			}
		}
		for (int i = (int) (filaEnemigos.length / 2); i < filaEnemigos.length; i++) {
			if (filaEnemigos[i].getContadorEntrada() > 0 && filaEnemigos[i].getExists()) {
				filaEnemigos[i].realizarMovEntrada("derecha");

			}
		}
	}

	private void atacarAlJugador() {
		atacarAlJugadorCommanders(this.enjambre.getEnemies()[0]);
		atacarAlJugadorPorFila(this.enjambre.getEnemies()[1]);
		atacarAlJugadorPorFila(this.enjambre.getEnemies()[2]);
		atacarAlJugadorPorFila(this.enjambre.getEnemies()[3]);
	}

	private void atacarAlJugadorCommanders(Enemy[] enemigo) {
		// Ataque en momento aleatorio
		int random;

		for (int i = 0; i < enemigo.length; i++) { // Movimiento de ataque
			if (enemigo[i].getExists()) {
				if (enemigo[i].getIsDentroEnjambre() && isComandanteAtacando() == false) {// Solo puede atacar un
																							// comandante a la vez
					random = (int) (Math.random() * 300); // PROBABILIDAD de que salgan del enjambre
					if (random == 1) {
						enemigo[i].setXYhueco(enemigo[i].getX(), enemigo[i].getY()); // Enemigo deja un hueco en el
																						// enjambre
						enemigo[i].setIsDentroEnjambre(false); // Deja de formar parte del enjambre
						((Commander) enemigo[i]).realizarAtaque(this.enjambre.getEnemies()[2],
								this.enjambre.getEnemies()[3]);
					}
				} else if (!enemigo[i].getIsDentroEnjambre()) {
					((Commander) enemigo[i]).realizarAtaque(this.enjambre.getEnemies()[2],
							this.enjambre.getEnemies()[3]);
					random = (int) (Math.random() * 150); // PROBABILIDAD de que disparen

					if (random == 2 && enemigo[i].getY() > enemigo[i].getXYhueco()[1]
							&& enemigo[i].getY() < this.jugador.getX() - 10) {
						// Disparo aleatorio por parte
						// de los enemigos
						enemigo[i].dispararTorpedos();
					}
				}
			}
			// Mueve los disparos realizados y comprueba si el enemigo ha sido alcanzado
			for (int j = 0; j < enemigo[i].getDisparos().length; j++) {
				if (enemigo[i].getDisparos()[j].getExists()) {
					enemigo[i].getDisparos()[j].moveEnemigo();

					if (this.jugador.isGod() == false) { // Comprueba si el jugador es invencible
						jugadorAlcanzado(enemigo[i].getDisparos()[j]);
					}
				}
			}
		}
	}

	private boolean isComandanteAtacando() {
		for (int i = 0; i < this.enjambre.getEnemies()[0].length; i++) {
			if (this.enjambre.getEnemies()[0][i].getExists()
					&& this.enjambre.getEnemies()[0][i].getIsDentroEnjambre() == false) {
				return true;
			}
		}
		return false;
	}

	private void atacarAlJugadorPorFila(Enemy[] enemigo) {
		// Ataque en momento aleatorio
		int random;

		for (int i = 0; i < enemigo.length; i++) { // Movimiento de ataque
			if (enemigo[i].getExists()) {
				if (enemigo[i].getIsDentroEnjambre()) {
					random = (int) (Math.random() * 1400); // PROBABILIDAD de que salgan del enjambre
					if (random == 1) {
						enemigo[i].setXYhueco(enemigo[i].getX(), enemigo[i].getY()); // Enemigo deja un hueco en el
																						// enjambre
						enemigo[i].setIsDentroEnjambre(false); // Deja de formar parte del enjambre
						enemigo[i].realizarMovAtaque(this.jugador.getX(), this.jugador.getY());
					}
				} else {

					if (!(enemigo[i] instanceof Zako)
							|| (enemigo[i] instanceof Zako) && !((Zako) enemigo[i]).isUnGuardia()) {
						enemigo[i].realizarMovAtaque(this.jugador.getX(), this.jugador.getY());
					} else {
						((Zako) enemigo[i]).realizarAtaqueGuardia();
					}

					random = (int) (Math.random() * 150); // PROBABILIDAD de que disparen

					if (random == 2 && enemigo[i].getY() > enemigo[i].getXYhueco()[1]
							&& enemigo[i].getY() < this.jugador.getX() - 10) {
						// Disparo aleatorio por parte
						// de los enemigos
						enemigo[i].dispararTorpedos();
					}
				}
			}
			// Mueve los disparos realizados y comprueba si el enemigo ha sido alcanzado
			for (int j = 0; j < enemigo[i].getDisparos().length; j++) {
				if (enemigo[i].getDisparos()[j].getExists()) {
					enemigo[i].getDisparos()[j].moveEnemigo();

					if (this.jugador.isGod() == false) { // Comprueba si el jugador es invencible
						jugadorAlcanzado(enemigo[i].getDisparos()[j]);
					}
				}
			}
		}

	}

	private void jugadorAlcanzado(Torpedo disparo) {

		if ((disparo.getX() <= this.jugador.getX() + 6 && disparo.getX() >= this.jugador.getX() - 6)
				&& disparo.getY() <= this.jugador.getY() + 6 && disparo.getY() >= this.jugador.getY() - 6) {

			this.jugador.setLifes(this.jugador.getLifes() - 1);
			this.gui.gb_setValueHealthCurrent(this.jugador.getLifes());
			if (this.jugador.getLifes() == 0) { // Jugador ya no tiene vidas: Se acaba el juego
				this.gui.gb_showMessageDialog(
						"FIN DE LA PARTIDA. HAS CONSEGUIDO " + this.jugador.getPoints() + " PUNTOS");

			} else {
				// Se notifica de la perdida de vida por pantalla, la partida continua
				this.gui.gb_showMessageDialog("HAS PERDIDO UNA VIDA. VIDAS RESTANTES: " + this.jugador.getLifes());
				if (this.jugador.getLifes() == 2) {
					gui.gb_setSpriteVisible(684, false);
				} else if (this.jugador.getLifes() == 1) {
					gui.gb_setSpriteVisible(674, false);
				}

			}

			disparo.setExists(false);
			this.gui.gb_setSpriteVisible(disparo.getId(), false);
		}

	}

	private boolean isJugadorColisionado() {
		for (int i = 0; i < this.enjambre.getEnemies()[0].length; i++) {
			if (this.enjambre.getEnemies()[0][i].getExists()
					&& this.enjambre.getEnemies()[0][i].isColisionado(this.jugador)) {
				this.enjambre.getEnemies()[0][i].setExists(false);
				return true;
			}
		}
		for (int i = 0; i < this.enjambre.getEnemies()[1].length; i++) {
			if (this.enjambre.getEnemies()[1][i].getExists()
					&& this.enjambre.getEnemies()[1][i].isColisionado(this.jugador)) {
				this.enjambre.getEnemies()[1][i].setExists(false);
				return true;
			}
		}
		for (int i = 0; i < this.enjambre.getEnemies()[2].length; i++) {
			if (this.enjambre.getEnemies()[2][i].getExists()
					&& this.enjambre.getEnemies()[2][i].isColisionado(this.jugador)) {
				this.enjambre.getEnemies()[2][i].setExists(false);
				return true;
			}
		}
		for (int i = 0; i < this.enjambre.getEnemies()[3].length; i++) {
			if (this.enjambre.getEnemies()[3][i].getExists()
					&& this.enjambre.getEnemies()[3][i].isColisionado(this.jugador)) {
				this.enjambre.getEnemies()[3][i].setExists(false);
				return true;
			}
		}
		return false;
	}

	private void enemigoAlcanzado(Torpedo disparo) { // Comprueba si algun enemigo del enjambre total ha sido alcanzadp,
														// para
														// ello se invoca el metodo superior de enemigoAlcanzadoPorFila
														// para cada una de las cuatro filas del enjambre.
		enemigoAlcanzadoPorFila(disparo, enjambre.getEnemies()[0]);
		enemigoAlcanzadoPorFila(disparo, enjambre.getEnemies()[1]);
		enemigoAlcanzadoPorFila(disparo, enjambre.getEnemies()[2]);
		enemigoAlcanzadoPorFila(disparo, enjambre.getEnemies()[3]);

	}

	private void enemigoAlcanzadoPorFila(Torpedo disparo, Enemy[] enemigo) { // Comprueba si enemigos de una fila han
																				// sido alcanzados
		for (int i = 0; i < enemigo.length; i++) {
			if (!this.enjambre.isDestruido()) {
				if (enemigo[i].getExists()) { // Solo puede ser alcanzado un enemigo que aun no haya sido matado, es
												// decir,
												// que exista
					if (disparo.getX() >= enemigo[i].getX() - 5 && disparo.getX() <= enemigo[i].getX() + 5
							&& disparo.getY() >= enemigo[i].getY() - 5 && disparo.getY() <= enemigo[i].getY()) {

						disparo.setExists(false); // El disparo deja de existir y a continuacion lo hacemos invisible
						this.gui.gb_setSpriteVisible(disparo.getId(), false);

						jugador.setHits(jugador.getHits() + 1); // Aumenta el numero de aciertos
						this.gui.gb_setValueAbility2(jugador.getHits());

						enemigo[i].setHealth(enemigo[i].getHealth() - 1); // Enemigo pierde una vida

						if (enemigo[i].isAlive() == false) { // Si el enemigo ha muerto:
							jugador.setPoints(jugador.getPoints() + enemigo[i].getReward()); // El jugador gana puntos
							this.gui.gb_setValuePointsUp(jugador.getPoints());

							gui.gb_println("El enemigo " + enemigo[i].getId() + " ha muerto. Ganas "
									+ enemigo[i].getReward() + " puntos");// Se informa de la muerte por consola

							if (enemigo[i] instanceof Commander && !enemigo[i].getIsDentroEnjambre()) {
								((Commander) enemigo[i]).haMuerto();
							}

							enemigo[i].explotar(); // El enemigo explota

						} else if (enemigo[i] instanceof Commander) { // Si el enemigo es de tipo commander y no ha
																		// muerto
																		// pero ha sido alcanzado, cambia la imagen del
																		// comander
							((Commander) enemigo[i]).setImagenesR(enemigo[i].crearArrImagenesRotacion());
							this.gui.gb_setSpriteImage(enemigo[i].getId(), enemigo[i].getImagen());
						}
					}
				}
			} else {
				this.gui.gb_setSpriteVisible(enemigo[i].getId(), false);

			}

		}
	}

	private void explotarEnemigos() {
		explotarEnemigosPorFila(this.enjambre.getEnemies()[0]);
		explotarEnemigosPorFila(this.enjambre.getEnemies()[1]);
		explotarEnemigosPorFila(this.enjambre.getEnemies()[2]);
		explotarEnemigosPorFila(this.enjambre.getEnemies()[3]);
	}

	private void explotarEnemigosPorFila(Enemy[] enemigo) {

		for (int i = 0; i < enemigo.length; i++) {
			if (enemigo[i].getImagen() == "explosion20.png" || enemigo[i].getImagen() == "explosion21.png"
					|| enemigo[i].getImagen() == "explosion22.png" || enemigo[i].getImagen() == "explosion23.png"
					|| enemigo[i].getImagen() == "explosion24.png") {

				enemigo[i].explotar();
			}
		}
	}

	private void pasarDeNivel() {

		// Elimina los disparos del jugador en pantalla
		for (int i = 0; i < this.jugador.getDisparos().length; i++) {
			this.jugador.getDisparos()[i].setExists(false); // El disparo deja de existir y a continuacion lo hacemos
															// invisible
			this.gui.gb_setSpriteVisible(this.jugador.getDisparos()[i].getId(), false);
		}
		for (int i = 0; i < this.num_powerUps - 1; i++) {
			this.PowerUps[i].setExists(false);
			gui.gb_setSpriteVisible(this.PowerUps[i].getId(), false);

		}
		this.num_powerUps = 1;

		jugador.setPoints(jugador.getPoints() + 1000); // El jugador gana puntos por haber destruido al enjambre // y
														// pasa de nivel
		this.gui.gb_setValuePointsUp(jugador.getPoints());
		if (this.nivel != 10) {
			this.gui.gb_showMessageDialog("HAS SUPERADO EL NIVEL" + this.nivel + "!\nPREPARATE PARA EL SIGUIENTE");
		} else {
			this.gui.gb_showMessageDialog("HAS SUPERADO EL JUEGO! \nENHORABUENA");
		}
		this.gui.gb_println("\nHAS SUPERADO EL NIVEL " + this.nivel + ",\nHAS GANADO 1000 PUNTOS\n");

	}

	private void PowerUpDestruido(Torpedo disparo) {

		for (int i = 0; i < num_powerUps - 1; i++) {
			if (num_powerUps > 1) {
				if (this.PowerUps[i].getExists()) {
					if (disparo.getX() >= this.PowerUps[i].getX() - 8 && disparo.getX() <= this.PowerUps[i].getX() + 8
							&& disparo.getY() >= this.PowerUps[i].getY() - 8
							&& disparo.getY() <= this.PowerUps[i].getY()) {
						disparo.setExists(false);
						this.PowerUps[i].setExists(false);
						this.gui.gb_setSpriteVisible(disparo.getId(), false);
						jugador.setHits(jugador.getHits() + 1);
						this.gui.gb_setValueAbility2(jugador.getHits());
						this.PowerUps[i].setHealth(0);
						this.gui.gb_setSpriteVisible(this.PowerUps[i].getId(), false);
						if (this.PowerUps[i] instanceof BluePU) {

							if (this.jugador.isGod() == false) {
								this.jugador.setIsGod(true);
								this.gui.gb_showMessageDialog(
										"Ha conseguido el PowerUp GodMode, la duracion sera de 10 segundos, ¡aprovéchelos!");
								this.contador_PU = System.currentTimeMillis();
							} else {
								this.gui.gb_showMessageDialog(
										"Ha conseguido el PowerUp GodMode, no le hace efecto al ya tener el modo invencible activado");
							}
						} else if (this.PowerUps[i] instanceof YellowPU) {
							gui.gb_showMessageDialog("Ha conseguido 5000 Puntos extra!");
							this.jugador.setPoints(this.jugador.getPoints() + 5000);
							this.gui.gb_setValuePointsUp(jugador.getPoints());
						} else if (this.PowerUps[i] instanceof RedPU) {
							gui.gb_showMessageDialog("Ha perdido la partida por destruir el orbe rojo!");
							MiniGalaga.newName = null;
							MiniGalaga.repetir = true;
							exit = true;
						} else if (this.PowerUps[i] instanceof GreenPU) {

							if (this.jugador.getLifes() < 3) {
								gui.gb_showMessageDialog("Ha ganado una vida por destruir el orbe verde!!");
								this.jugador.setLifes(this.jugador.getLifes() + 1);
								gui.gb_setValueHealthCurrent(this.jugador.getLifes());
								if (this.jugador.getLifes() == 3) {
									gui.gb_setSpriteVisible(684, true);
								} else if (this.jugador.getLifes() == 2) {
									gui.gb_setSpriteVisible(674, true);
								} else if (this.jugador.getLifes() == 1) {
									gui.gb_setSpriteVisible(664, true);
								}
							} else {
								gui.gb_showMessageDialog(
										"Ya tiene el máximo de vidas, el orbe verde no le hace efecto");
							}
						}

					}
				}
			}
		}
	}

}
