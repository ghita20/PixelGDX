package juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import pantallas.Inicio;
import sockets.PixelSocket;
import sockets.SocketCliente;
import sockets.SocketServidor;
import util.GestionAudio;

public class PixelGdx extends Game {
	
	// Debug
	public static boolean DEBUG = false; // Activar o desactivar la vista debug
	
	/* ATRIBUTOS EST�TICOS */
	public static final int WIDTH = 1240; // Width
	public static final int HEIGHT = 620; // Height
	public static final float PPM = 100f; // Pixeles por metro

	
	/* ATRIBUTOS */
	private SpriteBatch batch; // SpriteBatch ( Renderer )
	private ShapeRenderer shapeRenderer; // Para dibujar las barras de vida
	private TextureAtlas atlasSprites;	// Atlas con todos los sprites
	
	// Socket
	private PixelSocket conexionSocket;
	
	@Override
	public void create() {
		// Instancia el spriteBatch
		batch = new SpriteBatch();
		// Instancia el shapeRenderer
		shapeRenderer = new ShapeRenderer();
		// La conexi�n se realiza mas adelante
		conexionSocket = null;

		// Carga el atlas con los sprites
		atlasSprites = new TextureAtlas(Gdx.files.internal("assets/atlas/mobs.atlas")); 

		// Musica de fondo
		GestionAudio.MUSICA_FONDO.play();
		
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
	
	// Seg�n el booleano que se le pase como par�metro instanciar� el socket en modo cliete o servidor
	public void setConexionSocket(boolean esServidor) {
		// Instancia el socket
		conexionSocket = esServidor? new SocketServidor() : new SocketCliente(SocketServidor.IP, SocketServidor.PUERTO);
	}
	
	// Getters
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public TextureAtlas getAtlas() {
		return atlasSprites;
	}
	
	public PixelSocket getConexionSocket() {
		return conexionSocket;
	}

}
