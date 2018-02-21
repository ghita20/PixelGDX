package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

import juego.PixelGdx;
import otros.Espada;
import pantallas.MapaUno;
import util.BodyCreator;

public class NpcHerrero extends Sprite{
	
	// Cuerpo
	private Body cuerpo;
	
	// Variable para saber si el herrero ha terminado de mejorar el arma
	private boolean ocupado;
	private boolean mejorandoArma;
	private boolean mejoraAcabada;
	private int tiempoMejora;
	private Espada espada;
	
	// Aumento de daño
	private static final int AUMENTO_DAÑO = 2;
	
	// Constructor
	public NpcHerrero ( MapaUno mapa , float x , float y ) {
		// Crea el cuerpo
		cuerpo = BodyCreator.crearCuerpoNpc(mapa.getWorld(), this, x, y);
		
		// Posiciona la textura y le asigna un tamaño al sprite
		setBounds(0, 0, 128 / PixelGdx.PPM, 128 / PixelGdx.PPM);
		// Textura por defecto
		setRegion( new Texture(Gdx.files.internal("assets/herrero/1.png")));
		// Posición donde dibujará el sprite ( x - mitad del width y heigh del sprite )
		setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2 );		
		
		// Variable
		mejorandoArma = false;
		mejoraAcabada = false;
		tiempoMejora = 0;
		ocupado = false;
	}
	// Update
	public void update ( float delta ) {
		setPosition(cuerpo.getPosition().x - getWidth() / 2, cuerpo.getPosition().y - getHeight() / 2 );
	}
	
	// Lanza un hilo de mejora que mejorará el arma
	public void mejorarArma ( Jugador jugador ) {
		// tiempo que va a tardar en mejorar el arma
		tiempoMejora = (int)(Math.random() * 60000);
		// Espada
		this.espada = jugador.getEspada();
		// Elimina la referencia de la espada al jugador
		jugador.setEspada(null);
		// El herrero pasa a estar ocupado
		ocupado = true;
		// Mejorando
		mejorandoArma = true;
		mejoraAcabada = false;
		
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
	public Espada recogerArma ( ) {
		if ( !mejorandoArma ) {
			// Aumenta el daño de la espada
			espada.aumentarDaño(AUMENTO_DAÑO);
			
			// Reestablece las variables
			mejorandoArma = false;
			mejoraAcabada = false;
			tiempoMejora = 0;
			ocupado = false;
			// Espada auxiliar
			Espada auxEspada = espada;
			// Limpia la referencia
			espada = null;
			// Devuelve la espada
			return auxEspada;
		}
		return null;
	}
	
	//Getters
	public boolean isMejorandoArma() {
		return mejorandoArma;
	}

	public boolean isMejoraAcabada() {
		return mejoraAcabada;
	}

	public int getTiempoMejora() {
		return tiempoMejora;
	}
	
	public boolean getOcupado() {
		return ocupado;
	}

	public void setMejorandoArma(boolean mejorandoArma) {
		this.mejorandoArma = mejorandoArma;
	}

	public void setMejoraAcabada(boolean mejoraAcabada) {
		this.mejoraAcabada = mejoraAcabada;
	}


	public void setTiempoMejora(int tiempoMejora) {
		this.tiempoMejora = tiempoMejora;
	}
	
 
}
