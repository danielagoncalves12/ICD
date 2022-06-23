package socket;

import battleship.GameModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import socket.HandleConnectionThread.ThreadReader;

public class GameQueueThread extends Thread {

	// Jogos de Batalha Naval a decorrer...
	public static ArrayList<GameModel> activeGames = new ArrayList<>();
	private static String gameID;
	private Semaphore semaphore;
	
	public GameQueueThread(Semaphore semaphore) {	
		this.semaphore = semaphore;
		gameID = UUID.randomUUID().toString();
	}
	
	public static String getGameID() {
		return gameID;
	}
	
	public void run() {
		
		// Utilizadores ativos, prontos a jogar
		ArrayList<String> activeUsers = HandleConnectionThread.activeUsers;
		
		while (true) {

			int numPlayers = activeUsers.size();
			if (numPlayers >= 2) {
				
				String username1 = (String) activeUsers.get(0); // Username do jogador 1
				String username2 = (String) activeUsers.get(1); // Username do jogador 2
				activeUsers.remove(username1);
				activeUsers.remove(username2);

				System.out.println("Iniciar jogo entre " + username1 + " e " + username2);
				
				activeGames.add(new GameModel(gameID, username1, username2));
				try {
					Thread.sleep(500);
					semaphore.release(2);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameID = UUID.randomUUID().toString();
		}
	}
}
