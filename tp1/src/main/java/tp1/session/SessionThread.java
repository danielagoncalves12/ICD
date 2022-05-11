package tp1.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

import tp1.socket.GameThread;

public class SessionThread extends Thread {

	public static Hashtable<String, Socket> activeUsers = new Hashtable<String, Socket>();
	private Socket user;
	private BufferedReader is;
	private PrintWriter os;
	
	public SessionThread(Socket user) {		
		this.user = user;
		try {
			this.is = new BufferedReader(new InputStreamReader(user.getInputStream()));
			this.os = new PrintWriter(user.getOutputStream(), true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void run() {

		try {
			os.println("Batalha Naval: Introduza o seu nickname: ");
			String nickname = is.readLine();
						
			login(nickname, user);
			
			// Verifica se existe dois utilizadores registados e activos para começar um jogo
			startGame();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	private synchronized void login(String nickname, Socket user) throws Exception {

		// Se a conta já existir, pede para iniciar sessão
		if (!Session.availableNickname(nickname)) {
					
			os.println("Bem-vindo de volta " + nickname + "! Introduza a sua palavra-passe:");
			String password = is.readLine();
			if (!Session.login(nickname, password)) {
				os.println("Palavra-passe incorreta!");
				return;
			}
			os.println("Sucesso!");
		}
		// Caso contrário, o novo utilizador é registado
		else { 		
			os.println("Bem-vindo " + nickname + "! Escolha uma palavra-passe:");
			String password = is.readLine();
			Session.register(nickname, password, "");
			os.println("Inscrito com sucesso!");
		}		
		System.out.println("O utilizador " + nickname + " entrou no jogo.");
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
