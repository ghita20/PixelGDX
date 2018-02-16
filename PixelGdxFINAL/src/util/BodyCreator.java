package util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import juego.PixelGdx;

// Esta clase sirve para crear cuerpos en un objeto tipo World
public final class BodyCreator {
	
	
	// Crea los cuerpos del mapa dentro del mundo
	public static void crearCuerposMapa ( World world , TiledMap map) {
		// Variables auxiliares
		BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Recorre la capa 8 e instancia los objetos tipo Rectangulo que existen en el tileMap
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
        	// Consigue el objeto Rectangle
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // El suelo es de tipo est�tico para que est� siempre en el mismo sitio y no le afecten los golpes etc..
            bdef.type = BodyDef.BodyType.StaticBody;
            // Posicion
            bdef.position.set(
            		(rect.getX() + rect.getWidth() / 2f) / PixelGdx.PPM, // Hay que ajustar la posici�n a lo pixeles por metro establecidos
            		(rect.getY() + rect.getHeight() / 2f) / PixelGdx.PPM);
            
            // Crea la definici�n del cuerpo en el mundo
            body = world.createBody(bdef);

            // Ahora le asigna una forma de pol�gono
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
            		rect.getWidth() / 2f / PixelGdx.PPM, 
            		rect.getHeight() / 2f / PixelGdx.PPM);
            fdef.shape = shape;
            
            // Categoria
            fdef.filter.categoryBits = Colisiones.CATEGORIA_ESCENARIO;
            // M�scara para las colisiones
            fdef.filter.maskBits = Colisiones.MASK_ESCENEARIO;
            
            // Le da forma al objeto
            body.createFixture(fdef);
            
            // Dispose
            shape.dispose();
            
        }
        // Recorre la capa 8 e instancia los objetos tipo PolyLine que existen en el tileMap
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(PolylineMapObject.class)){
        	float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
        	Vector2[] worldVertices = new Vector2[vertices.length /2];
        	// Vertices
        	for (int i = 0; i < worldVertices.length; i++) 
        		worldVertices[i] = new Vector2(vertices[i*2] / PixelGdx.PPM, vertices[ i* 2 +1] / PixelGdx.PPM);
        	// Crea un nuevo shape
        	ChainShape cs = new ChainShape();
        	cs.createChain(worldVertices);
        	Shape shapePoly = cs;
        	
        	// Definici�n del cuerpo
        	Body bdy;
        	BodyDef bdyf = new BodyDef();
        	
        	// Tipo de cuerpo
        	bdyf.type = BodyType.StaticBody;
        	// Crea el cuerpo
        	bdy = world.createBody(bdyf);
        	
        	// Asigna el shape
        	fdef.shape = shapePoly;
        	
        	// Categoria
            fdef.filter.categoryBits = Colisiones.CATEGORIA_ESCENARIO;
            // M�scara para las colisiones
            fdef.filter.maskBits = Colisiones.MASK_ESCENEARIO;
        	
            // Da forma al objeto
        	bdy.createFixture(fdef);
        	
        	
        	// Dispose
        	shapePoly.dispose();
        	
        }
	}
	
	// Crea un cuerpo jugador en el mundo
	public static Body crearCuerpoJugador ( World world , Sprite sprite ) {
		// Definicion del cuerpo
		BodyDef bdef = new BodyDef();
		// Posicion por defecto
		bdef.position.set(100 / PixelGdx.PPM, 220 / PixelGdx.PPM);
		// Tipo din�mico, ya que el jugador se tiene que mover y esas cosaaas
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		// Crea el cuerpo
		Body body = world.createBody(bdef);

		// Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.friction = 0.09f; // Hace parar un poco el impulso
		
		// Forma del cuerpo
		PolygonShape shape = new PolygonShape();
		// Tama�o
		shape.setAsBox(8 / PixelGdx.PPM, 11  / PixelGdx.PPM);
		
		fdef.shape = shape;
		// Categoria Jugador
		fdef.filter.categoryBits = Colisiones.CATEGORIA_JUGADOR;
		// M�scara para las colisiones
		fdef.filter.maskBits = Colisiones.MASK_JUGADOR;
		// Crea el cuerpo
		body.createFixture(fdef).setUserData(sprite);
		
		// Devuelve el objeto Body
		return body;
	}

	// Crea la onda que genera la espada al atacar en la posici�n del jugador
	public static Body crearCuerpoOnda ( World world, Sprite sprite , float x , float y) {
		// Definici�n del cuerpo
		BodyDef bdef = new BodyDef();
		// Posici�n inicial
		bdef.position.set(x , y );
		// Tipo din�mico
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		// Crea el cuerpo
		Body cuerpo = world.createBody(bdef);
		
		// Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.friction = 1;
		fdef.density = 10;
		
		// Forma del objeto
		PolygonShape shape = new PolygonShape();
		
		// Tama�o
		shape.setAsBox(8 / PixelGdx.PPM, 10  / PixelGdx.PPM);
		
		// Filtro y m�scara
		fdef.shape = shape;
		fdef.filter.categoryBits = Colisiones.CATEGORIA_PODERES;
		fdef.filter.maskBits = Colisiones.MASK_PODERES;
		
		// Crea el objeto
		cuerpo.createFixture(fdef).setUserData(sprite);
		
		// Devuelve el cuerpo
		return cuerpo;
	}

	// Crea un cuerpo para los enemigos
	public static Body crearCuerpoEnemigo ( World world , Sprite sprite , float x , float y , float width , float heigh ) {
		// Definici�n del cuerpo
		BodyDef bdef = new BodyDef();
		// Posici�n de inicio
		bdef.position.set(x , y );
		// Tipo din�mico
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		// Crea el cuerpo
		Body cuerpo = world.createBody(bdef);
		
		// Filtro para las colisiones..etc
		FixtureDef fdef = new FixtureDef();
		fdef.friction = 0.3f; // Hace parar un poco el impulso
		fdef.filter.groupIndex = -1; // Never collide
		
		// Forma
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / PixelGdx.PPM, heigh  / PixelGdx.PPM);
		
		fdef.shape = shape;
		fdef.filter.categoryBits = Colisiones.CATEGORIA_ENEMIGO;
		fdef.filter.maskBits = Colisiones.MASK_ENEMIGO;
		
		// Fixture
		cuerpo.createFixture(fdef).setUserData(sprite);
		
		// Devuelve el cuerpo
		return cuerpo;
		
	}
	
}
