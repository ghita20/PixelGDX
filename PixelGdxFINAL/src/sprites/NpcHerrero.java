package sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

import sprites.Jugador.Espada;

public class NpcHerrero extends Sprite{
	
	// Variable para saber si el herrero ha terminado de mejorar el arma
	private boolean mejorandoArma;
	private boolean mejoraAcabada;
	private Jugador jugador;
	private int tiempoMejora;
	private Espada tipoEspada;
	
	// Constructor
	public NpcHerrero ( ) {
		mejorandoArma = false;
		mejoraAcabada = false;
		tiempoMejora = 0;
		jugador = null;
	}
	
	// Lanza un hilo de mejora que mejorará el arma
	public void mejorarArma ( Espada tipoEspada , Jugador jugador ) {
		// tiempo que va a tardar en mejorar el arma
		tiempoMejora = (int)(Math.random() * 1000);
		// Almacena la referencia al jugadir
		this.jugador = jugador;
		// Tipo espada
		this.tipoEspada = tipoEspada;
		
		// Crea un hilo y lo lanza
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(tiempoMejora);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mejoraAcabada = true;
				mejorandoArma = false;
			}
			
		}).start();
	}
	
	// Cuando el jugador va a  recoger su arma
	public boolean recogerArma ( ) {
		if ( !mejorandoArma ) {
			jugador.mejorarEspada(tipoEspada);
			
			// Reestablece las variables
			mejorandoArma = false;
			mejoraAcabada = false;
			tiempoMejora = 0;
			jugador = null;
			return true;
		}
		// Si devuelve false es que no ha acabado la mejora
		return false; 
	}
	
	//Getters
	public boolean isMejorandoArma() {
		return mejorandoArma;
	}

	public boolean isMejoraAcabada() {
		return mejoraAcabada;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public int getTiempoMejora() {
		return tiempoMejora;
	}

	public void setMejorandoArma(boolean mejorandoArma) {
		this.mejorandoArma = mejorandoArma;
	}

	public void setMejoraAcabada(boolean mejoraAcabada) {
		this.mejoraAcabada = mejoraAcabada;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public void setTiempoMejora(int tiempoMejora) {
		this.tiempoMejora = tiempoMejora;
	}
	
 
}
