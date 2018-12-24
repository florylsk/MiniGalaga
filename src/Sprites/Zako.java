/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;
import edu.uc3m.principal.Conf;

public class Zako extends Enemy {

	private boolean isUnGuardia;

	public Zako(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		this.setHealth(1);
		this.setReward(100);

		this.setIsUnGuardia(false);

	}

	public boolean isUnGuardia() {
		return isUnGuardia;
	}

	public void setIsUnGuardia(boolean isUnGuardia) {
		this.isUnGuardia = isUnGuardia;
	}

	public String[] crearArrImagenesRotacion() {
		String[] arr1 = { "enemy300.png", "enemy301.png", "enemy302.png", "enemy303.png", "enemy304.png",
				"enemy305.png", "enemy306.png", "enemy307.png", "enemy308.png", "enemy309.png", "enemy310.png",
				"enemy311.png", "enemy312.png", "enemy313.png", "enemy314.png", "enemy315.png" };
		return arr1;
	}

	public void realizarMovEntradaDerecha() {
		// Posiciona a los comandantes en su correspondiente lado
		if (this.getContadorEntrada() == 1) {
			this.setX(160);
			this.setY(-2);

		} else if (this.getContadorEntrada() <= 10) {
			this.moveDirection(Conf.DIR_S);

		} else if (this.getContadorEntrada() <= 25) {
			this.moveDirection(Conf.DIR_SW);

		} else if (this.getContadorEntrada() <= 33) {
			this.moveDirection(Conf.DIR_W);

		} else if (this.getContadorEntrada() <= 81) {
			if (this.getContadorEntrada() % 3 == 0) {
				if (this.getR() == 15) {
					this.moveDirection(0);
				} else {
					this.moveDirection(this.getR() + 1);
				}
			} else {
				this.moveDirection(this.getR());
			}

		} else if (this.getContadorEntrada() <= 85) {
			this.moveDirection(Conf.DIR_W);

		} else if (this.getContadorEntrada() == 86) {
			this.moveDirection(Conf.DIR_WSW);

		} else if (this.getContadorEntrada() == 87) {
			this.moveDirection(Conf.DIR_SW);

		} else if (this.getContadorEntrada() == 88) {
			this.moveDirection(Conf.DIR_SSW);
		} else if (this.getContadorEntrada() <= 107) {
			this.moveDirection(Conf.DIR_S);

		} else if (this.getContadorEntrada() <= 115) {
			if (this.getR() == 0) {
				this.moveDirection(15);
			} else {
				this.moveDirection(this.getR() - 1);
			}
		}
	}

	public void realizarMovEntradaIzquierda() {

		// Posiciona a los comandantes en su correspondiente lado
		if (this.getContadorEntrada() == 1) {
			this.setX(10);
			this.setY(-2);
		} else if (this.getContadorEntrada() <= 10) {
			this.moveDirection(Conf.DIR_S);
		}

		else if (this.getContadorEntrada() <= 25) {
			this.moveDirection(Conf.DIR_SE);

		} else if (this.getContadorEntrada() <= 33) {
			this.moveDirection(Conf.DIR_E);

		} else if (this.getContadorEntrada() <= 81) {
			if (this.getContadorEntrada() % 3 == 0) {
				if (this.getR() == 0) {
					this.moveDirection(15);
				} else {
					this.moveDirection(this.getR() - 1);
				}
			} else {
				this.moveDirection(this.getR());
			}

		} else if (this.getContadorEntrada() <= 85) {
			this.moveDirection(Conf.DIR_E);

		} else if (this.getContadorEntrada() == 86) {
			this.moveDirection(Conf.DIR_ESE);

		} else if (this.getContadorEntrada() == 87) {
			this.moveDirection(Conf.DIR_SE);

		} else if (this.getContadorEntrada() == 88) {
			this.moveDirection(Conf.DIR_SSE);
		} else if (this.getContadorEntrada() <= 107) {
			this.moveDirection(Conf.DIR_S);
		} else if (this.getContadorEntrada() <= 115) {
			if (this.getR() == 15) {
				this.moveDirection(0);
			} else {
				this.moveDirection(this.getR() + 1);
			}
		}
	}

	public void recolocarEnemigosTrasEntrada() {

		if (this.getContadorEntrada() <= 138) {
			this.moveDirection(Conf.DIR_N);
		} else if (this.getContadorEntrada() == 139) {
			if (this.getX() > this.getXenjInicial()) {
				this.moveDirection(Conf.DIR_WNW);
			} else if (this.getX() < this.getXenjInicial()) {
				this.moveDirection(Conf.DIR_ENE);
			}
		} else if (this.getContadorEntrada() <= 155) {
			this.moveDirection(this.getR());
		} else if (this.getYenjInicial() != this.getY()) {
			this.moveXY(this.getX(), this.getY() - 1);
			this.setR(Conf.DIR_N);
			this.getGui().gb_setSpriteImage(this.getId(), this.getImagenesR()[this.getR()]);
		}

		// Mueve a derecha o izquierda cuando ya estan en su Y inicial
		else if (this.getX() > this.getXenjInicial()) {
			this.moveXY(this.getX() - 1, this.getY());
			this.setR(Conf.DIR_W);
		} else if (this.getX() < this.getXenjInicial()) {
			this.moveXY(this.getX() + 1, this.getY());
			this.setR(Conf.DIR_E);
		}
		this.setImagen(this.getImagenesR()[this.getR()]);
		this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());

	}

	public void realizarMovEntrada(String lado) {

		if (this.getContadorEntrada() <= 115) {
			if (System.currentTimeMillis() - this.getLastActionTime() > 42) {
				switch (lado) {
				case "derecha":
					this.realizarMovEntradaDerecha();
					break;
				case "izquierda":
					this.realizarMovEntradaIzquierda();
					break;
				}
				this.setContadorEntrada(this.getContadorEntrada() + 1);
				this.setLastActionTime(System.currentTimeMillis());
			}
		} else if (this.getX() != this.getXenjInicial() || this.getY() != this.getYenjInicial()) {
			if (System.currentTimeMillis() - this.getLastActionTime() > 20) {
				recolocarEnemigosTrasEntrada();

				this.setContadorEntrada(this.getContadorEntrada() + 1);
				this.setLastActionTime(System.currentTimeMillis());
			}

		} else {
			this.setR(Conf.DIR_N);
			this.setImagen(this.getImagenesR()[this.getR()]);
			this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
		}
	}

	public void realizarAtaqueGuardia() {

		if (this.getContadorAtaque() > 0) {
			if (this.getY() > this.getXYhueco()[1]) {
				if (System.currentTimeMillis() - this.getLastActionTime() > 35) {
					if (this.getY() <= Conf.HEIGHT * 10) {
						this.moveDirection(Conf.DIR_S);
					} else if (this.getY() > Conf.HEIGHT * 10) {
						this.moveXY(this.getX(), 0);
					}

					this.setContadorAtaque(this.getContadorAtaque() + 1);
					this.setLastActionTime(System.currentTimeMillis());
				}
			} else {
				this.reposicionarseEnjambre();
			}
		}

	}

}
