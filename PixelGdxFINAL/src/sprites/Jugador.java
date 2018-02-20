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
import util.GestionAudio;

public class Jugador extends Sprite{
	
	private MapaUno mapa; // Mapa actual
	
	// Estados que puede tener el jugador
	public enum Estado { SALTANDO, CAYENDO, CORRIENDO , MUERTO , QUIETO, ATACANDO, ATURDIDO }
	private Estado estadoAnterior; 		// Almacena el estado anterior el personaje para poder saber cuanto tiempo est� en cada estado
	private float tiempoEstado; 		// Cada vez que el jugador entra en un estado, almacena el tiempo que lleva en el mismo
	private boolean direccionDerecha; 	// Direccion en la que est� el jugador actualmente, se utiliza para saber en que direcci�n estoy mirando si estoy quieto
	
	// Variables para controlar algunos estados
	private boolean atacando;
	private boolean aturdido; 
	private boolean muerto;
	
	// Animaciones
	private Animation jugadorCorriendo; 	// Correr
	private Animation jugadorSaltando; 		// Saltar
	private Animation jugadorAtacando;		// Atacar
	private Animation jugadorAtacandoTocha;		// Atacar
	private Animation jugadorAturdido;		// Aturdir
	private TextureRegion jugadorQuieto; 	// Quieto
	private TextureRegion jugadorMuerto; 	// Muerto
	
	// Tipos de espadas
	public enum Espada{
		BASICA , TOCHA
	}
	private int DA�O_ESPADA_BASICA = 1;
	private int DA�O_ESPADA_TOCHA = 4;
	private Espada espada;
	
	// Poderes
	private ArrayList<OndaEspada> ondasEspada; // Ondas que genera la espada al atacar
	
	// Body
	private Body cuerpo;
	
	// Salto
	private int numeroSaltos; // Cada vez que el jugador salta aumenta el numero de saltos, al tocar con el suelo se reinicia
	private final int MAX_SALTOS = 2; // N�mero m�ximo de saltos
	
	// Vida
	public static final int MAX_VIDA = 24;
	private int puntosDeVida;
	
	// Monedas
	private int cantidadMonedas; // N�mero de monedas del jugador
	
	// Constructor
	public Jugador ( MapaUno mapa , String personaje ) {
		
		// Carga las animaciones
		cargarAnimaciones(personaje);
		
		// Crea el cuerpo en el mundo
		cuerpo = BodyCreator.crearCuerpoJugador(mapa.getWorld(), this);
		
		// Posiciona la textura y le asigna un tama�o al jugador
		setBounds(0, 0, 128 / PixelGdx.PPM, 128 / PixelGdx.PPM);
		// Textura por defecto
		setRegion(jugadorQuieto);
		
		// Variables
		numeroSaltos = 0;
		direccionDerecha = true;
		tiempoEstado = 0;
		puntosDeVida = MAX_VIDA;
		espada = Espada.TOCHA;
		muerto = false;
		this.mapa = mapa;
		cantidadMonedas = 0;
		
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
		
		// �ndice de las ondas que han acabado su animaci�n
		ArrayList<Integer> indicesOndas = new ArrayList<>();
		
		// Actualiza el estado de todas las ondas del personaje
		for ( int i = ondasEspada.size() -1 ; i >= 0 ; i--) {
			// Recoge la onda
			OndaEspada onda = ondasEspada.get(i);
			// Si ha acabado su animaci�n la destruye del world
			if ( onda.animacionTerminada() ) {
				// A�ade el cuerpo a la lista de cuerpos a eliminar
				mapa.getWorldUpdate().addCuerpo(onda.getCuerpo());
				// A�ade el �ndice para eliminarla del arrayList
				indicesOndas.add(i);
			}else
				onda.update(delta);
		}
		
		// Elimina las ondas que han acabado su animaci�n
		for ( int i : indicesOndas )
			ondasEspada.remove(i);
	}
	
	// Draw
	@Override
	public void draw(Batch batch) {
		// Se pinta a si mismo
		super.draw(batch);
		
		// Tambi�n se encarga de pintar sus ondas
		for ( OndaEspada onda : ondasEspada)
			onda.draw(batch);
	}
	
	// Gestiona el movimiento del jugador
	private void movimientoJugador() {
		// Si est� muerto ya no se podr� controlar al jugador
		if ( !muerto ) {
			// Salto
			if (Gdx.input.isKeyJustPressed(Input.Keys.W)  && numeroSaltos < MAX_SALTOS  ) { 
				cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 0);
				cuerpo.applyForceToCenter(0, 150f, true);
				
				// Aumenta el n�mero de saltos
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
			
			// Pruebas
			if ( Gdx.input.isKeyJustPressed( Input.Keys.Q )) {
				mapa.getJugadores().getStatsJugador().mostrarDialogoNpc();
			}
		}
		
	}

