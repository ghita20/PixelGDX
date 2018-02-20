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
	private static final float VELOCIDAD = 0.4f;
	
	// Mapa
	protected MapaUno mapa;
	
	// Cuerpo
	protected Body cuerpo;

	// Atributos
	protected int puntosDeVida;
	protected boolean muerto;
	protected int daño; // Daño que hace el enemigo al atacar
	
	// Animaciones
	protected Animation animacionMovimiento;
	protected Animation animacionAturdido;
	protected boolean aturdido;
	protected float tiempo; // Tiempo de la animación
	
	// Dirección en la que se dirige
	protected boolean direccionDerecha;
	
	// Constructor
	public Enemigo ( MapaUno mapa, float x , float y , float width , float height , float spriteWidth , float spriteHeight , int vida , int daño ) {
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
		muerto = false;
		puntosDeVida = vida;
		this.daño = daño;
	}
	
	// Update
	public void update ( float delta ) {
		// Mueve el enemigo si no está muerto
		if ( !muerto )
			mover();
		
		// Comprueba si se ha acabado el estado de aturdimiento
		if ( aturdido )
			if ( animacionAturdido.isAnimationFinished(tiempo) )
				aturdido = false;
		
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
		// Aumenta el tiempo de la animación
		tiempo += delta;
		// Si está aturdido devuelve el frame de aturdimiento
		if ( aturdido )
			return (TextureRegion)animacionAturdido.getKeyFrame(tiempo);
		// Por defecto devuelve la animación de movimiento
		return (TextureRegion) animacionMovimiento.getKeyFrame(tiempo, true);
	}
	
	// Movimiento por defecto del enemigo ( se moverá entre los límites puestos en el tileMap )
	protected void mover() { 
		// Impulsará al enemigo según su posición
		cuerpo.applyLinearImpulse(new Vector2( direccionDerecha?0.1f: -0.1f,0 ), cuerpo.getWorldCenter(), true);
	}
	
	// Cambia el movimiento del enemigo
	public void cambiarDireccion ( boolean enDireccionDerecha ) {
		direccionDerecha = enDireccionDerecha;
		cuerpo.applyLinearImpulse(new Vector2( direccionDerecha?3.3f: -3.3f,0 ), cuerpo.getWorldCenter(), true);
	}
	
	// Ataca a un jugador
	public void atacarJugador ( Jugador jugador ) {
		// Asigna la velocidad lineal del jugador a 0
		jugador.getCuerpo().setLinearVelocity( new Vector2(0,0));
		// Ataca impulsandolo
		jugador.getCuerpo().applyLinearImpulse(new Vector2( direccionDerecha?1f:-1f,1.5f), jugador.getCuerpo().getWorldCenter(), true);
		// Aturde al jugador
		jugador.aturdir();
		
		// Resta vida al jugador
		jugador.restarVida(daño);
	}
	
	// Restar vida
	public void restarVida ( int daño ) {
		if ( !muerto ) {
			puntosDeVida -= daño;
			// Al atacar aturde al enemigo para mostrar el frame con el enemigo aturdido
			aturdido = true;
			tiempo = 0;
		}
		if ( puntosDeVida <= 0 )
			muerto = true;
	}
	
	// Métodos abstractos
	protected abstract void cargarAnimacionMovimiento();
	
	// Getters
	public Body getCuerpo() {
		return cuerpo;
	}
	public int getPuntosDeVida() {
		return puntosDeVida;
	}
	public boolean getMuerto ( ) {
		return muerto;
	}

}
