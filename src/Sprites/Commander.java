/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;
import edu.uc3m.principal.Conf;

public class Commander extends Enemy {

	Enemy guardiaIzq;
	Enemy guardiaCent;
	Enemy guardiaDer;

	public Commander(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		this.setHealth(2);
		this.setReward(500);
	}

	public String[] crearArrImagenesRotacion() {

		if (this.getHealth() == 1) {
			String[] arr1 = { "enemy900.png", "enemy901.png", "enemy902.png", "enemy903.png", "enemy904.png",
					"enemy905.png", "enemy906.png", "enemy907.png", "enemy908.png", "enemy909.png", "enemy910.png",
					"enemy911.png", "enemy912.png", "enemy913.png", "enemy914.png", "enemy915.png" };
			return arr1;
		} else {
			String[] arr2 = { "enemy100.png", "enemy101.png", "enemy102.png", "enemy103.png", "enemy104.png",
					"enemy105.png", "enemy106.png", "enemy107.png", "enemy108.png", "enemy109.png", "enemy110.png",
					"enemy111.png", "enemy112.png", "enemy113.png", "enemy114.png", "enemy115.png" };
			return arr2;
		}
	}

	public void realizarMovEntrada(String lado) {
		// Posiciona a los comandantes en su correspondiente lado
		if (System.currentTimeMillis() - this.getLastActionTime() > 42) {

			if (this.getContadorEntrada() <= 97) {
				switch (lado) {
				case "derecha":
					this.realizarMovEntradaDerecha();
					break;
				case "izquierda":
					this.realizarMovEntradaIzquierda();
					break;
				}

			} else if (this.getContadorEntrada() == 98) {
				this.setX(85);
			} else if (this.getYenjInicial() + 5 < this.getY()) {
				this.moveDirection(Conf.DIR_N);
			} else if (this.getYenjInicial() != this.getY()) { // Para que vaya mas lento hasta su Y de enjambre
				this.moveXY(this.getX(), this.getY() - 1);
			} else if (this.getX() != this.getXenjInicial()) {
				recolocarEnemigosTrasEntrada();
			} else {
				this.setR(Conf.DIR_N);
				this.setImagen(this.getImagenesR()[this.getR()]);
				this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
			}

			this.setContadorEntrada(this.getContadorEntrada() + 1);
			this.setLastActionTime(System.currentTimeMillis());
		}
	}

	public void realizarMovEntradaDerecha() {
		// Posiciona a los comandantes en su correspondiente lado
		if (this.getContadorEntrada() == 1) {
			this.setX(170);

		} else if (this.getContadorEntrada() <= 50) {
			this.moveDirection(Conf.DIR_NW);
		}

		else if (this.getContadorEntrada() <= 75) {
			this.moveDirection(Conf.DIR_E);

		} else if (this.getContadorEntrada() <= 90) {
			this.moveDirection(Conf.DIR_SSW);

		} else if (this.getContadorEntrada() <= 97) {
			this.moveDirection(Conf.DIR_SW);

		}

	}

	public void realizarMovEntradaIzquierda() {
		// Posiciona a los comandantes en su correspondiente lado
		if (this.getContadorEntrada() == 1) {
			this.setX(0);

		} else if (this.getContadorEntrada() <= 50) {
			this.moveDirection(Conf.DIR_NE);
		}

		else if (this.getContadorEntrada() <= 75) {
			this.moveDirection(Conf.DIR_W);

		} else if (this.getContadorEntrada() <= 90) {
			this.moveDirection(Conf.DIR_SSE);

		} else if (this.getContadorEntrada() <= 97) {
			this.moveDirection(Conf.DIR_SE);
		}
	}

	public void recolocarEnemigosTrasEntrada() {
		if (this.getX() > this.getXenjInicial()) {
			this.moveXY(this.getX() - 1, this.getY());
			this.setR(Conf.DIR_W);
		} else if (this.getX() < this.getXenjInicial()) {
			this.moveXY(this.getX() + 1, this.getY());
			this.setR(Conf.DIR_E);
		}
		this.setImagen(this.getImagenesR()[this.getR()]);
		this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
	}