	// Ataca lanzando una onda
	private void atacar() {
		// Crea una nueva onda y la a�ade al arrayList. El tama�o de la onda var�a seg�n la espada
		ondasEspada.add( new OndaEspada(this, 
				espada == Espada.TOCHA?42:32 , 
				espada == Espada.TOCHA?42:32 ,
				espada == Espada.TOCHA?10:8,
				espada == Espada.TOCHA?12:10
		));
		// Atacando
		atacando = true;
		tiempoEstado = 0;
		// Sonido
		GestionAudio.SONIDO_ESPADA.play();
	}
	// Aturde al jugador por un tiempo
	public void aturdir ( ) {
		// Aturdir
		aturdido = true;
		tiempoEstado = 0;
	}

	// Devuelve el estado del jugador
	public Estado getEstado() {
		// Primero se comprueba si est� muerto
		if ( muerto )
			return Estado.MUERTO;
		// El aturdimiento tiene prioridad sobre el ataque
		if ( aturdido ) {
			if ( jugadorAturdido.isAnimationFinished(tiempoEstado) )
				aturdido = false;
			else
				return Estado.ATURDIDO;
		}
		// Atacando - el ataque tiene prioridad sobre las dem�s animaciones
		if ( atacando )
			if ( jugadorAtacando.isAnimationFinished(tiempoEstado) ) // si ha acabado la animaci�n cambia el estado del booleano
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
			region = jugadorMuerto;
			break;
		// Aturdido
		case ATURDIDO:
			region = (TextureRegion) jugadorAturdido.getKeyFrame(tiempoEstado);
			break;
		}
		
		// Seg�n la direcci�n a la que se dirija el jugador, invertir� la textura
		if ( (cuerpo.getLinearVelocity().x < 0 || !direccionDerecha) && !region.isFlipX()) {
			region.flip(true, false);
			// Cambia la direcci�n del jugador 
			direccionDerecha = false;
		}else if ( (cuerpo.getLinearVelocity().x > 0 || direccionDerecha) && region.isFlipX() ) {
			region.flip(true, false);
			// Cambia la direcci�n del jugador 
			direccionDerecha = true;
		}
			
		// Si est� en el mismo estado que el anterior aumenta el tiempo que lleva en ese estado si no, lo reinicia a 0
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
		// Instancia la animaci�n
		jugadorCorriendo = new Animation<>(0.1f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// SALTAR
		for (int i = 1; i <= 3; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/saltando/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animaci�n
		jugadorSaltando = new Animation<>(0.2f, frames);
		// Limpia el arrayList
		frames.clear();
		
		// ATACAR
		
		// Animaci�n espada b�sica
		for (int i = 1; i <= 4; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/atacando/basica/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animaci�n
		jugadorAtacando = new Animation<>(0.07f, frames);
		frames.clear();

		// Espada tocha
		for (int i = 1; i <= 5; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/atacando/tocha/" +i +".png") , 1 , 1, 128 ,128));
		// Instancia la animaci�n
		jugadorAtacandoTocha = new Animation<>(0.07f, frames);
		frames.clear();
		
		
		// Jugador Aturdido
		for (int i = 1; i <= 3; i++) 
			frames.add( new TextureRegion( new Texture("assets/" +personaje + "/aturdido.png") , 1 , 1, 128 ,128));
		// Animacion
		jugadorAturdido = new Animation(0.05f, frames);
		
		// Quieto
		jugadorQuieto = new TextureRegion( new Texture("assets/" +personaje + "/corriendo/" +1 +".png"), 1, 1 , 128,128);
		
		// Jugador muerto
		jugadorMuerto = new TextureRegion( new Texture("assets/" +personaje + "/muerto.png"), 1, 1 , 128,128);
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
	public boolean estaMuerto ( ) {
		return muerto;
	}
	public int getPuntosAtaque ( ) {
		// El ataque var�a seg�n el tipo de espada
		if ( espada == Espada.TOCHA )
			return DA�O_ESPADA_TOCHA;
		return DA�O_ESPADA_BASICA;
	}
	public int getCantidadMonedas() {
		return cantidadMonedas;
	}
	
	// Otros m�todos
	public void reiniciarSaltos ( ) {
		numeroSaltos = 0;
	}
	public void setEspada ( Espada espada ) {
		this.espada = espada;
	}
	public void restarVida ( int da�o ) {
		if ( !muerto )
			puntosDeVida -= da�o;
		if ( puntosDeVida <= 0 )
			muerto = true; // Muerto
	}
	public void addMoneda ( ) {
		cantidadMonedas++;
	}
	// Mejora el da�o de la espada
	public void mejorarEspada ( Espada tipoEspada ) {
		if ( tipoEspada == Espada.TOCHA )
			DA�O_ESPADA_TOCHA += 2;
		if ( tipoEspada == Espada.BASICA )
			DA�O_ESPADA_BASICA +=2;
	}
	
	// Resta monedas
	public void restarMonedas ( int monedasRestar ) throws IllegalArgumentException{
		if ( monedasRestar - cantidadMonedas < 0 )
			throw new IllegalArgumentException("No tienes las monedas suficientes.");
		cantidadMonedas -= monedasRestar;
		if ( cantidadMonedas < 0 )
			cantidadMonedas = 0;
	}
	
	
}
