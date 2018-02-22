package util;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import pantallas.MapaUno;
import sockets.DatoLoot;
import sockets.DatosLoot;
import sockets.DatosMob;
import sockets.DatosMobs;
import sprites.Enemigo;
import sprites.Loot;
import sprites.Moneda;

public class GestionLoot {
	// Mapa
	private MapaUno mapa;
	
	// Loot actual del mapa
	private ArrayList<Loot> loot;
	
	// Constructor
	public GestionLoot ( MapaUno mapa ) {
		// Mapa
		this.mapa = mapa;
		// Instancia el arrayList
		loot = new ArrayList<>();
		// Añade el loot que hay en el tiledMap
		loot.addAll( BodyCreator.spawnMonedas(mapa, mapa.getGestionesMapa().getMap()));
	}
	
	// Update
	public void update ( float delta ) {
		// Si soy el servidor actualizo el loot y lo mando al cliente
		if ( mapa.getJuego().getConexionSocket().getEsServidor()) {
			actualizarLoot(delta);
			agregarDatosLoot();
		}else // Sincronizo el loot con los datos del servidor
			sincronizarLoot();	
		
	}
	
	// Sincroniza el loot
	private void sincronizarLoot() {
		if ( mapa.getJuego().getConexionSocket().recogerDatos() != null ) {
			// Recoge el objeto recibido
			DatosLoot datosLoot = mapa.getJuego().getConexionSocket().recogerDatos().getDatosLoot();
			
			// Itera sobre el loot
			Iterator<Loot> iterador = loot.iterator();
			// Recorre el loot
			while ( iterador.hasNext() ) {
				Loot auxL = iterador.next();
				int j = 0;
				while ( j < datosLoot.getLoot().size() ) {
					// Actualiza el estado del loot
					if ( auxL.getId() == datosLoot.getLoot().get(j).getId()) {
						// Actualiza la posición
						auxL.getCuerpo().setTransform(datosLoot.getLoot().get(j).getPosicion(), 0);
						// Actualiza la posición del sprite
						auxL.setPosition(
								auxL.getCuerpo().getPosition().x - auxL.getWidth()/2, 
								auxL.getCuerpo().getPosition().y - auxL.getHeight()/2);
						// Le asigna su textura
						auxL.setRegion( auxL.getFrameRemoto(datosLoot.getLoot().get(j).getTiempoAnimacion()));
						break; // Sale del while
					}
					j++;
				}
				// Si ha llegado al final y no encuentra el loot no existe en el server
				if ( j == datosLoot.getLoot().size()) {
					// Borra su cuerpo
					mapa.getWorldUpdate().addCuerpo(auxL.getCuerpo());
					// Lo elimina del arraylist
					iterador.remove();
				}
			}
		}
		
		// Crea el loot nuevo del servidor en el cliente ya que todo se crea primero en el servidor
		DatoLoot nuevoLoot = mapa.getJuego().getConexionSocket().recogerLoot();
		if ( nuevoLoot != null ) {
			// Si es de tipo moneda
			if ( nuevoLoot.getTipo().equals("Moneda")) {
				// Instancia la moneda
				Moneda auxM = new Moneda(mapa, nuevoLoot.getPosicion().x, nuevoLoot.getPosicion().y, BodyType.StaticBody, true);
				// Añade la moneda al gestionloot
				mapa.getLoot().addLoot(auxM);
			}
		}
	}

	// Agrega los datos del loot al objeto que se le envía al cliente
	private void agregarDatosLoot() {
		DatosLoot datosLoot = new DatosLoot();
		// Recorro los mobs
		for ( Loot l : loot ) {
			DatoLoot datoL = new DatoLoot();
			datoL.setPosicion(l.getCuerpo().getPosition());
			datoL.setTiempoAnimacion(l.getTiempo());
			datoL.setTipo( l.getClass().getSimpleName() );
			datoL.setId(l.getId());
			// 
			datosLoot.addLoot(datoL);
		}
		mapa.getDatosEnviar().setDatosLoot(datosLoot);
	}
	
	// Actualiza loot
	private void actualizarLoot( float delta ) {
		// Itera sobre el loot
		Iterator<Loot> iterador = loot.iterator();
		// Recorre los objetos
		while ( iterador.hasNext() ) {
			Loot auxLoot = iterador.next();
			// Si el loot no ha sido cogido lo actualiza
			if ( !auxLoot.getCogido() )
				auxLoot.update(delta);
			else { // Si ha sido cogido lo elimina
				mapa.getWorldUpdate().addCuerpo(auxLoot.getCuerpo());
				// y lo elimina del arraylist
				iterador.remove();
			}
		}
	}
	
	// Render
	public void render( SpriteBatch batch ) {
		// Renderiza todos los objetos loot
		for ( Loot auxLoot : loot )
			auxLoot.draw(batch);
	}

	
	// Añade un objeto loot al mapa
	public void addLoot ( Loot nuevoLoot ) {
		loot.add(nuevoLoot);
	}

}
