package util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import juego.PixelGdx;
import pantallas.MapaUno;

// Cargar un mapa, crear las fisicas etc..
public class GestionesMapaUno {
	
	// Límites del mapa
	public static final float WIDTH = 31.4f;
	public static final float HEIGHT = 31.4f;
	
	private TmxMapLoader maploader; // Carga el tileMap
	private TiledMap map; // Mapa tileMap
	private OrthogonalTiledMapRenderer renderer; // Para renderizar
	
	// Constructor
	public GestionesMapaUno ( MapaUno mapa ) {
		// Carga el mapa
		maploader = new TmxMapLoader();
		
		// Filtros para mejorar el renderizado
		TmxMapLoader.Parameters param = new TmxMapLoader.Parameters();
		param.textureMagFilter = Texture.TextureFilter.Nearest;
		param.textureMinFilter = Texture.TextureFilter.Nearest;
		param.generateMipMaps = true;
		
		// Carga el tileMap
		map = maploader.load("assets/mapa_uno/tile_map.tmx",param);
		
		// Instancia el render y se le pasa como argumento el tileMap a dibujar
		renderer = new OrthogonalTiledMapRenderer(map, 1f  / PixelGdx.PPM);
		
		// Crea los objetos fisicos del mapa
		BodyCreator.crearCuerposMapa(mapa.getWorld(), map);
		
	}
	
	// 
	public void dispose() {
		renderer.dispose();
	}
	
	public void update( OrthographicCamera gameCam ) {
		// Le dice al renderer que pintar ( pinta lo que actualmente se esta viendo en pantalla )
		renderer.setView(gameCam);
	}
	
	public void render() {
		// Dibuja
		renderer.render();
	}
	
	// Getters
	public TiledMap getMap() {
		return map;
	}
	

}
