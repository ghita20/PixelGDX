package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
	protected boolean atacando;
	
	// Constructor
	public PixelSocket( String ip , int puerto , boolean esServidor ) {
		this.ip = ip;
		this.puerto = puerto;
		this.esServidor = esServidor;
		ataque = null;
		atacando = false;
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
        				if ( socketRemoto!= null ) {
        					inputStreamRemoto = new ObjectInputStream(socketRemoto.getInputStream());
        					// Recoge el objeto leído
        					Object objetoRecibido = inputStreamRemoto.readObject();
        					// Si el objeto recibido es != de null y es de tipo DatosSincronizar
        					if ( objetoRecibido!= null && objetoRecibido instanceof DatosSincronizar )
        						// Almacena los datos en la variable datosRecibidos
        						datosRecibidos = (DatosSincronizar)objetoRecibido;
        					else if ( objetoRecibido!= null && objetoRecibido instanceof Ataque ) {
        						ataque = (Ataque)objetoRecibido;
        					}else if ( objetoRecibido!= null && objetoRecibido instanceof Boolean ) {
        							atacando = (boolean)objetoRecibido;
        					}
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
		boolean auxAtacando = atacando;
		atacando = false;
		return auxAtacando;
	}
	public boolean getEsServidor() {
		return esServidor;
	}

}
