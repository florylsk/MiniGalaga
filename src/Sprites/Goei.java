/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;
import edu.uc3m.principal.Conf;

public class Goei extends Enemy {

	public Goei(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		this.setHealth(1);
		this.setReward(250);

	}

	public String[] crearArrImagenesRotacion() {
		String[] arr1 = { "enemy200.png", "enemy201.png", "enemy202.png", "enemy203.png", "enemy204.png",
				"enemy205.png", "enemy206.png", "enemy207.png", "enemy208.png", "enemy209.png", "enemy210.png",
				"enemy211.png", "enemy212.png", "enemy213.png", "enemy214.png", "enemy215.png" };
		return arr1;
	}

	public void realizarMovEntradaDerecha() {

		if (this.getContadorEntrada() == 1) {
			this.setX(0);
		} else if (this.getContadorEntrada() <= 30) { // Subida en diagonal
			this.moveDirection(Conf.DIR_NNE);

		} else if (this.getContadorEntrada() == 31) {
			this.moveDirection(Conf.DIR_NE);

		} else if (this.getContadorEntrada() == 32) {
			this.moveDirection(Conf.DIR_ENE);

		} else if (this.getContadorEntrada() == 33) {
			this.moveDirection(Conf.DIR_E);

		} else if (this.getContadorEntrada() <= 49) { // Movimiento circulatorio
			if (this.getR() == 15) {
				this.moveDirection(0);
			} else {
				this.moveDirection(this.getR() + 1);
			}

		} else if (this.getContadorEntrada() < 60) {
			this.moveDirection(Conf.DIR_E);

		} else if (this.getContadorEntrada() <= 80) {
			this.moveDirection(Conf.DIR_NNE);

		} else if (this.getYenjInicial() < this.getY()) {
			this.moveDirection(Conf.DIR_ENE);
		} else if (this.getX() != this.getXenjInicial()) {
			recolocarEnemigosTrasEntrada();
		} else {
			this.setR(Conf.DIR_N);
			this.setImagen(this.getImagenesR()[this.getR()]);
			this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
		}
	}

	public void realizarMovEntradaIzquierda() {
		// Posiciona a los goeis en su correspondiente lado

		if (this.getContadorEntrada() == 1) {
			this.setX(170);

		} else if (this.getContadorEntrada() <= 30) { // Subida en diagonal
			this.moveDirection(Conf.DIR_NNW);

		} else if (this.getContadorEntrada() == 31) {
			this.moveDirection(Conf.DIR_NW);

		} else if (this.getContadorEntrada() == 32) {
			this.moveDirection(Conf.DIR_WNW);

		} else if (this.getContadorEntrada() == 33) {
			this.moveDirection(Conf.DIR_W);

		} else if (this.getContadorEntrada() <= 49) { // Movimiento circulatorio
			if (this.getR() == 0) {
				this.moveDirection(15);
			} else {
				this.moveDirection(this.getR() - 1);
			}

		} else if (this.getContadorEntrada() < 60) {
			this.moveDirection(Conf.DIR_W);

		} else if (this.getContadorEntrada() <= 80) {
			this.moveDirection(Conf.DIR_NNW);

		} else if (this.getYenjInicial() < this.getY()) {
			this.moveDirection(Conf.DIR_WNW);

		} else if (this.getX() != this.getXenjInicial()) {
			recolocarEnemigosTrasEntrada();
		} else {
			this.setR(Conf.DIR_N);
			this.setImagen(this.getImagenesR()[this.getR()]);
			this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
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

	public void realizarMovEntrada(String lado) {
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
	}

}
