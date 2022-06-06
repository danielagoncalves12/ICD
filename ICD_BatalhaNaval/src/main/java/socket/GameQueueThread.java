package socket;

import battleship.GameModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import socket.MenuThread.ThreadReader;

public class GameQueueThread extends Thread {

	// Jogos de Batalha Naval a decorrer...
	public static ArrayList<GameModel> activeGames = new ArrayList<>();
	private Semaphore semaphore;
	
	public GameQueueThread(Semaphore semaphore) {	
		this.semaphore = semaphore;
	}
	
	public void run() {
		
		// Utilizadores ativos, prontos a jogar
		Hashtable<String, ThreadReader> activeUsers = MenuThread.activeUsers;
		
		while (true) {

			if (activeUsers.keySet().size() >= 2) {
				
				String username1 = (String) activeUsers.keySet().toArray()[0]; // Nickname do jogador 1
				String username2 = (String) activeUsers.keySet().toArray()[1]; // Nickname do jogador 2
				//ThreadReader player1 = (ThreadReader) activeUsers.values().toArray()[0];   // Socket do jogador 1
				//ThreadReader player2 = (ThreadReader) activeUsers.values().toArray()[1];   // Socket do jogador 2
				
				activeUsers.remove(username1);
				activeUsers.remove(username2);

				System.out.println("Iniciar jogo entre " + username1 + " e " + username2);

				// Inicia um jogo, para os dois jogadores
				//GameThread game = new GameThread(player1, player2, nickname1, nickname2); // Iniciar o jogo
				//game.start();

				activeGames.add(new GameModel(username1, username2));
				semaphore.release(2);
			}
		}
	}
}
