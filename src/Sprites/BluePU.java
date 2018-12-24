/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;

public class BluePU extends PowerUp { //Da 10 segundos de modo invencible
	public BluePU(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		this.setImagenesRotacion(crearArrImagenesRotacion());
		this.setExists(true);
	}

	public String[] crearArrImagenesRotacion() {
		String[] imagenes = { "blue1.png", "blue2.png", "blue3.png", "blue4.png", "blue5.png", "blue6.png" };
		return imagenes;
	}

	public void cambiarImagen() {
		if (this.getImagen() == this.getImagenesRotacion()[0]) {
			this.setImagen(this.getImagenesRotacion()[1]);
		} else if (this.getImagen() == this.getImagenesRotacion()[1]) {
			this.setImagen(this.getImagenesRotacion()[2]);
		} else if (this.getImagen() == this.getImagenesRotacion()[2]) {
			this.setImagen(this.getImagenesRotacion()[3]);
		} else if (this.getImagen() == this.getImagenesRotacion()[3]) {
			this.setImagen(this.getImagenesRotacion()[4]);
		} else if (this.getImagen() == this.getImagenesRotacion()[4]) {
			this.setImagen(this.getImagenesRotacion()[5]);
		} else if (this.getImagen() == this.getImagenesRotacion()[5]) {
			this.setImagen(this.getImagenesRotacion()[0]);
		}
	}

}
