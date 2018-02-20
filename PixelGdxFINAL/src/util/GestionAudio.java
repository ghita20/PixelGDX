package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestionAudio {
	// Sonido de la moneda
	public static final Sound SONIDO_MONEDA;
	// Sonido al atacar
	public static final Sound SONIDO_ESPADA;
	// Musica de fondo
	public static final Music MUSICA_FONDO;
	
	// Instancia los sonidos
	static {
		// Moneda
		SONIDO_MONEDA = Gdx.audio.newSound(Gdx.files.internal("assets/sonidos/moneda.wav"));
		// Espada
		SONIDO_ESPADA = Gdx.audio.newSound(Gdx.files.internal("assets/sonidos/espada.mp3"));
		// Musica de fondo
		MUSICA_FONDO = Gdx.audio.newMusic(Gdx.files.internal("assets/sonidos/fondo.mp3"));
		MUSICA_FONDO.setLooping(true);
	}


}
