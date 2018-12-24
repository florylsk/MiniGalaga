/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;

public abstract class PowerUp extends Sprite {
	private int health;
	private String[] imagenesRotacion;

	public boolean isDestruido() {
		if (health == 0) {
			return false;
		} else {
			return true;
		}
	}

	public PowerUp(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		setHealth(1);
	}

	public abstract String[] crearArrImagenesRotacion();

	public abstract void cambiarImagen();

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String[] getImagenesRotacion() {
		return imagenesRotacion;
	}

	public void setImagenesRotacion(String[] imagenesRotacion) {
		this.imagenesRotacion = imagenesRotacion;
	}

}
