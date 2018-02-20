package util;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pantallas.MapaUno;
import sprites.Enemigo;
import sprites.Loot;

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
