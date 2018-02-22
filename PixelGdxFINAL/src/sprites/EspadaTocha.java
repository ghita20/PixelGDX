package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import pantallas.MapaUno;

public class EspadaTocha extends Loot{
	
	public EspadaTocha(MapaUno mapa, float x, float y, float spriteWidth, float spriteHeight, BodyType tipo, boolean sensor) {
		super(mapa, x, y, spriteWidth, spriteHeight, tipo, sensor);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void cargarAnimacion() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		// Caraga la animación directamente del atlas
		for (int i = 1; i < 4; i++) 
			frames.add( new TextureRegion( new Texture(Gdx.files.internal("assets/espada_tocha/" +i +".png")) , 1 , 1, 32 ,64));
		animacion = new Animation(0.15f, frames);
	}

}
