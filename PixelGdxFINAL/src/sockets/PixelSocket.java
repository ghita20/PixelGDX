package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import otros.Espada;

public abstract class PixelSocket {
	
	// Socket remoto
	protected Socket socketRemoto;
	
	// IP y Puerto
	protected String ip;
	protected int puerto;
	protected boolean esServidor;
	
	// Objeto que recibe del otro lado
	protected DatosSincronizar datosRecibidos;
	protected Ataque ataque; // Señal que se envia del servidor al cliente para decirle que el jugador ha sido atacado
	protected boolean atacando; // Booleano para saber si el jugador ha atacado (un poco chapuza...)
	protected DatoLoot nuevoloot; // Aqui almacena si le llega del servidor algún loot nuevo
	protected Espada espada; // Si el jugador cliente recoge una espada en el servidor se almacena aquí
	protected int addMoneda; // Añade una moneda si el valor es 1
	
	// Constructor
	public PixelSocket( String ip , int puerto , boolean esServidor ) {
		this.ip = ip;
		this.puerto = puerto;
		this.esServidor = esServidor;
		ataque = null;
		atacando = false;
		espada = null;
		addMoneda = 0;
	}
	
	// Lanza un hilo que está permanentemente leyendo del socket
	protected void lanzarHiloEscucha() {
		// Espera a leer objetos
        new Thread( new Runnable() {
        	@Override
        	public void run() {
        		// InputStream del socket remoto
        		ObjectInputStream inputStreamRemoto;
        		try {
        			// Bucle infinito
        			while ( true )
        				// Si el socket es != null significa que estamos conectados
        				if ( socketRemoto!= null && socketRemoto.isConnected()) {
        					inputStreamRemoto = new ObjectInputStream(socketRemoto.getInputStream());
        					// Recoge el objeto leído
        					Object objetoRecibido = inputStreamRemoto.readObject();
        					// Si el objeto recibido es != de null y es de tipo DatosSincronizar
        					if ( objetoRecibido != null )
	        					if ( objetoRecibido instanceof DatosSincronizar )
	        						// Almacena los datos en la variable datosRecibidos
	        						datosRecibidos = (DatosSincronizar)objetoRecibido;
	        					else if (objetoRecibido instanceof Ataque ) 
	        						ataque = (Ataque)objetoRecibido;
	        					else if ( objetoRecibido instanceof Boolean ) 
	        							atacando = (boolean)objetoRecibido;
	        					else if (objetoRecibido instanceof DatoLoot ) 
	        						nuevoloot = (DatoLoot)objetoRecibido;	
	        					else if ( objetoRecibido instanceof Integer )
	        						addMoneda = (int)objetoRecibido;
	        					else if ( objetoRecibido instanceof Espada )
	        						espada = (Espada)objetoRecibido;
        			}// fin while
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}

        	}
        }).start();
	}

	// Envia un objeto al socket
	public void enviarDatos ( Object datosEnviar ) {
		// OutputStream
		ObjectOutputStream outStream;
		try {
			if ( socketRemoto != null ) {
				// OutputStream del socket remoto
				outStream = new ObjectOutputStream(socketRemoto.getOutputStream());
				// Envía el objeto
				outStream.writeObject(datosEnviar);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	// Recoge datos
	public DatosSincronizar recogerDatos ( ) {
		return datosRecibidos;
	}
	public Ataque getAtaque() {
		// Si ha recibido la señal de que el jugador ha sido atacado
		if ( ataque != null ) {
			Ataque auxAtaque = ataque;
			// Cambia el estado de la variable
			ataque = null;
			return auxAtaque;
		}
		return null;
	}
	public boolean getAtacando ( ) {
		// Recojo la referencia
		boolean auxAtacando = atacando;
		// Cambio el estado a false
		atacando = false;
		return auxAtacando;
	}
	public DatoLoot recogerLoot ( ) {
		DatoLoot auxL = nuevoloot;
		nuevoloot = null;
		return auxL;
	}
	public Espada recogerEspada ( ) {
		// Variable auxiliar
		Espada auxE = espada;
		// Si hemos recibido anteriormente una espada
		if ( espada != null )
			/* Ponemos la variable espada a null para indicar 
			   que no hemos recibido ninguna espada nueva */
			espada = null;
		// Devolvemos la variable auxiliar
		return auxE;
	}
	public int recogerMoneda ( ) {
		// Si tengo una nueva moneda para añadir la devuelvo
		if ( addMoneda == 1 ) {
			addMoneda = 0;
			return 1;
		}
		// Si no nada..
		return 0;
	}
	public boolean getEsServidor() {
		return esServidor;
	}
	public Socket getSocketRemoto() {
		return socketRemoto;
	}

}
