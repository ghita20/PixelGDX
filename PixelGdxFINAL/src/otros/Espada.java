package otros;

import java.io.Serializable;

public class Espada implements Serializable{
	// Tipos de espada
	public enum TiposEspada{ BASICA , TOCHA }
	// Daño por defecto
	private static final int DAÑO_ESPADA_BASICA = 1;
	private static final int DAÑO_ESPADA_TOCHA = 4;
	
	// Tipo
	private TiposEspada tipo;
	// Daño
	private int daño;
	
	// Constructor
	public Espada ( TiposEspada tipo ) {
		this.tipo = tipo;
		daño = tipo==TiposEspada.BASICA?DAÑO_ESPADA_BASICA:DAÑO_ESPADA_TOCHA;
	}

	public static int getDañoEspadaBasica() {
		return DAÑO_ESPADA_BASICA;
	}

	public static int getDañoEspadaTocha() {
		return DAÑO_ESPADA_TOCHA;
	}

	public TiposEspada getTipo() {
		return tipo;
	}

	public int getDaño() {
		return daño;
	}

	public void setTipo(TiposEspada tipo) {
		this.tipo = tipo;
	}

	public void setDaño(int daño) {
		this.daño = daño;
	}
	
	public void aumentarDaño(int aumento) {
		daño+= aumento;
	}
}
