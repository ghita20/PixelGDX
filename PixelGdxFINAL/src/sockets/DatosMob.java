package sockets;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class DatosMob implements Serializable{
	// Atributos
	private boolean direccionDerecha;
	private boolean aturdido;
	private float tiempoAnimacion;
	private Vector2 posicion;
	private int id;
	
	// getters y setters
	public int getId() {
		return id;
	}
	public Vector2 getPosicion() {
		return posicion;
	}
	public float getTiempoAnimacion() {
		return tiempoAnimacion;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion;
	}
	public void setTiempoAnimacion(float tiempoAnimacion) {
		this.tiempoAnimacion = tiempoAnimacion;
	}
	public void setAturdido(boolean aturdido) {
		this.aturdido = aturdido;
	}
	public boolean getAturdido() {
		return aturdido;
	}
	public void setDireccionDerecha(boolean direccionDerecha) {
		this.direccionDerecha = direccionDerecha;
	}
	public boolean getDireccionDerecha() {
		return direccionDerecha;
	}
}
