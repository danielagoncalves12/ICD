package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 */

public class ConnectionThread extends Thread {
	
	private Socket con1, con2;
	private Hashtable<String, Socket> activePlayers = new Hashtable<String, Socket>();

	public ConnectionThread(Socket con1) {
		this.con1 = con1;
		//this.con2 = con2;
	}

	private boolean isSocketAlive(Socket socket) {
		
		try {
			socket.setSoTimeout(250);
			if (socket.getInputStream().read() == -1) return false;
			} catch (IOException e) {}
		return true;		
	}
	
	private synchronized void regista(String nickName, Socket connection) {
		activePlayers.put(nickName, connection);
	}
	
	private synchronized void broadcast() {
		Enumeration<String> e = activePlayers.keys();
		String lista = "Lista de utilizadores: \7";
	
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			if (isSocketAlive(activePlayers.get(key)))
				lista = lista + "(" + activePlayers.get(key).getRemoteSocketAddress() + ")" + key + "\7";
			else {
				try {
					activePlayers.get(key).close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
				activePlayers.remove(key);
			}
		}
		
	}
	
	public void run() {

		BufferedReader is1 = null;
		PrintWriter    os1 = null;

		try {
			System.out.println("Entrou um utilizador, IP: " + con1.getRemoteSocketAddress());

			// Jogador 1
			is1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
			os1 = new PrintWriter(con1.getOutputStream(), true);
			
			String inputLine = is1.readLine();
			regista(inputLine, con1);
			System.out.println("Bem-vindo: " + inputLine + "!");
			
			// Envia os utilizadores registados para todos os utilizadores ativos
			broadcast();
			
			//System.out.println("Servidor: Recebi '" + inputLine + "'");
			//os.println("@" + inputLine.toUpperCase()); // converte para maiusculas
														
		} catch (IOException e) {
			System.err.println("Erro na ligação ->" + e.getMessage());
		} finally {
			// garantir que o socket está fechado
			try {
				if (is1 != null) is1.close();
				if (os1 != null) os1.close();				
				if (con1 != null) con1.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", " + con1.getRemoteSocketAddress());
	}
}
