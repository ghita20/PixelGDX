package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import pantallas.MapaUno;

public class Serpiente extends Enemigo {
	
	// Vida máxima de la serpiente
	private static final int MAX_VIDA = 5;
	private static final int DAÑO_ATAQUE = 1;
	
	// Tamaño del cuerpo físico
	private static final float WIDTH = 8;
	private static final float HEIGHT = 10;
	
	// Tamaño de la imagen del sprite
	private static final float SPRITE_WIDTH = 32;
	private static final float SPRITE_HEIGHT = 32;
	
	// Constructor
	public Serpiente ( MapaUno mapa , float x , float y ) {
		super(mapa, x, y, WIDTH, HEIGHT , SPRITE_WIDTH, SPRITE_HEIGHT , MAX_VIDA , DAÑO_ATAQUE);
	}

	@Override
	protected void cargarAnimacionMovimiento() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		// Caraga la animación directamente del atlas
		for (int i = 0; i < 4; i++) 
			frames.add( new TextureRegion( new Texture("assets/serpiente/" +i +".png") , 1 , 1, 32 ,32));
		animacionMovimiento = new Animation(0.25f, frames);
		frames.clear();
		
		// Aturdido
		for (int i = 0; i < 1; i++) 
			frames.add( new TextureRegion( new Texture("assets/serpiente/aturdida.png") , 1 , 1, 32 ,32));
		animacionAturdido = new Animation(0.3f, frames);
	}

}
