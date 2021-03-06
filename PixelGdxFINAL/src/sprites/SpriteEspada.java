package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import pantallas.MapaUno;

public class SpriteEspada extends Loot{
	// Tama�o del sprite
	private static final float SPRITE_WIDTH = 16;
	private static final float SPRITE_HEIGHT = 16;
	
	// Constructor
	public SpriteEspada(MapaUno mapa, float x, float y, BodyType tipo, boolean sensor) {
		super(mapa, x, y, SPRITE_WIDTH, SPRITE_HEIGHT, tipo, sensor);
	}

	@Override
	protected void cargarAnimacion() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		// Caraga la animaci�n directamente del atlas
		for (int i = 1; i < 9; i++) 
			frames.add( new TextureRegion( new Texture(Gdx.files.internal("assets/espada_basica/" +i +".png")) , 1 , 1, 45 ,48));
		animacion = new Animation(0.15f, frames);
	}

}
