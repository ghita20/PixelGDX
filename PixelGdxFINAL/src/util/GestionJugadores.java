package util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hud.StatsJugador;
import pantallas.MapaUno;
import sprites.Jugador;

// TODO: esta clase tiene que predecir si soy el servidor o el cliente
public class GestionJugadores {
	
	// ATRIBUTOS 
	private Jugador jugadorServidor; // Jugador servidor
	private Jugador jugadorCliente; // Jugador cliente
	
	// HUD - pertenece al jugador principal
	private StatsJugador statsJugador; // Los stats del jugador que se muestran en pantalla
	
	// CONSTRUCTOR 
	public GestionJugadores ( MapaUno mapa ) {
		// Jugador servidor
		jugadorServidor = new Jugador(mapa, "rey");
		
		// Stats del jugador
		statsJugador = new StatsJugador(mapa.getJuego().getBatch(), jugadorServidor);
	}
	
	public void update( float delta ) {
		// Jugador servidor
		jugadorServidor.update( delta );
	}
	
	public void render( SpriteBatch batch ) {
		// Jugador servidor
		jugadorServidor.draw(batch);
		
		// Si no paramos el batch antes de renderizar el stage genera excepción
		batch.end();
		
		// Renderiza los stats
		statsJugador.stage.draw();
		
	}
	
	// Getters
	// Este método tiene que devolver el jugador cliente si somos cliente o el servidor si somos servidor...
	public Jugador getJugadorPrincipal() {
		return jugadorServidor;
	}
	

}
