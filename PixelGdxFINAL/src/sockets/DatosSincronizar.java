package sockets;

import java.io.Serializable;

public class DatosSincronizar implements Serializable{
	// Datos jugador
	private DatosJugador datosJugador;
	// Datos mobs
	private DatosMobs datosMobs;
	// Datos loot
	private DatosLoot datosLoot;
	
	// Constructor
	public DatosSincronizar() {
		// TODO Auto-generated constructor stub
	}
	
	// Getters
	public DatosJugador getDatosJugador() {
		return datosJugador;
	}
	public DatosMobs getDatosMobs() {
		return datosMobs;
	}
	public DatosLoot getDatosLoot() {
		return datosLoot;
	}
	
	// Setters
	public void setDatosJugador(DatosJugador datosJugador) {
		this.datosJugador = datosJugador;
	}
	public void setDatosMobs(DatosMobs datosMobs) {
		this.datosMobs = datosMobs;
	}
	public void setDatosLoot(DatosLoot datosLoot) {
		this.datosLoot = datosLoot;
	}

}
