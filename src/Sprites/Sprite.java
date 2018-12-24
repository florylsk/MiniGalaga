/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;

public abstract class Sprite {
	private int id;
	private int x;
	private int y;
	private String imagen;
	private GameBoardGUI gui;
	private boolean exists;
	private long lastActionTime;

	public Sprite(int id, int x, int y, String imagen, GameBoardGUI gui, Boolean exists) {
		setId(id);
		setX(x);
		setY(y);
		setImagen(imagen);
		this.gui = gui;
		setExists(exists);
		setLastActionTime(System.currentTimeMillis());

		gui.gb_addSprite(id, imagen, true);
		gui.gb_setSpriteVisible(id, true);
	}

	public long getLastActionTime() {
		return lastActionTime;
	}

	public void setLastActionTime(long lastActionTime) {
		this.lastActionTime = lastActionTime;
	}

	public boolean getExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public GameBoardGUI getGui() {
		return gui;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
