package hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Corazon {
	
	// Un corazón puede almacenar 4 puntos de vida
	public static final int MAX_PUNTOS = 4;
	
	// Puntos de vida
	private int vida;

	// Imagenes de los corazones
	private Image fullVida , tresCuartos , mitadVida , cuartoDeVida , sinVida;

	// Constructor
	public Corazon ( ) {
		// Instancia las imagenes
		fullVida = new Image(new Texture("assets/stats/vida.png"));
		tresCuartos = new Image(new Texture("assets/stats/vida_1.png"));
		mitadVida = new Image(new Texture("assets/stats/vida_2.png"));
		cuartoDeVida = new Image(new Texture("assets/stats/vida_3.png"));
		sinVida = new Image(new Texture("assets/stats/sin_vida.png"));

		// Empieza a full vida
		vida = 4;
	}

	// Get vida
	public int getVida ( ) {
		return vida;
	}
	public void setVida ( int vida ) {
		this.vida = vida;
	}

	// Resta un punto de vida
	public void restarPunto ( ) {
		if ( vida > 0 )
			vida--;
	}

	// Devuelve la imagen segun el estado
	public Image getCorazon ( ) {
		switch ( vida ) {
		case 0 :
			return sinVida;
		case 1:
			return cuartoDeVida;
		case 2:
			return mitadVida;
		case 3:
			return tresCuartos;
		case 4:
			return fullVida;
		default:
			return sinVida;
		}
	}
}
