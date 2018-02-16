package util;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class WorldUpdate {
	
	// World
	private World world;
	// Cuerpos que eliminar� despues del step
	private ArrayList<Body> cuerposEliminar;
	
	// Constructor
	public WorldUpdate ( World world ) {
		this.world = world;
		// Instancia el arraylist
		cuerposEliminar = new ArrayList<>();
	}

	// Update
	public void update ( ) {
		// World step. En teor�a siempre hay que eliminar los cuerpos despu�s del world.step
		world.step( 1 / 60f, 6, 2);
		// Elimina los cuerpos
		eliminarCuerpos();
	}
	
	// A�ade un cuerpo a la lista
	public void addCuerpo ( Body body ) {
		cuerposEliminar.add(body);
	}
	
	public void eliminarCuerpos ( ) {
		// Si el mundo ya ha acabado de hacer sus cositas
		if ( !world.isLocked() ) {
			// Elimina los cuerpos
			for ( Body body : cuerposEliminar )
				world.destroyBody(body);
			// Limpia la escena del crimen
			cuerposEliminar.clear();
		}
	}
}
