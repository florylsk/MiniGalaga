/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.*;

public class Torpedo extends Sprite {

	public Torpedo(int id, int x, int y, GameBoardGUI gui, boolean exists, String imagen) {
		super(id, x, y, imagen, gui, exists);
	}

	public void moveJugador() {
		if (System.currentTimeMillis() - this.getLastActionTime() > 5) {// Cambio VELOCIDAD torpedo

			this.setY(this.getY() - 1);
			getGui().gb_moveSpriteCoord(getId(), getX(), getY());

			if (this.getY() < -2) {
				this.setExists(false);
			}
			this.setLastActionTime(System.currentTimeMillis());
		}
	}

	public void moveEnemigo() {
		if (System.currentTimeMillis() - this.getLastActionTime() > 5) { // Cambio VELOCIDAD torpedo

			this.setY(this.getY() + 1); // Cambio velocidad torpedo
			getGui().gb_moveSpriteCoord(getId(), getX(), getY());

			if (this.getY() > 220) {
				this.setExists(false);
				getGui().gb_setSpriteVisible(this.getId(), false);
			}
			this.setLastActionTime(System.currentTimeMillis());
		}
	}

}
