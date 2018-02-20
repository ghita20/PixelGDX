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
import sprites.Enemigo;
import sprites.Loot;
import sprites.Moneda;
import sprites.Murcielago;
import sprites.Serpiente;

// Esta clase se encarga de la gesti�n de los mobs, loot 
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
	
	// A�adir enemigo
	public void addEnemigo ( Enemigo enemigo ) {
		enemigos.add(enemigo);
	}
	
	// Update
	public void update( float delta ) {
		// Itera sobre los enemigos
		Iterator<Enemigo> iterador = enemigos.iterator();
		// Recorre los enemigos
		while ( iterador.hasNext() ) {
			Enemigo enemigo = iterador.next();
			// Si el enemigo no est� muerto actuliza su estado
			if ( !enemigo.getMuerto() )
				enemigo.update(delta);
			else { // Si est� muerto lo a�ade a li lista de cuerpos a eliminar
				mapa.getWorldUpdate().addCuerpo(enemigo.getCuerpo());
				
				// Genera el loot aleatoriamente
				int random = (int) (Math.random()*10);
				System.out.println(random);
				if ( random > 5) {
					Loot auxLoot = new Moneda(mapa,enemigo.getCuerpo().getPosition().x,enemigo.getCuerpo().getPosition().y,BodyType.StaticBody);
					// Lo a�ade al mundo
					mapa.getLoot().addLoot(auxLoot); 
				}
				
				// y lo elimina del arraylist
				iterador.remove();
			}
		}
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
		// Los a�ade a la lista
		enemigos.addAll(serpientes);
		
		// Genera los murcielagos
		ArrayList<Murcielago> murcielagos = BodyCreator.spawnMurcielagos(mapa, map);
		// Los a�ade a la lista de enemigos
		enemigos.addAll(murcielagos);
	}
	

}
