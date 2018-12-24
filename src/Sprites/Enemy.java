/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;
import edu.uc3m.principal.Conf;

public abstract class Enemy extends Sprite {
	private int health;
	private int reward;
	private String[] imagenesRot; // String con las diferentes imagenes de la rotacion del enemigo
	private boolean isDentroEnjambre;
	private int r;
	private int[] XYhueco = new int[2]; // Posicion del hueco del enemigo
	private int contadorAtaque;
	private int contadorEntrada;
	private Torpedo[] disparos;
	private int XenjInicial;
	private int YenjInicial;

	public boolean isAlive() {
		if (health == 0) {
			return false;
		} else {
			return true;
		}
	}

	public Enemy(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		setImagenesR(crearArrImagenesRotacion());
		setIsDentroEnjambre(true); // Inicialmente todos los enemigos forman parte de su enjambre
		setR(Conf.DIR_S);
		setDisparos(crearTorpedos());
		setContadorAtaque(0);
		setContadorEntrada(0);
	}

	public int getXenjInicial() {
		return XenjInicial;
	}

	public void setXenjInicial(int xenjInicial) {
		XenjInicial = xenjInicial;
	}

	public int getYenjInicial() {
		return YenjInicial;
	}

	public void setYenjInicial(int yenjInicial) {
		YenjInicial = yenjInicial;
	}

	public Torpedo[] getDisparos() {
		return this.disparos;
	}

	private void setDisparos(Torpedo[] disparos) {
		this.disparos = disparos;
	}

	public void setContadorAtaque(int contador) {
		this.contadorAtaque = contador;
	}

	public int getContadorAtaque() {
		return this.contadorAtaque;
	}

	public void setContadorEntrada(int contador) {
		this.contadorEntrada = contador;
	}

	public int getContadorEntrada() {
		return this.contadorEntrada;
	}

	public void setXYhueco(int x, int y) {
		int[] arr1 = { x, y };
		this.XYhueco = arr1;
	}

