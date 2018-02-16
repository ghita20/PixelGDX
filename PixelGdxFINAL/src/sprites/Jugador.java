package sprites;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
	
	// Estados que puede tener el jugador
	public enum Estado { SALTANDO, CAYENDO, CORRIENDO , MUERTO , QUIETO, ATACANDO }
	private Estado estadoAnterior; 		// Almacena el estado anterior el personaje para poder saber cuanto tiempo está en cada estado
	private float tiempoEstado; 		// Cada vez que el jugador entra en un estado, almacena el tiempo que lleva en el mismo
	private boolean direccionDerecha; 	// Direccion en la que está el jugador actualmente, se utiliza para saber en que dirección estoy mirando si estoy quieto
	
	// Variables para controlar el ataque
	private boolean atacando;
	
	// Animaciones
	private Animation jugadorCorriendo; 	// Correr
	private Animation jugadorSaltando; 		// Saltar
	private Animation jugadorAtacando;		// Atacar
	private Animation jugadorAtacandoTocha;		// Atacar
	private TextureRegion jugadorQuieto; 	// Quieto
	
	// Tipos de espadas
	public enum Espada{
		BASICA , TOCHA
	}
	private Espada espada;
	
	// Poderes
	private ArrayList<OndaEspada> ondasEspada; // Ondas que genera la espada al atacar
	
	// Body
	private Body cuerpo;
	
	// Salto
	private int numeroSaltos; // Cada vez que el jugador salta aumenta el numero de saltos, al tocar con el suelo se reinicia
	private final int MAX_SALTOS = 200; // Número máximo de saltos
	
	// Vida
	public static final int MAX_VIDA = 24;
	private int puntosDeVida;
	
	// Constructor
	public Jugador ( MapaUno mapa , String personaje ) {
		
		// Carga las animaciones
		cargarAnimaciones(personaje);
		
		// Crea el cuerpo en el mundo
		cuerpo = BodyCreator.crearCuerpoJugador(mapa.getWorld(), this);
		
		// Posiciona la textura y le asigna un tamaño al jugador
		setBounds(0, 0, 128 / PixelGdx.PPM, 128 / PixelGdx.PPM);
		// Textura por defecto
		setRegion(jugadorQuieto);
		
		// Variables
		numeroSaltos = 0;
		direccionDerecha = true;
		tiempoEstado = 0;
		puntosDeVida = MAX_VIDA;
		espada = Espada.TOCHA;
		this.mapa = mapa;
		
		// Instancia el arrayList de ondas
		ondasEspada = new ArrayList<>();
	}

	// Actualiza el estado del jugador
	public void update ( float delta ) {
		// Actualiza el movimiento
		movimientoJugador();
		
		// Actualiza la posicion del sprite asignandola la del cuerpo fisico
		setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2 );
		// Asigna el frame segun el estado del personaje
		setRegion( getFrame(delta) );
		
		// Índice de las ondas que han acabado su animación
		ArrayList<Integer> indicesOndas = new ArrayList<>();
		
		// Actualiza el estado de todas las ondas del personaje
		for ( int i = ondasEspada.size() -1 ; i >= 0 ; i--) {
			// Recoge la onda
			OndaEspada onda = ondasEspada.get(i);
			// Si ha acabado su animación la destruye del world
			if ( onda.animacionTerminada() ) {
				// Añade el cuerpo a la lista de cuerpos a eliminar
				mapa.getWorldUpdate().addCuerpo(onda.getCuerpo());
				// Añade el índice para eliminarla del arrayList
				indicesOndas.add(i);
			}else
				onda.update(delta);
		}
		
		// Elimina las ondas que han acabado su animación
		for ( int i : indicesOndas )
			ondasEspada.remove(i);
	}
	
	// Draw
	@Override
	public void draw(Batch batch) {
		// Se pinta a si mismo
		super.draw(batch);
		
		// También se encarga de pintar sus ondas
		for ( OndaEspada onda : ondasEspada)
			onda.draw(batch);
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
			atacar();
		}
		
		// Cambiar de espada
		if ( Gdx.input.isKeyJustPressed( Input.Keys.E )) {
			if ( espada == Espada.BASICA)
				espada = Espada.TOCHA;
			else
				espada = Espada.BASICA;
		}
		
		
	}

	// Ataca lanzando una onda
	private void atacar() {
		// Crea una nueva onda y la añade al arrayList
		ondasEspada.add( new OndaEspada(this) );
		// Atacando
		atacando = true;
		tiempoEstado = 0;
	}

	// Devuelve el estado del jugador
	public Estado getEstado() {
		// Atacando - el ataque tiene prioridad sobre todas las demás animaciones
		if ( atacando )
			if ( jugadorAtacando.isAnimationFinished(tiempoEstado) ) // si ha acabado la animación cambia el estado del booleano
				atacando = false;
			else
				return Estado.ATACANDO;
		
		// Saltando
		if ( Gdx.input.isKeyJustPressed(Input.Keys.W) || cuerpo.getLinearVelocity().y > 0 && estadoAnterior == Estado.SALTANDO ) 
			return Estado.SALTANDO;
		
		// Cayendo
		if ( cuerpo.getLinearVelocity().y < 0 )
			return Estado.CAYENDO;
		
		// Corriendo
		if ( cuerpo.getLinearVelocity().x != 0 )
			return Estado.CORRIENDO;
		
		return Estado.QUIETO;
	}
	
	// Devuelve el frame actual del jugador
	public TextureRegion getFrame ( float delta ) {
		// Recupera el estado actual
		Estado estadoActual = getEstado();
		
		// Textura actual del jugador
		TextureRegion region = null;
		
		switch( estadoActual ) {
		// Corriendo
		case CORRIENDO: 
			region = (TextureRegion) jugadorCorriendo.getKeyFrame(tiempoEstado, true);
			break;
		// Quieto o Cayendo
		case QUIETO: case CAYENDO:
			region = jugadorQuieto;
			break;
		// Saltando	
		case SALTANDO:
			region = (TextureRegion) jugadorSaltando.getKeyFrame(tiempoEstado);
			break;
		// Atacando
		case ATACANDO:
			if ( espada == Espada.BASICA)
				region = (TextureRegion) jugadorAtacando.getKeyFrame(tiempoEstado);
			else
				region = (TextureRegion) jugadorAtacandoTocha.getKeyFrame(tiempoEstado);
			break;
		// TODO: Muerto
		case MUERTO:
			region = jugadorQuieto;
			break;
		}
		
		// Según la dirección a la que se dirija el jugador, invertirá la textura
		if ( (cuerpo.getLinearVelocity().x < 0 || !direccionDerecha) && !region.isFlipX()) {
			region.flip(true, false);
			// Cambia la dirección del jugador 
			direccionDerecha = false;
		}else if ( (cuerpo.getLinearVelocity().x > 0 || direccionDerecha) && region.isFlipX() ) {
			region.flip(true, false);
			// Cambia la dirección del jugador 
			direccionDerecha = true;
		}
			
		// Si está en el mismo estado que el anterior aumenta el tiempo que lleva en ese estado si no, lo reinicia a 0
		tiempoEstado = estadoActual == estadoAnterior ? tiempoEstado + delta : 0; 
		// El estado anterior se convierte en el estado actual
		estadoAnterior = estadoActual;
		
		// Devuelve la textura
		return region;
	}
	
	// Carga las animaciones
	private void cargarAnimaciones( String personaje  ) {
		
		// ArrayList auxiliar para cargar los frames
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		// CORRER
		for (int i = 1; i <= 4; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/corriendo/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animación
		jugadorCorriendo = new Animation<>(0.1f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// SALTAR
		for (int i = 1; i <= 3; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/saltando/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animación
		jugadorSaltando = new Animation<>(0.2f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// ATACAR
		
		// Animación espada básica
		for (int i = 1; i <= 4; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/atacando/basica/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animación
		jugadorAtacando = new Animation<>(0.07f, frames);
		frames.clear();

		// Espada tocha
		for (int i = 1; i <= 5; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/atacando/tocha/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animación
		jugadorAtacandoTocha = new Animation<>(0.07f, frames);
		
		
		
		// Textura normal
		jugadorQuieto = new TextureRegion( new Texture("assets/" +personaje + "/corriendo/" +1 +".png"), 1, 1 , 128,128);
	}

	// Getters
	public Body getCuerpo() {
		return cuerpo;
	}
	public boolean enDireccionDerecha ( ) {
		return direccionDerecha;
	}
	public MapaUno getMapa() {
		return mapa;
	}
	public int getPuntosDeVida() {
		return puntosDeVida;
	}
	
	// Otros métodos
	public void reiniciarSaltos ( ) {
		numeroSaltos = 0;
	}
	public void setEspada ( Espada espada ) {
		this.espada = espada;
	}
}
