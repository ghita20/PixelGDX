package launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import juego.PixelGdx;

public class LauncherEscritorio {
	// Main
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// Ancho y alto
		config.width = PixelGdx.WIDTH;
		config.height = PixelGdx.HEIGHT;
		
		// Inicia el juego
		new LwjglApplication(new PixelGdx(), config);
	}
}
