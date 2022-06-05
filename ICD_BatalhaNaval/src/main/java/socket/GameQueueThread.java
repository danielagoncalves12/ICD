package socket;

import java.util.Hashtable;
import socket.MenuThread.ThreadReader;

public class GameQueueThread extends Thread {

	public void run() {
		
		Hashtable<String, ThreadReader> activeUsers = MenuThread.activeUsers;
		
		while (true) {

			if (activeUsers.keySet().size() >= 2) {
				
				String nickname1 = (String) activeUsers.keySet().toArray()[0]; // Nickname do jogador 1
				String nickname2 = (String) activeUsers.keySet().toArray()[1]; // Nickname do jogador 2
				ThreadReader player1 = (ThreadReader) activeUsers.values().toArray()[0];   // Socket do jogador 1
				ThreadReader player2 = (ThreadReader) activeUsers.values().toArray()[1];   // Socket do jogador 2
				
				activeUsers.remove(nickname1);
				activeUsers.remove(nickname2);

				System.out.println("Iniciar jogo entre " + nickname1 + " e " + nickname2);

				// Inicia um jogo, para os dois jogadores
				new GameThread(player1, player2, nickname1, nickname2).start(); // Iniciar o jogo
			}
		}
	}
}
