package util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pantallas.MapaUno;
import sprites.Jugador;

public class GestionJugadores {
	
	// ATRIBUTOS 
	private Jugador jugadorServidor; // Jugador servidor
	private Jugador jugadorCliente; // Jugador cliente
	
	// CONSTRUCTOR
	public GestionJugadores ( MapaUno mapa ) {
		// Jugador servidor
		jugadorServidor = new Jugador(mapa, "rey", 69);
	}
	
	public void update( float delta ) {
		// Jugador servidor
		jugadorServidor.update( delta );
	}
	
	public void render( SpriteBatch batch ) {
		// Jugador servidor
		jugadorServidor.draw(batch);
	}
	
	// Getters
	// Este método tiene que devolver el jugador cliente si somos cliente o el servidor si somos servidor...
	public Jugador getJugadorPrincipal() {
		return jugadorServidor;
	}
	

}
