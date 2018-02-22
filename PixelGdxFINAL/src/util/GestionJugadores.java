package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import hud.StatsJugador;
import otros.Espada;
import pantallas.MapaUno;
import sockets.Ataque;
import sockets.DatosJugador;
import sockets.SocketServidor;
import sprites.Jugador;
import sprites.Jugador.Estado;
import sprites.Jugador.TipoJugador;

// TODO: esta clase tiene que predecir si soy el servidor o el cliente
public class GestionJugadores {
	
	// Mapa
	private MapaUno mapa;
	
	// ATRIBUTOS 
	private Jugador jugadorServidor; // Jugador servidor
	private Jugador jugadorCliente; // Jugador cliente
	
	// HUD - pertenece al jugador principal
	private StatsJugador statsJugador; // Los stats del jugador que se muestran en pantalla
	
	// Sólo actualiza la posición del jugador cada 5 sincronizaciones
	private int numeroSincronizaciones ;
	
	// CONSTRUCTOR 
	public GestionJugadores ( MapaUno mapa ) {
		// Mapa
		this.mapa = mapa;
		// Jugador servidor
		jugadorServidor = new Jugador(mapa, "rey", TipoJugador.PRINCIPAL);
		
		// Jugador cliente
		jugadorCliente = new Jugador(mapa, "rey_rojo", TipoJugador.REMOTO);
		
		// Stats del jugador
		statsJugador = new StatsJugador(mapa.getJuego().getBatch(), getJugadorPrincipal(),mapa.getHerrero());
		
		// Número sincronizaciones
		numeroSincronizaciones = 0;
	}
	
	// Update
	public void update( float delta ) {
		// Añade los datos del jugador al objeto que se enviará al remoto
		añadirDatosJugador();
		
		// Sólo actualizo mi jugador
		getJugadorPrincipal().update(delta);
		
		// Si soy cliente y el servidor me manda la señal de que he sido atacado
		if ( !mapa.getJuego().getConexionSocket().getEsServidor() ) {
			// Si le han atacado en el servidor
			Ataque ataqueRecibido = mapa.getJuego().getConexionSocket().getAtaque();
			if ( ataqueRecibido != null ) {
				// Asigna la velocidad lineal del jugador a 0
				getJugadorPrincipal().getCuerpo().setLinearVelocity( new Vector2(0,0));
				// Ataca impulsandolo
				getJugadorPrincipal().getCuerpo().applyLinearImpulse(new Vector2( ataqueRecibido.direccionDerecha?1f:-1f,1.5f), getJugadorRemoto().getCuerpo().getWorldCenter(), true);
				// Aturde al jugador
				getJugadorPrincipal().aturdir();
				
				// Resta vida al jugador
				getJugadorPrincipal().restarVida( ataqueRecibido.daño );
				
				// TODO: Arreglar el daño
				//System.out.println(ataqueRecibido.daño);
			}
			

			// Si soy cliente recibo mis datos del servidor, ( monedas , tipo espada ). Las colisiones con el loot se realizan en el servidor por tanto es necesario sincronizar
			Espada nuevaEspada = mapa.getJuego().getConexionSocket().recogerEspada();
			// Si tengo nueva espada la asigno
			if ( nuevaEspada != null )
				getJugadorPrincipal().setEspada(nuevaEspada);
			
			// Nueva moneda
			int nuevaMoneda = mapa.getJuego().getConexionSocket().recogerMoneda();
			if ( nuevaMoneda == 1 )
				getJugadorPrincipal().addMoneda();
		}
		
		
		// Sincronizo los datos del jugador remoto
		sincronizarJugadorRemoto();
		
		// Actualiza las ondas de la espada del jugador remoto
		getJugadorRemoto().actualizarOndasEspada(Gdx.graphics.getDeltaTime());
		
		// Refresca los stats 
		statsJugador.refrescarStats();
	}
	
