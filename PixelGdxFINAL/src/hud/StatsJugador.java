package hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import juego.PixelGdx;
import otros.Espada;
import pantallas.MapaUno;
import sprites.Jugador;
import sprites.NpcHerrero;

// Imprime en pantalla el estado del jugador. Vida,yang...etc
public class StatsJugador {
	
	// Debud del Stage	
	public static final boolean DEBUG = false;
	
	// Stage
	public Stage stage;
	private Skin skin; // Estilos
	private Viewport viewPort; // Adapta el stage a la pantalla actual
	
	// Dialogo NPC
	private Dialog dialogoNpcUno; // Dialogo donde presenta la oferta de mejorar el arma
	private Dialog dialogoNpcMejorando; // Dialogo donde informa que está en proceso de mejora
	private Dialog dialogoNpcAcabado; // Dialogo que informa de que la mejora ha acabado
	private Dialog dialogoNpcNoTienesDinero; // Dialogo que informa de que no tiene dinero
	private NpcHerrero npc;
	
	// Jugador
	private Jugador jugador;
	
	private ArrayList<Cell> vidas; // Almacena las celdas
	private ArrayList<Corazon> corazones; // Referencia al corazón de cada celda
	private Label numeroMonedas;
	
	// Numero de corazones ( vida máxima del jugador * nr de puntos de vida que puede almacenar un corazón )
	private final static int NUMERO_CORAZONES = Jugador.MAX_VIDA / Corazon.MAX_PUNTOS;
	
	// Constructor
	public StatsJugador ( SpriteBatch sb , Jugador jugador , NpcHerrero npc ) {
		// Jugador
		this.jugador = jugador;
		// Npc Herrero
		this.npc = npc;
		
		// Estilos 
		skin = new Skin(Gdx.files.internal("assets/skins/clean-crispy/skin/clean-crispy-ui.json"));
		
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
		
		// Hacemos un arraylist en paralelo para tener referencia al objeto corazón referenciado en cada celda
		vidas = new ArrayList<>();
		corazones = new ArrayList<>();
		
		// Corazones
		table.row().left().top();
		// El número de corazones es = a la vida / por la cantidad de puntos que puede almacenar un corazón ( en teoría el jugador siempre empieza con todos los corazones llenos de vida )
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
		
		// Monedas
		table.row().left().bottom();
		// Imagen
		table.add(new Image(new Texture(Gdx.files.internal("assets/stats/moneda.png")))).padLeft(20);
		// Label con el número de moneda
		
		// Fuente de texto
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/raleway.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		// Tamaño de la fuente
		parameter.size = 18;
		// Genera la fuente
		BitmapFont font12 = generator.generateFont(parameter); 
		// Dispose
		generator.dispose(); 
		
		// Número de monedas
		numeroMonedas = new Label("0", new Label.LabelStyle(font12, Color.DARK_GRAY));
		numeroMonedas.setWrap(true);
		
		// Añade el label a la tabla
		table.add(numeroMonedas).fill();
		
		// Añade la tabla al stage
		stage.addActor(table);
		
		// Crea los dialogos
		crearDialogos();
		
	}
	
