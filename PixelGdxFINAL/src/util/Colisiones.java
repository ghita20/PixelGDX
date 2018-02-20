package util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import juego.PixelGdx;
import sprites.Enemigo;
import sprites.Jugador;
import sprites.Loot;
import sprites.Moneda;
import sprites.Murcielago;
import sprites.OndaEspada;

public class Colisiones implements ContactListener{

	// CategoryBits - categorias de los distintos cuerpos del juego
	public static final short CATEGORIA_JUGADOR = 0x0001;
	public static final short CATEGORIA_ENEMIGO = 0x0002;
	public static final short CATEGORIA_ESCENARIO = 0x0004;
	public static final short CATEGORIA_PODERES = 0x0008;
	public static final short CATEGORIA_LIMITE = 0x0016;
	public static final short CATEGORIA_ESPADA = 0x0032;
	public static final short CATEGORIA_LOOT = 0x0064;

	// Masks - cada máscara indica con qué tipo de categorias colisiona
	public static final short MASK_JUGADOR = CATEGORIA_ENEMIGO | CATEGORIA_ESCENARIO;
	public static final short MASK_ENEMIGO = CATEGORIA_JUGADOR | CATEGORIA_ESCENARIO | CATEGORIA_PODERES ;
	public static final short MASK_LOOT = CATEGORIA_JUGADOR | CATEGORIA_ESCENARIO ;
	public static final short MASK_PODERES = CATEGORIA_ENEMIGO;
	public static final short MASK_LIMITES = CATEGORIA_ENEMIGO;
	public static final short MASK_ESCENEARIO = -1; // colisiona con todo
	
	@Override
	public void beginContact(Contact contact) {
		// Recogemos los Fixture de los dos objetos que han entrado en contacto
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		// Juntamos las categorias
		int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
		switch ( cDef ) {
		// Choque entre jugador y escenario
		case CATEGORIA_JUGADOR | CATEGORIA_ESCENARIO :
			Jugador jugador = null;
			
			if ( fixtureA.getUserData() instanceof Jugador ) 
				jugador = (Jugador) fixtureA.getUserData();
			if ( fixtureB.getUserData() instanceof Jugador ) 
				jugador = (Jugador) fixtureB.getUserData();
			
			// Al tocar el jugador con el escenario se reinician los saltos
			jugador.reiniciarSaltos();
			break;
			
		// Colision entre un enemigo y un límite
		case CATEGORIA_ENEMIGO | CATEGORIA_LIMITE :
			Enemigo enemigo = null;
			boolean enemigoEnA;
			// Objeto enemigo
			if ( fixtureA.getUserData() instanceof Enemigo ) {
				enemigo = (Enemigo) fixtureA.getUserData();
				enemigoEnA = true;
			}
			else if ( fixtureB.getUserData() instanceof Enemigo ) {
				enemigo = (Enemigo) fixtureB.getUserData();
				enemigoEnA = false;
			}else break; 
			
			// Limite
			String tipoLimite = "";
			// Tipo de límite con el que ha colisionado
			tipoLimite += enemigoEnA?(String)fixtureB.getBody().getUserData() : (String)fixtureA.getBody().getUserData();
			
			// Según el tipo de límite realiza un movimiento
			switch ( tipoLimite ) {
			case "izquierda": // Cambia la dirección del enemigo 
				enemigo.cambiarDireccion( true );
				break;
			case "derecha": // Cambia la dirección del enemigo 
				enemigo.cambiarDireccion( false );
				System.out.println("Cambiando derecha");
				break;
			}
			break;
		// Colision entre Jugador y Enemigo
		case CATEGORIA_JUGADOR | CATEGORIA_ENEMIGO:
			// Referencias al jugador y al enemigo
			Jugador auxJugador = null;
			Enemigo auxEnemigo = null;
			
			// Objeto Jugador
			if ( fixtureA.getUserData() instanceof Jugador ) {
				// Jugador
				auxJugador = (Jugador) fixtureA.getUserData();
				// Enemigo
				auxEnemigo = (Enemigo) fixtureB.getUserData();
			}else if ( fixtureB.getUserData() instanceof Jugador ) {
				// Jugador
				auxJugador = (Jugador) fixtureB.getUserData();
				// Enemigo
				auxEnemigo = (Enemigo) fixtureA.getUserData();
			}else break; 
			
			// Ataca al jugador
			auxEnemigo.atacarJugador(auxJugador);
			break;
		// Colision entre OndaEspada y Enemigo
		case CATEGORIA_PODERES | CATEGORIA_ENEMIGO:
			// Referencias a la onda de la espada y al enemigo
			OndaEspada auxOnda = null;
			Enemigo enemigoAtacar = null;

			// Objeto OndaEspada
			if ( fixtureA.getUserData() instanceof OndaEspada ) {
				// OndaEspada
				auxOnda = (OndaEspada) fixtureA.getUserData();
				// Enemigo
				enemigoAtacar = (Enemigo) fixtureB.getUserData();
			}else if ( fixtureB.getUserData() instanceof OndaEspada ) {
				// OndaEspada
				auxOnda = (OndaEspada) fixtureB.getUserData();
				// Enemigo
				enemigoAtacar = (Enemigo) fixtureA.getUserData();
			}else break; 
			
			// Ataca al enemigo
			Jugador jugadorOnda = auxOnda.getJugador();
			enemigoAtacar.restarVida( jugadorOnda.getPuntosAtaque() );
			break;
			
		// Colision entre Jugador y Loot
		case CATEGORIA_JUGADOR | CATEGORIA_LOOT:
			// Referencias al jugador y al loot
			Jugador jugadorActual = null;
			Loot auxLoot = null;

			// Objeto Jugador
			if ( fixtureA.getUserData() instanceof Jugador ) {
				// Jugador
				jugadorActual = (Jugador) fixtureA.getUserData();
				// Loot
				auxLoot = (Loot) fixtureB.getUserData();
			}else if ( fixtureB.getUserData() instanceof Jugador ) {
				// Jugador
				jugadorActual = (Jugador) fixtureB.getUserData();
				// Loot
				auxLoot = (Loot) fixtureA.getUserData();
			}else break;

			// Sonido
			if ( auxLoot instanceof Moneda )
				GestionAudio.SONIDO_MONEDA.play();
			// Cambia la propiedad "cogido" del loot a true
			auxLoot.setCogido(true);
			// Aumenta la cantidad de monedas del jugador
			jugadorActual.addMoneda();
			System.out.println(jugadorActual.getCantidadMonedas());
			break;
		}

	}

	@Override
	public void endContact(Contact arg0) {
		
					
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		
	}
	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
	
	}

}
