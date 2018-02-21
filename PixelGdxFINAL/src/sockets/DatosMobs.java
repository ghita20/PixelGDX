package sockets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

public class DatosMobs implements Serializable{
	
	// Informacion mobs
	private ArrayList<DatosMob> mobs;

	// Constructor
	public DatosMobs() {
		mobs = new ArrayList<>();
	}
	
	public ArrayList<DatosMob> getMobs() {
		return mobs;
	}
	
	// Añade datos de un mob
	public void addMob( DatosMob mob ){
		mobs.add(mob);
	}

}
