package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import tp1.battleship.GameModel;
import tp1.battleship.Jogo;

public class Game extends Thread {

	private Socket player1, player2;
	
	public Game(Socket player1, Socket player2) {
		
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void run() {

		BufferedReader is1 = null;
		PrintWriter    os1 = null;

		BufferedReader is2 = null;
		PrintWriter    os2 = null;
		
		try { 

			System.out.println("Jogo iniciado!");
			
			is1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
			os1 = new PrintWriter(player1.getOutputStream(), true);
			os1.println("1");		
			
			is2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			os2 = new PrintWriter(player2.getOutputStream(), true);
			os2.println("2");
			
			GameModel game = new GameModel();

			// usar o caracter bell como separador em vez do \n não se deve fazer!!! porque não é genérico.
			for(;;) {			

				// Jogador 1, recebe o tabuleiro do Jogador 2
				os1.println((game.getBoard(2) + "\nSua vez -> Jogada do jogador 1:").replaceAll("\n", "\7")); 

				// Jogador 1 faz a sua jogada
				os2.println("Jogador 1 esta a escolher a sua jogada...");
				String inputLine1 = is1.readLine();
				if(inputLine1 == null)
					break;

				// É demonstrado o resultado da jogada
				String result1 = game.play(1, inputLine1);
				os1.println((game.getBoard(2)).replaceAll("\n", "\7")); 
				os1.println("Resultado: " + result1);

				// Verificar se o jogador 1 ganhou		
				if (game.checkWin(1)) {
					String result = (game.getBoard(2) + "\nVitoria do Jogador 1! Localizou os 30 navios.").replaceAll("\n", "\7");
					os1.println(result); 
					os2.println(result);
					break;
				}

				
				
				// Jogador 2, recebe o tabuleiro do Jogador 1
				os2.println((game.getBoard(1) + "\nSua vez -> Jogada do jogador 2:").replaceAll("\n", "\7")); 
				
				// Jogador 2 faz a sua jogada
				os1.println("Jogador 2 esta a escolher a sua jogada...");
				String inputLine2 = is2.readLine();
				if(inputLine2 == null)
					break;

				// É demonstrado o resultado da jogada
				String result2 = game.play(2, inputLine2);
				os2.println((game.getBoard(1)).replaceAll("\n", "\7")); 
				os2.println("Resultado: " + result2);

				// Verificar se o jogador 2 ganhou			
				if (game.checkWin(2)) {
					String result = (game.getBoard(1) + "\nVitoria do Jogador 2! Localizou os 30 navios.").replaceAll("\n", "\7");
					os1.println(result); 
					os2.println(result);
					break;
				}
			}		
		} catch (IOException e) {
			System.err.println("Erro na ligaçao : "
					+ e.getMessage());
		} finally {
			// garantir que o socket é fechado
			try {
				if (is1 != null)     is1.close();
				if (os1 != null)     os1.close();
				if (player1 != null) player1.close();
				if (is2 != null)     is2.close();
				if (os2 != null)     os2.close();
				if (player2 != null) player2.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", "
				+ player1.getRemoteSocketAddress()+ ", " + player2.getRemoteSocketAddress());
	} // end run
}
