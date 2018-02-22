package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCliente extends PixelSocket{

	// Constructor
	public SocketCliente(String ip, int puerto) {
		super(ip, puerto, false);
		// Conecta con el servidor
		try {
			this.socketRemoto = new Socket(ip, puerto);
			// Una vez conectado lanza el hilo de escucha
			lanzarHiloEscucha();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
