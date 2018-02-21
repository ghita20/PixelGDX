package util;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import juego.PixelGdx;
import pantallas.MapaUno;
import sockets.DatosJugador;
import sockets.DatosMob;
import sockets.DatosMobs;
import sprites.Enemigo;
import sprites.Loot;
import sprites.Moneda;
import sprites.Murcielago;
import sprites.Serpiente;

// Esta clase se encarga de la gestión de los mobs, loot 
public class GestionMobs {
	
	// Mapa
	private MapaUno mapa;
	
	// Enemigos
	private ArrayList<Enemigo> enemigos;
	
	// Constructor
	public GestionMobs ( MapaUno mapa , TiledMap map ) {
		// Mapa
		this.mapa = mapa;
		// Instancia el arrayList
		enemigos = new ArrayList<>();
		// Genera los mobs
		generarMobs(map);
	}
	
	// Añadir enemigo
	public void addEnemigo ( Enemigo enemigo ) {
		enemigos.add(enemigo);
	}
	
	// Update
	public void update( float delta ) {
		// Si soy el servidor actualizo los mobs y genero un objeto DatosMobs con información
		if ( mapa.getJuego().getConexionSocket().getEsServidor()) {
			updateMobs(delta);
			agregarDatosMobs();
		}else // Sincronizo los mobs con los datos del servidor
			sincronizarMobs();	
	}
	
	// Actualiza el estado de los mobs
	private void updateMobs(float delta) {
		// Itera sobre los enemigos
		Iterator<Enemigo> iterador = enemigos.iterator();
		// Recorre los enemigos
		while ( iterador.hasNext() ) {
			Enemigo enemigo = iterador.next();
			// Si el enemigo no está muerto actuliza su estado
			if ( !enemigo.getMuerto() )
				enemigo.update(delta);
			else { // Si está muerto lo añade a li lista de cuerpos a eliminar
				mapa.getWorldUpdate().addCuerpo(enemigo.getCuerpo());

				// Genera el loot aleatoriamente
				int random = (int) (Math.random()*10);
				System.out.println(random);
				if ( random > 5) {
					Loot auxLoot = new Moneda(mapa,enemigo.getCuerpo().getPosition().x,enemigo.getCuerpo().getPosition().y,BodyType.StaticBody,true);
					// Lo añade al mundo
					mapa.getLoot().addLoot(auxLoot); 
				}

				// y lo elimina del arraylist
				iterador.remove();
			}
		}
	}
	
	// Sincroniza lo mobs
	private void sincronizarMobs() {
		if ( mapa.getJuego().getConexionSocket().recogerDatos() != null ) {
			// Recoge el objeto recibido
			DatosMobs datosMobs = mapa.getJuego().getConexionSocket().recogerDatos().getDatosMobs();
			
			// Itera sobre los enemigos
			Iterator<Enemigo> iterador = enemigos.iterator();
			// Recorre los enemigos
			while ( iterador.hasNext() ) {
				Enemigo enemigo = iterador.next();
				int j = 0;
				while ( j < datosMobs.getMobs().size() ) {
					// Actualiza el estado del mob
					if ( enemigo.getId() == datosMobs.getMobs().get(j).getId()) {
						// Actualiza la posición
						enemigo.getCuerpo().setTransform(datosMobs.getMobs().get(j).getPosicion(), 0);
						// Actualiza la posición del sprite
						enemigo.setPosition(
								enemigo.getCuerpo().getPosition().x - enemigo.getWidth()/2, 
								enemigo.getCuerpo().getPosition().y - enemigo.getHeight()/2);
						// Le asigna su textura
						enemigo.setRegion( enemigo.getFrame(
								datosMobs.getMobs().get(j).getTiempoAnimacion(),
								datosMobs.getMobs().get(j).getAturdido(),
								datosMobs.getMobs().get(j).getDireccionDerecha()));
						break; // Sale del while
					}
					j++;
				}
				// Si ha llegado al final y no encuentra el mob significa que habrá muerto
				if ( j == datosMobs.getMobs().size()) {
					// Borra su cuerpo
					mapa.getWorldUpdate().addCuerpo(enemigo.getCuerpo());
					// Lo elimina del arraylist
					iterador.remove();
				}
			}
		}
	}

	// Agrega los datos de los mobs al objeto que enviará por el socket
	private void agregarDatosMobs ( ) {
		// Si soy el cliente genero un objeto DatosMobs con información de los mobs
		DatosMobs datosMobs = new DatosMobs();
		// Recorro los mobs
		for ( Enemigo e : enemigos ) {
			DatosMob datoE = new DatosMob();
			datoE.setPosicion(e.getCuerpo().getPosition());
			datoE.setId(e.getId());
			datoE.setAturdido(e.getAturdido());
			datoE.setTiempoAnimacion(e.getTiempo());
			datoE.setDireccionDerecha(e.getDireccion());
			// 
			datosMobs.addMob(datoE);
		}
		mapa.getDatosEnviar().setDatosMobs(datosMobs);
	}
	
	// Render
	public void render( SpriteBatch batch ) {
		// Renderiza todos los enemigos
		for ( Enemigo enemigo : enemigos )
			enemigo.draw(batch);
	}
	
	// Crea los mobs que hay en el tileMap
	private void generarMobs ( TiledMap map ) {
		// Genera las serpientes
		ArrayList<Serpiente> serpientes = BodyCreator.spawnSerpientes(mapa, map);
		// Los añade a la lista
		enemigos.addAll(serpientes);
		
		// Genera los murcielagos
		ArrayList<Murcielago> murcielagos = BodyCreator.spawnMurcielagos(mapa, map);
		// Los añade a la lista de enemigos
		enemigos.addAll(murcielagos);
	}
	

}
