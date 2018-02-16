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

public class Inicio implements Screen{
	// Skin
	private Stage stage;
	private Skin skin;
	
	// Constructor
	public Inicio( PixelGdx game ) {
		// Skin - Comic
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("assets/skins/comic/skin/comic-ui.json"));
		
		Gdx.input.setInputProcessor( stage );
		

        Image img = new Image( new Texture("assets/pantalla_inicio.png"));
        
        stage.addActor(img);
		
		final TextButton button = new TextButton("Iniciar partida", skin, "default");
        button.setBounds(430, 180, 300, 70);
        
        button.addListener( new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {

				stage.addAction(Actions.sequence( Actions.fadeOut(1), Actions.run( new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						game.setScreen( new MapaUno(game) );
						Gdx.input.setInputProcessor(null);
					}
				}) ));
				
        	}
        });
        
        stage.addActor(button);
        
        final TextButton buttonS = new TextButton("Salir", skin, "default");
        buttonS.setBounds(430, 80, 250, 70);
        
        buttonS.addListener( new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {

				stage.addAction(Actions.sequence( Actions.fadeOut(1), Actions.run( new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						System.exit(0);
					}
				}) ));
				
        	}
        });
		
        stage.addActor(buttonS);
        
		
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
