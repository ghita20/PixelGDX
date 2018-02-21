package sockets;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class DatoLoot implements Serializable{
	
	private Vector2 posicion;
	private float tiempoAnimacion;
	private String tipo;
	private int id;
	
	public Vector2 getPosicion() {
		return posicion;
	}
	public String getTipo() {
		return tipo;
	}
	public int getId() {
		return id;
	}
	public float getTiempoAnimacion() {
		return tiempoAnimacion;
	}
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion;
	}
	public void setTiempoAnimacion(float tiempoAnimacion) {
		this.tiempoAnimacion = tiempoAnimacion;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	

}
