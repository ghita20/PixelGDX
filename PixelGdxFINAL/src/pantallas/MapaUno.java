package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import juego.PixelGdx;
import util.Colisiones;
import util.GestionJugadores;
import util.GestionMobs;
import util.GestionesMapaUno;
import util.WorldUpdate;

public class MapaUno implements Screen {
	
	// Referencia al juego principal
	private PixelGdx juego; 
	
	// Cámara
	private OrthographicCamera gameCam; // Camara principal
	private Viewport viewPort;  		// Gestiona como renderizar sobre la ventana segun su tamaño etc..
	
	// Gestiones
	private GestionesMapaUno mapa;		// Mapa
	private GestionJugadores jugadores;	// Jugadores
	private GestionMobs mobs;			// Mobs
	
	// Mundo
	private World world; 				// Esta clase gestiona las físicas
	private WorldUpdate worldUpdate;	// Gestiona el update del world y la eliminación de cuerpos
	private Box2DDebugRenderer debugRender; // Imprime los límites de cada objeto que hay en el world
	private final float GRAVEDAD = -5; 	// Gravedad
	
	// Constructor
	public MapaUno ( PixelGdx juego ) {
		// Juego
		this.juego = juego;

        // World - fisica
        world = new World( new Vector2( 0 , GRAVEDAD), true);
        world.setContactListener( new Colisiones() ); // Gestor de colisiones
		
		// Instancia la camara principal
		gameCam = new OrthographicCamera();

		// ViewPort -- Se divide el ancho y el alto entre 1.8 para hacer el efecto zoom
		viewPort = new FitViewport(PixelGdx.WIDTH / PixelGdx.PPM / 1.8f, PixelGdx.HEIGHT / PixelGdx.PPM / 1.8f, gameCam);

        // Genera el mapa ( tileMap )
        mapa = new GestionesMapaUno(this);
        
		// Posiciona la camara centrada en el principio del mapa
        gameCam.position.set(viewPort.getWorldWidth() / 2f, viewPort.getWorldHeight() / 2f, 0);
        gameCam.setToOrtho(false, PixelGdx.WIDTH / PixelGdx.PPM / 1.8f, PixelGdx.HEIGHT / PixelGdx.PPM / 1.8f);
        gameCam.update();
        
        // Gestion jugadores
        jugadores = new GestionJugadores(this);
        
        // Gestion mobs
        mobs = new GestionMobs(this,mapa.getMap());
        
        // Debug Render
        debugRender = new Box2DDebugRenderer();
        
        // World Update
        worldUpdate = new WorldUpdate(world);
	}
	
	public void update( float delta ) {
		// Actualiza el estado del mundo y elimina los cuerpos que tiene pendientes de eliminar
		worldUpdate.update();
		
		// Actualizo los jugadores
		jugadores.update(delta);
		
		// Actualiza los mobs
		mobs.update(delta);
		
		// Actualiza la camara para que siga al jugador
		updateCam(delta,jugadores.getJugadorPrincipal().getCuerpo().getPosition().x,jugadores.getJugadorPrincipal().getCuerpo().getPosition().y);

		// Actualiza el renderer del mapa
		mapa.update(gameCam);
	}
	
	// Actualiza el estado de la cámara
	private void updateCam(float delta , float x , float y) {
		// Límites y de la cámara
		if (y >= GestionesMapaUno.HEIGHT - gameCam.viewportHeight / 2 )
			y = GestionesMapaUno.HEIGHT - gameCam.viewportHeight / 2;
		if (y < gameCam.viewportHeight / 2)
			y = gameCam.viewportHeight / 2;
		
		// Límites x de la cámara
		if (x >= GestionesMapaUno.WIDTH - gameCam.viewportWidth / 2 )
			x = GestionesMapaUno.WIDTH - gameCam.viewportWidth / 2;
		// Lado izquierdo
		if (x < gameCam.viewportWidth / 2)
			x = gameCam.viewportWidth / 2;
		
		// A donde se dirige la cámara
        Vector3 target = new Vector3(x,y,0); 
        // Le da un toque suave al movimiento de la cámara al seguir al jugador
		gameCam.position.lerp(target, 0.04f);
		// Update
		gameCam.update();
		
	}

	@Override
	public void render( float delta ) {
		// Actualiza todo
		update(delta);
		
		// Limpia la pantalla con un negro xD
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		// Renderiza el mapa
		mapa.render();
		
		// Debug
		if ( PixelGdx.DEBUG )
			debugRender.render(world, gameCam.combined);
		
        // Le diche al SpriteBatch que es lo que tiene que pintar segun donde estemos con la camara
        juego.getBatch().setProjectionMatrix(gameCam.combined);
        
        // Empieza el renderizado
        juego.getBatch().begin();
        
	        
	        // Renderiza los mobs
	        mobs.render(juego.getBatch());
	        

			// Renderiza los jugadores ( Hay que dejar que lo último que se renderize sea el jugador porque el se encargará de cerrar el batch )
	        jugadores.render( juego.getBatch() );
        
		// Termina el renderizado ( lo termina el jugador en su render porque  )
        //juego.getBatch().end();
        
	}
	
	
	
	
	
	
	
	
	
	
	
	// Getters
	public PixelGdx getJuego() {
		return juego;
	}
	
	public World getWorld() {
		return world;
	}
	
	public WorldUpdate getWorldUpdate() {
		return worldUpdate;
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
