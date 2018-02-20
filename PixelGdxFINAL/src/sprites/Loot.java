package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import juego.PixelGdx;
import pantallas.MapaUno;
import util.BodyCreator;

public abstract class Loot extends Sprite{
	// Cuerpo
	private Body cuerpo;
	
	// Animacion
	protected Animation animacion;
	private float tiempo;
	
	// Centinela para comprobar si el loot ha sido cogido o no
	private boolean cogido;
	
	// Constructor
	public Loot ( MapaUno mapa , float x , float y , float spriteWidth , float spriteHeight, BodyType tipo) {
		// Cuerpo 
		cuerpo = BodyCreator.crearCuerpoLoot(mapa.getWorld(), this, x, y, 8, 8, tipo);
		// Carga la animación
		cargarAnimacion();
		// Tiempo animación
		tiempo = 0;
		// Tamaño en la pantalla
		setBounds(0, 0, spriteWidth / PixelGdx.PPM, spriteHeight / PixelGdx.PPM);
		// Region por defecto
		setRegion( (TextureRegion)animacion.getKeyFrame(0f)  );
		// por defecto no estña cogido
		cogido = false;
	}

	// Update
	public void update ( float delta ) {
		// Posiciona el sprite
		setPosition( cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2);
		// Asigna el frame según su estado
		TextureRegion region = getFrame(delta);
		setRegion(region);
	}
	
	// Get Frame
	public TextureRegion getFrame ( float delta ) {
		// Aumenta el tiempo de la animación
		tiempo += delta;
		// Devuelve el keyframe
		return (TextureRegion) animacion.getKeyFrame(tiempo, true); // En bucle
	}
		
	// Método abstractos
	protected abstract void cargarAnimacion();

	
	// Getters y setters
	public boolean getCogido ( ) {
		return cogido;
	}
	public void setCogido ( boolean cogido ) {
		this.cogido = cogido;
	}
	public Body getCuerpo() {
		return cuerpo;
	}
}
