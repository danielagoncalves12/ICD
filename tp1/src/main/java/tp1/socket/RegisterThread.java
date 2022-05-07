package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class RegisterThread extends Thread {

	public static Hashtable<String, Socket> activeUsers = new Hashtable<String, Socket>();
	private Socket user;
	
	public RegisterThread(Socket user) {
		this.user = user;
	}
	
	public void run() {
		
		BufferedReader is = null;
		PrintWriter    os = null;
		
		try {
			is = new BufferedReader(new InputStreamReader(user.getInputStream()));
			os = new PrintWriter(user.getOutputStream(), true);

			os.println("Registo: Bem-vindo jogador! Introduza o seu nickname: ");
			String nickname = is.readLine();
			
			login(nickname, user);
			os.println("registered");
			
			System.out.println("Utilizador registado: " + nickname);
			
			// Verifica se existe dois utilizadores registados e activos para começar um jogo
			startGame();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	private synchronized void login(String nickname, Socket user) {
		activeUsers.put(nickname, user);
	}

	public synchronized void startGame() {

		if (activeUsers.keySet().size() >= 2) {
					
			String nickname1 = (String) activeUsers.keySet().toArray()[0]; // Nickname do jogador 1
			String nickname2 = (String) activeUsers.keySet().toArray()[1]; // Nickname do jogador 2
			Socket player1 = (Socket) activeUsers.values().toArray()[0];   // Socket do jogador 1
			Socket player2 = (Socket) activeUsers.values().toArray()[1];   // Socket do jogador 2
			
			activeUsers.remove(nickname1);
			activeUsers.remove(nickname2);
			
			System.out.println("A iniciar jogo: " + nickname1 + " vs " + nickname2 + ".");
			
			new GameThread(player1, player2).start(); // Iniciar o jogo
		}
	}
}
