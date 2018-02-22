package sockets;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

import otros.Espada;
import sprites.Jugador.Estado;

public class DatosJugador implements Serializable{

	// Posición
	private Vector2 posicion;
	private Vector2 velocidadLinear;
	private int cantidadMonedas;
	private Estado estado;
	private float tiempoEstado;
	private boolean atacando;
	private Espada espada;
	
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
	public void setEspada(Espada espada) {
		this.espada = espada;
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
	public Espada getEspada() {
		return espada;
	}
	public boolean getAtacando() {
		return atacando;
	}
	public float getTiempoEstado() {
		return tiempoEstado;
	}
	
}
