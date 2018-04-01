package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
public class SocketServidor extends PixelSocket{
	
	// IP y Puerto por defecto
	public static final String IP 	= "localhost";
	public static final int PUERTO 	= 61000;
	
	// Socket servidor
	private ServerSocket socketServidor;

	// Constructor
	public SocketServidor() {
		super(IP, PUERTO, true);
		// Crea el socket servidor
		try {
			socketServidor = new ServerSocket(PUERTO, 1, InetAddress.getByName(IP));
			// Aún nadie se ha conectado a nuestro servidor
			socketRemoto = null;
			// Espera a que un cliente se conecte
			esperarCliente();
			// Lanza el hilo de escucha
			lanzarHiloEscucha();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Espera a que alguien se conecte al socket
	private void esperarCliente() {
		try {
			// Acepta la conexión y obtiene la referencia al socket
			socketRemoto = socketServidor.accept();
			// Información adicional
			String clientAddress = socketRemoto.getInetAddress().getHostAddress();
			System.out.println("\r\nAlguien se ha conectado. IP: " + clientAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
