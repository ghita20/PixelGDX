package util;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import juego.PixelGdx;
import pantallas.MapaUno;
import sprites.Enemigo;
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
		// Actualiza el estado de los enemigos
		for ( Enemigo enemigo : enemigos )
			enemigo.update(delta);
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
		for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
			// Recupera el rectángulo para saber su posición
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			// Crea una nueva serpiente
			enemigos.add( new Serpiente(mapa, rect.getX() / PixelGdx.PPM, rect.getY() / PixelGdx.PPM));
		}
	}
	

}
