package sockets;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

import sprites.Jugador.Estado;

public class DatosJugador implements Serializable{

	// Posición
	private Vector2 posicion;
	private Vector2 velocidadLinear;
	private int cantidadMonedas;
	private Estado estado;
	private float tiempoEstado;
	private boolean atacando;
	
	// Constructor
	public DatosJugador() {	}
	
	// Setters
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion;
	}
	public void setVelocidadLinear(Vector2 velocidadLinear) {
		this.velocidadLinear = velocidadLinear;
	}
	public void setCantidadMonedas(int cantidadMonedas) {
		this.cantidadMonedas = cantidadMonedas;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public void setTiempoEstado(float tiempoEstado) {
		this.tiempoEstado = tiempoEstado;
	}
	public void setAtacando(boolean atacando) {
		this.atacando = atacando;
	}
	
	// Getters
	public Vector2 getPosicion() {
		return posicion;
	}
	public Vector2 getVelocidadLinear() {
		return velocidadLinear;
	}
	public int getCantidadMonedas() {
		return cantidadMonedas;
	}
	public Estado getEstado() {
		return estado;
	}
	public boolean getAtacando() {
		return atacando;
	}
	public float getTiempoEstado() {
		return tiempoEstado;
	}
	
}
