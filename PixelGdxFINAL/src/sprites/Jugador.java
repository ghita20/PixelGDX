package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import juego.PixelGdx;
import pantallas.MapaUno;
import util.BodyCreator;

public class Jugador extends Sprite{
	private MapaUno mapa; // Mapa actual
	
	// Animaciones
	private Animation jugadorCorriendo; // Correr
	private Animation jugadorSaltando; 	// Saltar
	private Animation jugadorAtacando;	// Atacar
	private TextureRegion jugadorQuieto; // Quieto
	
	// Body
	private Body cuerpo;
	
	// Salto
	private int numeroSaltos; // Cada vez que el jugador salta aumenta el numero de saltos, al tocar con el suelo se reinicia
	private final int MAX_SALTOS = 2; // Número máximo de saltos
	
	// Constructor
	public Jugador ( MapaUno mapa , String personaje , int regionY ) {
		// Carga la region del personaje del atlas
		super ( mapa.getJuego().getAtlas().findRegion(personaje) );
		
		// Carga las animaciones
		cargarAnimaciones(personaje,regionY);
		
		// Crea el cuerpo en el mundo
		cuerpo = BodyCreator.crearCuerpoJugador(mapa.getWorld(), this);
		
		// Posiciona la textura y le asigna un tamaño al jugador
		setBounds(0, 0, 34 / PixelGdx.PPM, 34 / PixelGdx.PPM);
		// Textura por defecto
		setRegion(jugadorQuieto);
		
		// Variables
		numeroSaltos = 0;
	}

	// Actualiza el estado del jugador
	public void update ( float delta ) {
		// Actualiza el movimiento
		movimientoJugador();
		
		// Actualiza la posicion del sprite asignandola la del cuerpo fisico
		setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2 );
		// Asigna el frame segun el estado del personaje
		setRegion( getFrame(delta) );
	}
	
	// Gestiona el movimiento del jugador
	private void movimientoJugador() {
		
		// Salto
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)  && numeroSaltos < MAX_SALTOS  ) { 
			cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 0);
			cuerpo.applyForceToCenter(0, 150f, true);
			
			// Aumenta el número de saltos
			numeroSaltos++;
		}
		
		// Abajo
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			cuerpo.applyLinearImpulse(new Vector2(0, -2.4f), cuerpo.getWorldCenter(), true);
		
		// Movimiento Derecha
		if (Gdx.input.isKeyPressed(Input.Keys.D) && cuerpo.getLinearVelocity().x <= 1.0f) {
			float velX = cuerpo.getLinearVelocity().x;
			if ( velX < 0 )
				cuerpo.setLinearVelocity(0, cuerpo.getLinearVelocity().y);
			cuerpo.applyForceToCenter(new Vector2(3.5f, 0), true);
		}
		
		// Movimiento izquierda
		if (Gdx.input.isKeyPressed(Input.Keys.A) && cuerpo.getLinearVelocity().x >= -1.0f) {
			float velX = cuerpo.getLinearVelocity().x;
			if ( velX > 0 )
				cuerpo.setLinearVelocity(0, cuerpo.getLinearVelocity().y);
			cuerpo.applyForceToCenter(new Vector2(-3.5f, 0), true);
		}
		
		// Ataque
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			
		}
		
		
	}

	// Devuelve el frame actual del jugador
	public TextureRegion getFrame ( float delta ) {
		return jugadorQuieto;
	}
	
	// Carga las animaciones
	private void cargarAnimaciones( String personaje , int regionY ) {
		
		// Tamaño del sprite ( El sprite trebol es un poco mas grande asi que hay que reducirlo un poco )
		int width = personaje.equals("trebol")?36:32;
		int height = personaje.equals("trebol")?36:32;
		
		// ArrayList auxiliar para cargar los frames
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		// CORRER
		for (int i = 0; i < 4; i++) 
			frames.add( new TextureRegion(getTexture(), i * 32 , regionY, width ,height));
		// Instancia la animación
		jugadorCorriendo = new Animation(0.1f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// SALTAR
		for (int i = 4; i < 7; i++) 
			frames.add( new TextureRegion(getTexture(), i * 32 , regionY, width ,height));
		// Instancia la animación
		jugadorSaltando = new Animation(0.2f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// ATACAR
		frames.add( new TextureRegion(getTexture(), 11 * 32 , regionY, width ,height));
		frames.add( new TextureRegion(getTexture(), 8 * 32 , regionY, width ,height));
		frames.add( new TextureRegion(getTexture(), 12 * 32 , regionY, width ,height));
		frames.add( new TextureRegion(getTexture(), 12 * 32 , regionY, width ,height));
		// Instancia la animación
		jugadorAtacando = new Animation(0.08f, frames);
		
		
		// Textura normal
		jugadorQuieto = new TextureRegion( getTexture(), 1, regionY , width,height);
		
	}

	// Getters
	public Body getCuerpo() {
		return cuerpo;
	}
	
	// Otros métodos
	public void reiniciarSaltos ( ) {
		numeroSaltos = 0;
	}
}
