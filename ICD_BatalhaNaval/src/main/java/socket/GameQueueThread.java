package socket;

import battleship.GameModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import socket.HandleConnectionThread.ThreadReader;

public class GameQueueThread extends Thread {

	// Jogos de Batalha Naval a decorrer...
	public static ArrayList<GameModel> activeGames = new ArrayList<>();
	private Semaphore semaphore;
	
	public GameQueueThread(Semaphore semaphore) {	
		this.semaphore = semaphore;
	}
	
	public void run() {
		
		// Utilizadores ativos, prontos a jogar
		Hashtable<String, ThreadReader> activeUsers = HandleConnectionThread.activeUsers;
		
		while (true) {

			if (activeUsers.keySet().size() >= 2) {
				
				String username1 = (String) activeUsers.keySet().toArray()[0]; // Nickname do jogador 1
				String username2 = (String) activeUsers.keySet().toArray()[1]; // Nickname do jogador 2
				
				activeUsers.remove(username1);
				activeUsers.remove(username2);

				System.out.println("Iniciar jogo entre " + username1 + " e " + username2);

				activeGames.add(new GameModel(username1, username2, new Semaphore(0)));
				semaphore.release(2);
			}
		}
	}
}
