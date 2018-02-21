package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import juego.PixelGdx;
import otros.Espada.TiposEspada;
import util.BodyCreator;

public class OndaEspada extends Sprite {
	
	// Jugador al que pertenece la onda
	private Jugador jugador;
	
	// Cuerpo de la onda
	private Body cuerpo;
	
	// Dirección de la onda
	private boolean direccionDerecha; 
	private float tiempo; // Tiempo 				
	
	// Animacion
	private Animation animacion;
	
	// Constructor
	public OndaEspada ( Jugador jugador , TiposEspada espada , int spriteWidth , int spriteheight , int widthCuerpo , int heightCuerpo) {
		// Region del atlas correspondiente a la onda
		super(jugador.getMapa().getJuego().getAtlas().findRegion("onda"));
		this.jugador = jugador;
		
		// Animación
		cargarAnimacion(espada==TiposEspada.TOCHA?0.15f:0.07f);
		
		// Tamaño
		setBounds(0, 0, spriteWidth / PixelGdx.PPM, spriteheight / PixelGdx.PPM);
		// Frame actual
		setRegion( (TextureRegion)animacion.getKeyFrame(0f) );
		
		// Define su cuerpo en el world
		cuerpo = BodyCreator.crearCuerpoOnda(jugador.getMapa().getWorld(), 
				this, 
				jugador.getCuerpo().getPosition().x  + (jugador.enDireccionDerecha()?-0.1f:0.1f), 
				jugador.getCuerpo().getPosition().y  +(espada==TiposEspada.TOCHA?0.1f:0), widthCuerpo, heightCuerpo);
		
		// La onda se dirigirá en la dirección en la que está mirando el jugador al atacar
		direccionDerecha = jugador.enDireccionDerecha();
		
		// Impulsa la onda en la dirección a la que está mirando el jugador
		impulsarOnda();
	}
	
	// Update
	public void update ( float delta ) {
		// Lo onda solo podrá moverse en horizontal
		cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 0);
		// Posiciona el sprite en la posición del cuerpo
		setPosition( cuerpo.getPosition().x - getWidth() / 2 , cuerpo.getPosition().y - getHeight() / 2);

		// Recoge el frame actual
		TextureRegion region = getFrame( delta );
		// Lo gira si es necesario
		if ( !direccionDerecha && !region.isFlipX())
			region.flip(true, false);
		else if ( direccionDerecha && region.isFlipX() )
			region.flip(true, false);
		
		// Asigna el frame
		setRegion( region );
	}
	
	// Frame actual
	public TextureRegion getFrame ( float delta ) {
		// Suma el delta al tiempo
		tiempo += delta;
		return (TextureRegion) animacion.getKeyFrame(tiempo,false);
	}

	// Carga la animación de la onda
	private void cargarAnimacion ( float duracion ) {
		// ArrayList
		Array<TextureRegion> frames = new Array<TextureRegion>();
		// Recoge los frames de la animación
		for (int i = 0; i < 4; i++)
			frames.add( new TextureRegion(getTexture(), i * 32 +1 , 103, 32 ,32));
		// Instancia la animación
		animacion = new Animation(duracion, frames);
	}
	
	// Impulsa la onda en una dirección
	private void impulsarOnda ( ) {
		if ( direccionDerecha )
			cuerpo.applyForceToCenter(new Vector2(60f, 0),true); // Derecha
		else
			cuerpo.applyForceToCenter(new Vector2(-60f, 0),true); // Izquierda
	}

	// Comrpueba si la animación ha terminado
	public boolean animacionTerminada ( ) {
		return animacion.isAnimationFinished(tiempo);
	}
	
	// Getters
	public Body getCuerpo() {
		return cuerpo;
	}
	public Jugador getJugador ( ) {
		return jugador;
	}
}
