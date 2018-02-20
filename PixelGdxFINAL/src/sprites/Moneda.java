package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import pantallas.MapaUno;

public class Moneda extends Loot {
	// Tamaño del sprite
	private static final float SPRITE_WIDTH = 16;
	private static final float SPRITE_HEIGHT = 16;
	
	// Constructor
	public Moneda(MapaUno mapa, float x, float y, BodyType tipo) {
		super(mapa, x, y, SPRITE_WIDTH, SPRITE_HEIGHT, tipo);
	}

	@Override
	protected void cargarAnimacion() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		// Caraga la animación directamente del atlas
		for (int i = 1; i < 9; i++) 
			frames.add( new TextureRegion( new Texture("assets/moneda/" +i +".png") , 1 , 1, 45 ,48));
		animacion = new Animation(0.15f, frames);
	}
	

}
