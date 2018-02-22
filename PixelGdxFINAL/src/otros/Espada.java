package otros;

import java.io.Serializable;

public class Espada implements Serializable{
	// Tipos de espada
	public enum TiposEspada{ BASICA , TOCHA }
	// Da�o por defecto
	private static final int DA�O_ESPADA_BASICA = 1;
	private static final int DA�O_ESPADA_TOCHA = 4;
	
	// Tipo
	private TiposEspada tipo;
	// Da�o
	private int da�o;
	
	// Constructor
	public Espada ( TiposEspada tipo ) {
		this.tipo = tipo;
		da�o = tipo==TiposEspada.BASICA?DA�O_ESPADA_BASICA:DA�O_ESPADA_TOCHA;
	}

	public static int getDa�oEspadaBasica() {
		return DA�O_ESPADA_BASICA;
	}

	public static int getDa�oEspadaTocha() {
		return DA�O_ESPADA_TOCHA;
	}

	public TiposEspada getTipo() {
		return tipo;
	}

	public int getDa�o() {
		return da�o;
	}

	public void setTipo(TiposEspada tipo) {
		this.tipo = tipo;
	}

	public void setDa�o(int da�o) {
		this.da�o = da�o;
	}
	
	public void aumentarDa�o(int aumento) {
		da�o+= aumento;
	}
}
