package hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import juego.PixelGdx;
import pantallas.MapaUno;
import sprites.Jugador;

// Imprime en pantalla el estado del jugador. Vida,yang...etc
public class StatsJugador {
	
	// Debud del Stage	
	public static final boolean DEBUG = true;
	
	// Stage
	public Stage stage;
	private Skin skin; // Estilos
	private Viewport viewPort; // Adapta el stage a la pantalla actual
	
	// Jugador
	private Jugador jugador;
	
	private ArrayList<Cell> vidas; // Almacena las celdas
	private ArrayList<Corazon> corazones; // Referencia al coraz�n de cada celda
	
	// Numero de corazones ( vida m�xima del jugador * nr de puntos de vida que puede almacenar un coraz�n )
	private final static int NUMERO_CORAZONES = Jugador.MAX_VIDA / Corazon.MAX_PUNTOS;
	
	// Constructor
	public StatsJugador ( SpriteBatch sb , Jugador jugador ) {
		// Jugador
		this.jugador = jugador;
		
		// Estilos 
		skin = new Skin(Gdx.files.internal("assets/skins/comic/skin/comic-ui.json"));
		
		// ViwePort y Stage ( FitViewPort para que se adapte automaticamente )
		viewPort = new FitViewport(PixelGdx.WIDTH , PixelGdx.HEIGHT , new OrthographicCamera());
		stage = new Stage(viewPort,sb);
		
		// Tabla principal
		Table table = new Table();
		
		// Alineada arriba a la izquierda
		table.left().top();
		
		// Fill Parent ( Expandir )
		table.setFillParent(true);
		
		// Debug
		table.setDebug(DEBUG);
		
		// Hacemos un arraylist en paralelo para tener referencia al objeto coraz�n referenciado en cada celda
		vidas = new ArrayList<>();
		corazones = new ArrayList<>();
		
		// Corazones
		table.row().left().top();
		// El n�mero de corazones es = a la vida / por la cantidad de puntos que puede almacenar un coraz�n ( en teor�a el jugador siempre empieza con todos los corazones llenos de vida )
		int numeroCorazones = jugador.getPuntosDeVida() / Corazon.MAX_PUNTOS;
		for ( int i = 0 ; i < numeroCorazones ; i++ ) {
			// Instancia cada corazon
			Corazon auxCorazon = new Corazon();
			corazones.add(auxCorazon);
			
			Image auxI = auxCorazon.getCorazon();
			if ( i == 0)
				vidas.add( table.add( auxI ).padTop(20).padLeft(20).width(40f).height(40f)  );
			else
				vidas.add( table.add( auxI ).padTop(20).width(40f).height(40f) );
		}
		
//		// Bot�n men�
//		final TextButton button = new TextButton("Opciones", skin, "default");
//        button.setBounds(430, 160, 350, 70);
//        
//        table.add(button).right();
		
		
		// A�ade la tabla al stage
		stage.addActor(table);
	}
	
	// Dispose
	public void dispose ( ) {
		stage.dispose();
	}

}
