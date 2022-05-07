import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Porfírio Filipe
 *
 */
public class Servidor {

	public final static int DEFAULT_PORT = 5025; // porto onde vai ficar á espera

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(DEFAULT_PORT);

			Socket newSock = null;

			for (;;) {
				System.out.println("Servidor TCP concorrente aguarda ligacao no porto " + DEFAULT_PORT + "...");

				// Espera connect do cliente estabelecendo o circuito virtual
				newSock = serverSocket.accept();

				Thread th = new HandleConnectionThread(newSock);
				th.start();
			}
		} catch (IOException e) {
			System.err.println("Excepção no servidor: " + e);
		}
	} // end main

} // end ServidorTCP

class HandleConnectionThread extends Thread {
	// registo dos utilizadores
	static Hashtable<String, Socket> active = new Hashtable<String, Socket>();

	private Socket connection = null;

	// testa se o socket está ligado
	private boolean isAliveSocket(Socket sk) {
		// sk.getInetAddress().isReachable(250)
		try {
			sk.setSoTimeout(250);
			if (sk.getInputStream().read() == -1)
				return false;
		} catch (IOException e) {
		}
		return true;
	}

	// executa o registo com exclusão no acesso
	private synchronized void regista(String nickName, Socket connection) {
		active.put(nickName, connection);
	}

	// envia para todos os utilizadores ativos a respetiva lista
	private synchronized void broadcast() {
		Enumeration<String> e = active.keys();
		String lista = "Lista de utilizadores:\7";
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			if (isAliveSocket(active.get(key)))
				lista = lista +"("+active.get(key).getRemoteSocketAddress()+ ") "+key + "\7"; 
			else {
				try {
					active.get(key).close(); // fecha deste lado a ligação
				} catch (IOException e1) {
					e1.printStackTrace();
				}  		 
				active.remove(key);  // remove os inativos...
			}
		}
		e = active.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			Socket sk = active.get(key);
			try {
				PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
				os.println(lista);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	public HandleConnectionThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {

		BufferedReader is = null;
		// PrintWriter os = null;

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// os = new PrintWriter(connection.getOutputStream(), true);

			String inputLine = is.readLine();

			regista(inputLine, connection);

			System.out.println("Registou: "+inputLine);
			
			// envia os utilizadores registados para todos os activos
			broadcast();

		} catch (IOException e) {
			System.err.println("Erro na ligaçao " + connection + ": " + e.getMessage());
		} finally {
			System.out.println("Terminou a Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());
			/*
			 * try { if (is != null) is.close(); if (os != null) os.close();
			 * 
			 * if (connection != null) connection.close(); } catch (IOException e) { }
			 */
		}
	} // end run

} // end HandleConnectionThread