	// Crea los dialogos
	private void crearDialogos ( ) {
		// Primer dialogo
		dialogoNpcUno = new Dialog("Mejora tu arma!", skin) {
			@Override
			protected void result(Object object) {
				// Elimina el cuadro de Dialogo
				remove();
				
				// Comprueba el resultado
				if ( object.equals("si")) {
					try {
						// intenta retirar el dinero
						jugador.restarMonedas(20);
						// si consigue retirarlo mejora el arma
						npc.mejorarArma(jugador);
					}catch ( IllegalArgumentException e ) {
						dialogoNpcNoTienesDinero.show(stage);
						return;
					}
				}
				
				if ( object.equals("no"))
					System.out.println("Pues no quiere el tio...");;
			}
		};
		dialogoNpcUno.text("Si me das tu espada la mejorare, pero tardare un tiempo...\nEl precio de la mejora es de 20 monedas de oro.");
		dialogoNpcUno.button("Vale","si");
		dialogoNpcUno.button("No quiero","no");
		
		// Segundo dialogo
		dialogoNpcMejorando = new Dialog("Mejora tu arma!", skin) {
			@Override
			protected void result(Object object) {
				// Elimina el cuadro de Dialogo
				remove();
			}
		};
		dialogoNpcMejorando.text("Tu arma está en proceso de mejora...");
		dialogoNpcMejorando.button("Entendido","si");
		
		// Tercer dialogo
		dialogoNpcAcabado = new Dialog("Mejora tu arma!", skin) {
			@Override
			protected void result(Object object) {
				// Elimina el cuadro de Dialogo
				remove();
				// Recoge la espada del herrero
				Espada espada = npc.recogerArma();
				System.out.println(espada);
				// Se la devuelve al jugador
				jugador.setEspada(espada);
			}
		};
		dialogoNpcAcabado.text("He terminado de mejorar tu arma!");
		dialogoNpcAcabado.button("Recoger arma","recoger");

		
		// Dialogo no tienes dinero
		dialogoNpcNoTienesDinero = new Dialog("Mejora tu arma!", skin) {
			@Override
			protected void result(Object object) {
				// Elimina el cuadro de Dialogo
				remove();
			}
		};
		dialogoNpcNoTienesDinero.text("No tienes el dinero suficiente para la mejora...!");
		dialogoNpcNoTienesDinero.button("Entendido...","");
		
	}
	// Refresca los corazones que se tienen que imprimir en pantall
	public void refrescarStats ( ) {
		// Refresca la cantidad de monedas
		refrescarMonedas();
		
		// Puntos del vida del jugador
		int numVidas = jugador.getPuntosDeVida();
		int vidasCorazones = 0;
		
		// Esto por si revivimos a full vida
		if ( numVidas == Jugador.MAX_VIDA ) {
			for ( int i = 0 ; i < numVidas/4 ; i++ ) {
				// Full vida todos los corazones
				corazones.get(i).setVida(Corazon.MAX_PUNTOS);
				// Refresca imagen
				vidas.get( i ).setActor( corazones.get(i).getCorazon());
			}
			return;
		}
		
		// Calcula cuantos puntos de vida tenemos almacenados en los corazones
		for ( int i = corazones.size()-1; i>=0 ; i-- ) 
			vidasCorazones += corazones.get(i).getVida();
		
		// Si el numVidas es = al numero de puntos de vida que tenemos en los corazones sale del método
		if ( numVidas == vidasCorazones ) 
			return;
		
		// Resta las vidas necesarias para sincronizar los corazones con los puntos de vida del jugador
		if ( numVidas < vidasCorazones ) {
			int iCorazon = NUMERO_CORAZONES -1; // Indice corazones
			for ( int i = vidasCorazones ; i > numVidas ; i-- ) {
				while ( iCorazon > 0 && corazones.get(iCorazon).getVida() == 0)
					--iCorazon;
					
				// Resta el punto de vida
				corazones.get(iCorazon).restarPunto();
				// Cambia la imagen
				vidas.get( iCorazon ).setActor( corazones.get(iCorazon).getCorazon());
				
			}
		}
	}
	
	private void refrescarMonedas() {
		// Actualiza el número de monedas
		numeroMonedas.setText( jugador.getCantidadMonedas() +"");
	}

	// Dispose
	public void dispose ( ) {
		stage.dispose();
	}
	
	// Muestra el dialogo del npc en pantalla
	public void mostrarDialogoNpc ( ) {
		// Para que se puedan pulsar los botones
		Gdx.input.setInputProcessor( stage );
		// Si no está ocupado
		if ( !npc.getOcupado() )
			dialogoNpcUno.show(stage);
		else {
			// Si ha acabado la mejora
			if ( npc.isMejoraAcabada() )
				dialogoNpcAcabado.show(stage);
			else // Si no...
				dialogoNpcMejorando.show(stage);
		}
		
	}

}
