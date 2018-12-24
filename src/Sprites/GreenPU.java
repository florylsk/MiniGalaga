package Sprites;

import edu.uc3m.game.GameBoardGUI;

public class GreenPU extends PowerUp{ //Da una vida extra (hasta que el jugador tenga un máximo de tres vidas)
	public GreenPU(int id, int x, int y, String imagen, GameBoardGUI gui, boolean exists) {
		super(id, x, y, imagen, gui, exists);
		this.setImagenesRotacion(crearArrImagenesRotacion());
		this.setExists(true);
	}

	public String[] crearArrImagenesRotacion() {
		String[] imagenes = { "green1.png", "green2.png", "green3.png", "green4.png", "green5.png", "green6.png" };
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
