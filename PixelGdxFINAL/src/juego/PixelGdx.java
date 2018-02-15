package juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import pantallas.MapaUno;

public class PixelGdx extends Game {
	
	// Debug
	public static final boolean DEBUG = false; // Activar o desactivar la vista debug
	
	/* ATRIBUTOS ESTÁTICOS */
	public static final int WIDTH = 1240; // Width
	public static final int HEIGHT = 620; // Height
	public static final float PPM = 100f; // Pixeles por metro

	// CategoryBits - categorias de los distintos cuerpos del juego
	public static final short CATEGORIA_JUGADOR = 0x0001;
	public static final short CATEGORIA_ENEMIGO = 0x0002;
	public static final short CATEGORIA_ESCENARIO = 0x0004;
	public static final short CATEGORIA_PODERES = 0x0008;
	public static final short CATEGORIA_LIMITE = 0x0016;
	public static final short CATEGORIA_ESPADA = 0x0032;

	// Masks - cada máscara indica con qué tipo de categorias colisiona
	public static final short MASK_JUGADOR = CATEGORIA_ENEMIGO | CATEGORIA_ESCENARIO;
	public static final short MASK_ENEMIGO = CATEGORIA_JUGADOR | CATEGORIA_ESCENARIO | CATEGORIA_PODERES ;
	public static final short MASK_PODERES = CATEGORIA_ENEMIGO;
	public static final short MASK_LIMITES = CATEGORIA_ENEMIGO;
	public static final short MASK_ESCENEARIO = -1; // colisiona con todo
	
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
		setScreen( new MapaUno(this) );
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
