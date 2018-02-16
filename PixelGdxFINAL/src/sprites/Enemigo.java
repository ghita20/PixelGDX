package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import juego.PixelGdx;
import pantallas.MapaUno;
import util.BodyCreator;

public abstract class Enemigo extends Sprite {
	
	// Atributos estáticos
	private static final int MAX_VIDA = 100;
	private static final float VELOCIDAD = 0.4f;
	
	// Mapa
	protected MapaUno mapa;
	
	// Cuerpo
	protected Body cuerpo;

	// Atributos
	protected int puntosDeVida;
	
	// Animaciones
	protected Animation animacionMovimiento;
	protected float tiempo; // Tiempo de la animación
	
	// Dirección en la que se dirige
	protected boolean direccionDerecha;
	
	// Constructor
	public Enemigo ( MapaUno mapa, float x , float y , float width , float height , float spriteWidth , float spriteHeight ) {
		// Mapa
		this.mapa = mapa;
		// Cuerpo del enemigo
		cuerpo = BodyCreator.crearCuerpoEnemigo(mapa.getWorld(), this, x, y, 8, 10);
		
		// Carga la animación de movimiento
		cargarAnimacionMovimiento();
		
		// Tamaño en la pantalla
		setBounds(0, 0, spriteWidth / PixelGdx.PPM, spriteHeight / PixelGdx.PPM);
		
		// Primer keyFrame que se dibuja
		setRegion( (TextureRegion)animacionMovimiento.getKeyFrame(0f) );
		
		// Variables
		tiempo = 0;
		direccionDerecha = true;
	}
	
	// Update
	public void update ( float delta ) {
		// Mueve el enemigo
		mover();
		
		// Posiciona el sprite
		setPosition( cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2);
		// Asigna el frame según su estado
		TextureRegion region = getFrame(delta);
		
		// Comprueba si es necesario invertir el frame
		if ( (direccionDerecha && region.isFlipX()) || (!direccionDerecha && !region.isFlipX()) )
			region.flip(true, false);
		
		// Comprueba que la velocidad no supere el máximo establecido
		if ( cuerpo.getLinearVelocity().x > VELOCIDAD ) { // Derecha
			cuerpo.setLinearVelocity(new Vector2(0,cuerpo.getLinearVelocity().y));
		}else if ( cuerpo.getLinearVelocity().x < 0 ) 
			if ( cuerpo.getLinearVelocity().x*-1 > VELOCIDAD ) // Izquierda
				cuerpo.setLinearVelocity(new Vector2(0,cuerpo.getLinearVelocity().y));
		
		// Asigna el frame a imprimir
		setRegion( region );
	}
	
	// Get Frame
	public TextureRegion getFrame ( float delta ) {
		tiempo += delta;
		// Por defecto devuelve la animación de movimiento
		return (TextureRegion) animacionMovimiento.getKeyFrame(tiempo, true);
	}
	
	// Movimiento por defecto del enemigo ( se moverá entre los límites puestos en el tileMap )
	protected void mover() { 
		// Impulsará al enemigo según su posición
		if ( (cuerpo.getLinearVelocity().x < 0.5f && direccionDerecha) || (cuerpo.getLinearVelocity().x > -0.5f && !direccionDerecha) )
			cuerpo.applyLinearImpulse(new Vector2( direccionDerecha?0.1f:-0.1f , 0 ), cuerpo.getWorldCenter(), true);
	}
	
	
	// Métodos abstractos
	protected abstract void cargarAnimacionMovimiento();
	
	

}