	public void realizarAtaque(Enemy[] filaZakosSup, Enemy[] filaZakosInf) {
		if (this.getContadorAtaque() == 0) {// Primero busca que enemigos puede usar como guardias
			this.buscarGuardias(filaZakosSup, filaZakosInf);
		} else if (this.getContadorAtaque() == 1) {
			this.moveDirection(Conf.DIR_S);
			this.setContadorAtaque(this.getContadorAtaque() + 1);
			this.setLastActionTime(System.currentTimeMillis());
		} else if (this.getY() > this.getXYhueco()[1]) {

			// Coloca al Commander y a los Zakos en formación
			if (guardiaIzq.getR() != Conf.DIR_S || guardiaCent.getR() != Conf.DIR_S
					|| guardiaDer.getR() != Conf.DIR_S) {
				if (System.currentTimeMillis() - this.getLastActionTime() > 30) { // VELOCIDAD colocación en formación
					if (this.getY() < this.guardiaDer.getY() - 12) {
						this.moveXY(this.getX(), this.getY() + 2);
					}

					colocacionGuardia(this.guardiaIzq, -15);
					colocacionGuardia(this.guardiaCent, 0);
					colocacionGuardia(this.guardiaDer, 15);
					this.setLastActionTime(System.currentTimeMillis());
				}
			} else if (guardiaDer.getContadorAtaque() == 0) {
				guardiaDer.moveDirection(Conf.DIR_S);
				guardiaCent.moveDirection(Conf.DIR_S);
				guardiaIzq.moveDirection(Conf.DIR_S);
				this.moveDirection(Conf.DIR_S);

				guardiaDer.setContadorAtaque(1);
				guardiaCent.setContadorAtaque(1);
				guardiaIzq.setContadorAtaque(1);
			}

			else if (this.getY() <= Conf.HEIGHT * 10) {
				if (System.currentTimeMillis() - this.getLastActionTime() > 35) {
					this.moveDirection(Conf.DIR_S);
					this.setLastActionTime(System.currentTimeMillis());
				}
			} else if (this.getY() > Conf.HEIGHT * 10) {
				this.setY(0);
			}

			this.setContadorAtaque(this.getContadorAtaque() + 1);

		} else {
			this.reposicionarseEnjambre();
		}

	}

	private void buscarGuardias(Enemy[] filaZakosSup, Enemy[] filaZakosInf) {
		// Primero busca que enemigos puede usar como guardias
		this.setContadorAtaque(this.getContadorAtaque() + 1);
		int contador = 0;

		for (int i = 0; i < filaZakosSup.length && contador != 3; i++) { // Preferentemente usa de guardias los
																			// zakos de la fila
			// superior
			if (filaZakosSup[i].getExists() && filaZakosSup[i].getIsDentroEnjambre()) {
				((Zako) filaZakosSup[i]).setIsUnGuardia(true);
				filaZakosSup[i].setIsDentroEnjambre(false);
				switch (contador) {
				case 0:
					guardiaIzq = filaZakosSup[i];
					break;
				case 1:
					guardiaCent = filaZakosSup[i];
					break;
				case 2:
					guardiaDer = filaZakosSup[i];
					break;
				}
				filaZakosSup[i].setXYhueco(filaZakosSup[i].getX(), filaZakosSup[i].getY());
				contador++;
			}
		}

		if (contador < 3) { // Si no ha encotnrado todos los guardias en la fila superior busca en la fila
							// inferior
			for (int i = 0; i < filaZakosInf.length && contador != 3; i++) {
				if (filaZakosInf[i].getExists() && filaZakosInf[i].getIsDentroEnjambre()) {
					((Zako) filaZakosInf[i]).setIsUnGuardia(true);
					filaZakosInf[i].setIsDentroEnjambre(false);
					switch (contador) {
					case 0:
						guardiaIzq = filaZakosInf[i];
						break;
					case 1:
						guardiaCent = filaZakosInf[i];
						break;
					case 2:
						guardiaDer = filaZakosInf[i];
						break;
					}
					filaZakosInf[i].setXYhueco(filaZakosInf[i].getX(), filaZakosInf[i].getY());
					contador++;
				}
			}
		}

		if (contador < 3) { // Si no ha conseguido encontrar tres guardas
			this.setIsDentroEnjambre(true);
			this.setContadorAtaque(0); // Si no hay tres zakos disponibles el Commander no ataca
			switch (contador) {
			case 2:
				guardiaCent.setIsDentroEnjambre(true);
				((Zako) guardiaCent).setIsUnGuardia(false);
			case 1:
				guardiaIzq.setIsDentroEnjambre(true);
				((Zako) guardiaIzq).setIsUnGuardia(false);
			}
		}
	}

	private void colocacionGuardia(Enemy guardia, int variableColocacion) {
		if (guardia.getExists()) {
			if (guardia.getX() > this.getX() + variableColocacion) {
				guardia.setX(guardia.getX() - 1);
				guardia.setR(Conf.DIR_W);
			} else if (guardia.getX() < this.getX() + variableColocacion) {
				guardia.setX(guardia.getX() + 1);
				guardia.setR(Conf.DIR_E);
			} else {
				guardia.setR(Conf.DIR_S);
			}
			if (guardia.getY() != guardiaDer.getY()) {
				guardia.setY(guardia.getY() + 2);
			}
			guardia.moveXY(guardia.getX(), guardia.getY());
			guardia.setImagen(guardia.getImagenesR()[guardia.getR()]);
			this.getGui().gb_setSpriteImage(guardia.getId(), guardia.getImagen());
		}
	}

	public void haMuerto() {
		if (guardiaDer.getContadorAtaque() == 0) { // Si aún se encontraban los guardas en fase de colocacion con el
													// commander
			if (guardiaCent.getExists()) {
				guardiaCent.moveDirection(Conf.DIR_S);
				((Zako) this.guardiaCent).setIsUnGuardia(false);
			}
			if (guardiaIzq.getExists()) {
				guardiaIzq.moveDirection(Conf.DIR_S);
				((Zako) this.guardiaIzq).setIsUnGuardia(false);
			}
			if (guardiaDer.getExists()) {
				guardiaDer.moveDirection(Conf.DIR_S);
				((Zako) this.guardiaDer).setIsUnGuardia(false);
			}

		}
	}

}
