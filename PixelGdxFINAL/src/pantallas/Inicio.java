package pantallas;

import java.awt.Dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import juego.PixelGdx;
import util.GestionAudio;

public class Inicio implements Screen{
	// Skin
	private Stage stage;
	private Skin skin;
	
	// Modo de juego
	private boolean modoServidor;
	
	// Constructor
	public Inicio( PixelGdx game ) {
		// Skin - Comic
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("assets/skins/clean-crispy/skin/clean-crispy-ui.json"));
		
		// Por defecto el juego arranca en modo servidor
		modoServidor = true;
		
		// Para que se pueda clickear en los botones
		Gdx.input.setInputProcessor( stage );
		
		// Imagen de fondo
        Image imagenFondo = new Image( new Texture("assets/pantalla_inicio.png"));
        // Añade la imagen
        stage.addActor(imagenFondo);
	
        // Añade los botones
        agregarBotones(game);
	}
	
	// Añade los botones a la pantalla
	private void agregarBotones ( PixelGdx game ) {
		// Boton Iniciar Partida
		final TextButton btnIniciar = new TextButton("Iniciar partida", skin, "default");
		// Posición
		btnIniciar.setBounds(430, 230, 200, 40);
		// Listener
		btnIniciar.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Reproduce el sonido de la moneda
				GestionAudio.SONIDO_MONEDA.play();
				// Instancia el socket
				game.setConexionSocket(modoServidor);
				// Pone la pantalla principal
				game.setScreen( new MapaUno(game) );
				Gdx.input.setInputProcessor(null);
			}
		});
		// Añade el botón a la pantalla
		stage.addActor(btnIniciar);

		// Boton Modo de juego
		final TextButton btnModo = new TextButton("Modo Servidor", skin, "default");
		btnModo.setBounds(430, 180, 200, 40);
		// Listener
		btnModo.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				modoServidor = !modoServidor;
				btnModo.setText( modoServidor?"Modo Servidor":"Modo Cliente");
			}
		});
		// Añade el botón
		stage.addActor(btnModo);

		// Botón salir
		final TextButton buttonS = new TextButton("Salir", skin, "default");
		buttonS.setBounds(430, 130, 200, 40);
		// Listener
		buttonS.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
					
				stage.addAction(Actions.sequence( Actions.delay(1), Actions.run( new Runnable() {
					@Override
					public void run() {
						System.exit(0);
					}
				}) ));

			}
		});
		// Añade el botón
		stage.addActor(buttonS);
		
		// Botón Modo Debug
		final TextButton buttonDebug = new TextButton("Debug OFF", skin, "default");
		buttonDebug.setBounds(1090, 560, 100, 40);
		// Listener
		buttonDebug.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				PixelGdx.DEBUG = !PixelGdx.DEBUG;
				buttonDebug.setText(PixelGdx.DEBUG?"Debug ON":"Debug OFF");
			}
		});
		// Añade el botón
		stage.addActor(buttonDebug);
	}

	private void update(float arg0) {
		// TODO Auto-generated method stub
		stage.act(arg0);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float arg0) {
		// TODO Auto-generated method stub
		update(arg0);
		stage.draw();
	}	


	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