	public int[] getXYhueco() {
		return this.XYhueco;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getR() {
		return this.r;
	}

	public void setIsDentroEnjambre(boolean isDentroEnjambre) {
		this.isDentroEnjambre = isDentroEnjambre;
	}

	public boolean getIsDentroEnjambre() {
		return this.isDentroEnjambre;
	}

	public String[] getImagenesR() {
		return imagenesRot;
	}

	public void setImagenesR(String[] imagenesRot) {
		this.imagenesRot = imagenesRot;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public abstract String[] crearArrImagenesRotacion();
	public abstract void realizarMovEntrada(String lado);

	private Torpedo[] crearTorpedos() {
		Torpedo[] disparos = new Torpedo[5];
		for (int i = 0; i < disparos.length; i++) {
			disparos[i] = new Torpedo(10000 * this.getId() + i, 0, 0, this.getGui(), false, "torpedo200.png"); // Crea
																												// el
																												// torpedo
			getGui().gb_setSpriteVisible(disparos[i].getId(), false); // Inicialmente son invisibles
		}
		return disparos;
	}

	public void dispararTorpedos() {

		boolean disparoDisponible = false; // Booleano que dice se hay algun torpedo en el array que aun no exista
		for (int i = 0; i < disparos.length && disparoDisponible == false; i++) {
			if (disparos[i].getExists() == false) {// Busca un torpedo que no exista del array y lo usa para dispararlo
				disparoDisponible = true;
				disparos[i].setExists(true); // Se hace que el torpedo exista
				disparos[i].setX(this.getX());
				disparos[i].setY(this.getY() + 1);
				getGui().gb_moveSpriteCoord(disparos[i].getId(), disparos[i].getX(), disparos[i].getY());
				getGui().gb_setSpriteVisible(disparos[i].getId(), true); // Hacemos el torpedo visible

			}
		}
	}

	public void aletear(int centroEnjambre) {
		// Se alternan las imagenes cada vez que el enjambre se mueve 5 pixeles
		// Las imagenes del alteo dependen del tipo de enemigo
		String imagen1, imagen2;
		if (this instanceof Commander) {
			if (this.getHealth() == 2) { // Imagenes para el comandante con 2 vidas
				imagen1 = "enemy1G1.png";
				imagen2 = "enemy1G0.png";
			} else { // Imagenes para el comandante con una vida
				imagen1 = "enemy9G1.png";
				imagen2 = "enemy9G0.png";
			}

		} else if (this instanceof Goei) {
			imagen1 = "enemy2G0.png";
			imagen2 = "enemy2G1.png";
		} else {
			imagen1 = "enemy3G0.png";
			imagen2 = "enemy3G1.png";
		}

		if (centroEnjambre % 10 == 0 && this.getIsDentroEnjambre()) { // Cambia de imagen cada 5 pixeles
			this.setImagen(imagen1);
		} else if (centroEnjambre % 5 == 0 && this.getIsDentroEnjambre()) {
			this.setImagen(imagen2);
		}

		this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());

	}

	public void realizarMovAtaque(int xJugador, int yJugador) {

		if (contadorAtaque == 0) {
			this.moveDirection(Conf.DIR_S);
			this.setContadorAtaque(this.getContadorAtaque() + 1);
		} else if (this.getY() > this.getXYhueco()[1]) {
			if (System.currentTimeMillis() - this.getLastActionTime() > 55) {// Control de VELOCIDAD de ataque
				if (contadorAtaque > 0 && contadorAtaque <= 15) {
					this.moveDirection(Conf.DIR_S);
					// Realiza un circulo
				} else if (contadorAtaque < 27) {
					if (this.getR() == 15) {
						this.moveDirection(0);
					} else {
						this.moveDirection(this.getR() + 1);
					}
				} else if (contadorAtaque == 27) {
					if (this.getX() >= xJugador) {
						this.moveDirection(Conf.DIR_SW);
					} else if (this.getX() < xJugador) {
						this.moveDirection(Conf.DIR_SE);
					}
				} else if (this.getY() + 80 < yJugador) {
					this.moveDirection(this.getR());
				} else if (this.getY() + 20 < yJugador) {
					if (this.getX() >= xJugador) {
						this.moveDirection(Conf.DIR_SW);
					} else if (this.getX() < xJugador) {
						this.moveDirection(Conf.DIR_SE);
					}
				} else if (this.getY() + 15 < yJugador) {
					this.moveDirection(this.getR());
				} else {
					this.moveDirection(Conf.DIR_S);
				}

				this.setLastActionTime(System.currentTimeMillis());
				this.setContadorAtaque(this.getContadorAtaque() + 1);
				// El enemigo sale por abajo y vuelve a aparecer por arriba:
				if (this.getY() >= Conf.HEIGHT * 10) {
					this.setY(0);
				}
			}
		} else {
			reposicionarseEnjambre();

		}

	}

	protected void reposicionarseEnjambre() {

		if (this.getY() != this.getXYhueco()[1]) {

			if (System.currentTimeMillis() - this.getLastActionTime() > 40) {
				if (this.getX() > this.getXYhueco()[0]) {
					this.moveXY(this.getX() - 1, this.getY() + 1);
				} else if (this.getX() < this.getXYhueco()[0]) {
					this.moveXY(this.getX() + 1, this.getY() + 1);
				} else {
					this.moveXY(this.getXYhueco()[0], this.getY() + 1);
				}

				this.setLastActionTime(System.currentTimeMillis());
			}

		} else if (System.currentTimeMillis() - this.getLastActionTime() > 15) {
			if (this.getX() > this.getXYhueco()[0]) {
				this.setX(this.getX() - 1);
				this.setR(Conf.DIR_W);
			} else if (this.getX() < this.getXYhueco()[0]) {
				this.setX(this.getX() + 1);
				this.setR(Conf.DIR_E);
			}
			this.setLastActionTime(System.currentTimeMillis());
			this.moveXY(this.getX(), this.getXYhueco()[1]);
		}
		if (this.getX() == this.getXYhueco()[0] && this.getY() == this.getXYhueco()[1]) {

			if (this instanceof Zako) {
				((Zako) this).setIsUnGuardia(false);
			}
			this.setR(Conf.DIR_N);
			this.setIsDentroEnjambre(true);
			this.setContadorAtaque(0);
		}

		this.getGui().gb_setSpriteImage(this.getId(), this.getImagenesR()[this.getR()]);

	}

	public void moveDirection(int movimiento) {
		this.moveXY(this.getX() + Conf.Moves[movimiento][0], this.getY() + Conf.Moves[movimiento][1]);
		this.setR(movimiento);
		this.setImagen(this.getImagenesR()[this.getR()]);
		this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
	}

	public void moveXY(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.getGui().gb_moveSpriteCoord(this.getId(), this.getX(), this.getY());
	}

	public void explotar() {

		if (this.getImagen() != "explosion20.png" && this.getImagen() != "explosion21.png"
				&& this.getImagen() != "explosion22.png" && this.getImagen() != "explosion23.png"
				&& this.getImagen() != "explosion24.png") {

			this.setImagen("explosion20.png");
			this.setIsDentroEnjambre(false); // Enemigo deja de formar parte del enjambre
			this.setExists(false); // Enemigo deja de existir
			this.setLastActionTime(System.currentTimeMillis());
		}

		else if (System.currentTimeMillis() - this.getLastActionTime() > 60) { // Control VELOCIDAD explosion

			if (this.getImagen() == "explosion20.png") {
				this.setImagen("explosion21.png");

			} else if (this.getImagen() == "explosion21.png") {
				this.setImagen("explosion22.png");

			} else if (this.getImagen() == "explosion22.png") {
				this.setImagen("explosion23.png");

			} else if (this.getImagen() == "explosion23.png") {
				this.setImagen("explosion24.png");

			} else if (this.getImagen() == "explosion24.png") {
				this.getGui().gb_setSpriteVisible(this.getId(), false); // El enemigo se hace invisible

			}
			this.setLastActionTime(System.currentTimeMillis());
		}
		this.getGui().gb_setSpriteImage(this.getId(), this.getImagen());
	}

	public boolean isColisionado(Player jugador) { // comprueba si el enemigo esta colisionando con el jugador
		if (this.getX() >= jugador.getX() - 6 && this.getX() <= jugador.getX() + 6 && this.getY() >= jugador.getY() - 5
				&& this.getX() <= jugador.getY() + 5) {
			return true;
		}
		return false;
	}
}