	public void render( SpriteBatch batch ) {
		// Jugador servidor
		jugadorServidor.draw(batch);
		
		// Jugador cliente
		jugadorCliente.draw(batch);
		
		// Si no paramos el batch antes de renderizar el stage genera excepción
		batch.end();
		
		// Renderiza los stats
		statsJugador.stage.draw();
		
	}
	
	// Otros métodos
	private void añadirDatosJugador() {
		// Añado datos sobre mi posición
		DatosJugador misDatos = new DatosJugador();
		
		// Posición
		misDatos.setPosicion( getJugadorPrincipal().getCuerpo().getPosition() );
		// Velocidad linear
		misDatos.setVelocidadLinear(
				getJugadorPrincipal().getCuerpo().getLinearVelocityFromWorldPoint(getJugadorPrincipal().getCuerpo().getWorldCenter()));
		// Estado
		misDatos.setEstado( getJugadorPrincipal().getEstado() );
		// Tiempo estado
		misDatos.setTiempoEstado( getJugadorPrincipal().getTiempoEstado());
		// Espada
		misDatos.setEspada( getJugadorPrincipal().getEspada() );
		
		// Agrega los datos del jugador al objeto que enviará al remoto
		mapa.getDatosEnviar().setDatosJugador(misDatos);
	}
	private void sincronizarJugadorRemoto ( ) {
		if ( mapa.getJuego().getConexionSocket().recogerDatos() != null ) {
			// Recoge el objeto recibido
			DatosJugador datosJugadorRemoto = mapa.getJuego().getConexionSocket().recogerDatos().getDatosJugador();
			
			// Espada
			getJugadorRemoto().setEspada(datosJugadorRemoto.getEspada());
			// Posición del sprite
			getJugadorRemoto().setPosition(
					getJugadorRemoto().getCuerpo().getPosition().x - getJugadorRemoto().getWidth() / 2,
					getJugadorRemoto().getCuerpo().getPosition().y - getJugadorRemoto().getHeight() / 2);
			
			// Recoge la velocidad lineal del jugador
			getJugadorRemoto().getCuerpo().setLinearVelocity(datosJugadorRemoto.getVelocidadLinear());
			
			// Actualizo la posición real solo cada 5 sincronizaciones
			if (datosJugadorRemoto.getVelocidadLinear().x == 0 && datosJugadorRemoto.getVelocidadLinear().y == 0 || numeroSincronizaciones == 0 )
				getJugadorRemoto().getCuerpo().setTransform(datosJugadorRemoto.getPosicion(), 0);
				
			// Aumenta el número de sincronizaciones
			numeroSincronizaciones++;
			// Si llego a 5 reinicio
			if ( numeroSincronizaciones == 5 )
				numeroSincronizaciones = 0;
			
			// Textura
			Estado estadoJugador = datosJugadorRemoto.getEstado();
			float tiempoAnimacion = datosJugadorRemoto.getTiempoEstado();
			// Asigna la textura correspondiente al jugador según su estado
			getJugadorRemoto().setRegion( getJugadorRemoto().getFrame(Gdx.graphics.getDeltaTime(),estadoJugador,tiempoAnimacion,true));
		
			// Si el jugado acaba de atacar lo hace también
			if ( mapa.getJuego().getConexionSocket().getAtacando() )
				getJugadorRemoto().atacar(false);
		}
	}
	
	// Getters
	// Este método tiene que devolver el jugador cliente si somos cliente o el servidor si somos servidor...
	public Jugador getJugadorPrincipal() {
		return mapa.getJuego().getConexionSocket().getEsServidor()?jugadorServidor:jugadorCliente;
	}
	public Jugador getJugadorRemoto() {
		return mapa.getJuego().getConexionSocket().getEsServidor()?jugadorCliente:jugadorServidor;
	}
	public StatsJugador getStatsJugador() {
		return statsJugador;
	}
	

}
