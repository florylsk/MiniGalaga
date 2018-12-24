/**
 * @author Cristina Pinia & Florinel Olteanu
 */

package Sprites;

import edu.uc3m.game.GameBoardGUI;

public class Player extends Sprite {

	private int points, lifes, hits, shoots;
	private String name;
	private double velocity;
	private Torpedo[] disparos;
	private boolean isGod;
	
	public Player(int id, int x, int y, String imagen, int lifes, String name, GameBoardGUI gui, Boolean exists,
			int points) {
		super(id, x, y, imagen, gui, exists);
		setLifes(lifes);
		this.name = name;
		setPoints(0);
		setIsGod(false);
		setDisparos(crearTorpedos()); // Crea el array de torpedos al crear el

		// Valores iniciales del jugador que aparecen en la interfaz, a la derecha
		gui.gb_setValueHealthMax(this.getLifes());
		gui.gb_setValueHealthCurrent(this.getLifes());
		gui.gb_setTextPlayerName(this.getName());
		gui.gb_setTextPointsDown("Velocidad: ");
		gui.gb_setTextPointsUp("Puntos");
		gui.gb_setValuePointsUp(this.getPoints());
		gui.gb_setTextAbility1("Disparos: ");
		gui.gb_setValueAbility1(shoots);
		gui.gb_setTextAbility2("Aciertos: ");
		gui.gb_setValueAbility2(hits);
		
		//Crea las imagenes de "vidas" en la esquina inferior izquierda
		for(int i=1;i<=this.getLifes();i++) {
			gui.gb_addSprite(654+10*i, "player.png", true);
			gui.gb_setSpriteImage(654+10*i, "player.png");
			gui.gb_moveSpriteCoord(654+10*i, 10+10*i, 220);
			gui.gb_setSpriteVisible(654+10*i, true);
		}
	}
	

	public boolean isGod() {
		return isGod;
	}

	public void setIsGod(boolean isGod) {
		this.isGod = isGod;
	}

	public Torpedo[] getDisparos() {
		return disparos;
	}
	
	public void setDisparos(Torpedo[] disparos) {
		this.disparos = disparos;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public String getName() {
		return name;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public void moveLeft(boolean action) {
		if(action==true) {
			if (this.getX() > 3)
				this.setX((this.getX()) - 2); //Cambio velocidad movimiento
		}
	}
	public void moveRight(boolean action) {
		if(action==true) {
		if (this.getX() < 168)
			this.setX((this.getX()) + 2); //Cambio velocidad movimiento
		}
	}

	private Torpedo[] crearTorpedos() {
		Torpedo[] disparos = new Torpedo[200];
		for (int i = 0; i < disparos.length; i++) {
			disparos[i] = new Torpedo(i + 23, 0, this.getY() -1, this.getGui(), false, "torpedo100.png"); //Los torpedos aun no existen
			getGui().gb_setSpriteVisible(disparos[i].getId(), false); //Inicialmente son invisibles
		}
		return disparos;
	}

	public void dispararTorpedos() { 
		
		boolean disparoDisponible = false; //Booleano que dice se hay algun torpedo en el array que aun no exista
		for (int i = 0; i < disparos.length && disparoDisponible == false; i++) { 
			if (disparos[i].getExists() == false) {//Busca un torpedo que no exista del array y lo usa para dispararlo
				disparoDisponible = true; 
				disparos[i].setExists(true); //Se hace que el torpedo exista
				disparos[i].setX(this.getX());
				disparos[i].setY(this.getY()-1);
				getGui().gb_moveSpriteCoord(disparos[i].getId(), disparos[i].getX(), disparos[i].getY());
				getGui().gb_setSpriteVisible(disparos[i].getId(), true); // Hacemos el torpedo visible

				this.shoots++;
				getGui().gb_setValueAbility1(this.shoots); // Reflejamos en el marcador que se ha realizado un disparo mas
			}
		}
	}

}
