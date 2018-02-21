package sockets;

import java.io.Serializable;
import java.util.ArrayList;

public class DatosLoot implements Serializable{
	
	// Información del loot
	private ArrayList<DatoLoot> loot;

	// Constructor
	public DatosLoot() {
		loot = new ArrayList<>();
	}

	public ArrayList<DatoLoot> getLoot() {
		return loot;
	}

	public void addLoot ( DatoLoot objeto ) {
		loot.add(objeto);
	}
	
	
}
