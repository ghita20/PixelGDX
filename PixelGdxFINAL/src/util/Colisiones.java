package util;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import juego.PixelGdx;
import sprites.Jugador;

public class Colisiones implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		// Recogemos los Fixture de los dos objetos que han entrado en contacto
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		// Juntamos las categorias
		int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
		switch ( cDef ) {
		// Choque entre jugador y escenario
		case PixelGdx.CATEGORIA_JUGADOR | PixelGdx.CATEGORIA_ESCENARIO :
			Jugador jugador = null;
			
			if ( fixtureA.getUserData() instanceof Jugador ) 
				jugador = (Jugador) fixtureA.getUserData();
			if ( fixtureB.getUserData() instanceof Jugador ) 
				jugador = (Jugador) fixtureB.getUserData();
			
			// Al tocar el jugador con el escenario se reinician los saltos
			jugador.reiniciarSaltos();
			break;
		}

	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}

}
