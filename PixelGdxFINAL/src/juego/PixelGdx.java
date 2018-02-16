package juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import pantallas.Inicio;
import pantallas.MapaUno;

public class PixelGdx extends Game {
	
	// Debug
	public static final boolean DEBUG = false; // Activar o desactivar la vista debug
	
	/* ATRIBUTOS ESTÁTICOS */
	public static final int WIDTH = 1240; // Width
	public static final int HEIGHT = 620; // Height
	public static final float PPM = 100f; // Pixeles por metro

	
	/* ATRIBUTOS */
	private SpriteBatch batch; // SpriteBatch ( Renderer )
	private ShapeRenderer shapeRenderer; // Para dibujar las barras de vida
	private TextureAtlas atlasSprites;	// Atlas con todos los sprites
	
	@Override
	public void create() {
		// Instancia el spriteBatch
		batch = new SpriteBatch();
		// Instancia el shapeRenderer
		shapeRenderer = new ShapeRenderer();

		// Carga el atlas con los sprites
		atlasSprites = new TextureAtlas("assets/atlas/mobs.atlas");
		
		// Carga el Mapa1
		//setScreen( new MapaUno(this) );
		setScreen( new Inicio(this));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
	}
	
	// Getters
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public TextureAtlas getAtlas() {
		return atlasSprites;
	}

}
