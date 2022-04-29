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
			
			is2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			os2 = new PrintWriter(player2.getOutputStream(), true);
			
			GameModel game = new GameModel();
			
			for(;;) {			
				// usar o caracter bell como separador em vez do \n
				// não se deve fazer!!! porque não é genérico.

				// Jogador 1, recebe o tabuleiro do Jogador 2
				os1.println((game.getBoard(2) + "\nJogada do jogador 1:").replaceAll("\n", "\7")); 
			
				String inputLine1 = is1.readLine();
				if(inputLine1 == null)
					break;

				os1.println("Resultado: " + game.play(1, inputLine1));
				os1.println((game.getBoard(2)).replaceAll("\n", "\7")); 
				
				//JG.jogar(Short.parseShort(inputLine1), 'X');
				//JG.mostrar();
				
				/*
				if(JG.vitoria('X')) {
					String resp=(JG.JogoToTXT() + "\nVitória do X!").replaceAll("\n", "\7");
					os1.println(resp); 
					os2.println(resp);
					break;
				}
				else
					if(JG.empate()) {
						String resp=(JG.JogoToTXT()+"\nEmpate!").replaceAll("\n", "\7");
						os1.println(resp); 
						os2.println(resp);
						break;
					}
				*/
				
				os2.println((game.getBoard(1) + "\nJogada do jogador 2:").replaceAll("\n", "\7")); 
				
				String inputLine2 = is2.readLine();
				if(inputLine2 == null)
					break;

				os2.println("Resultado: " + game.play(2, inputLine2));
				os2.println((game.getBoard(1)).replaceAll("\n", "\7")); 
				
				/*
				JG.jogar(Short.parseShort(inputLine2), 'O');
				JG.mostrar();
				*/
				/*
				if(JG.vitoria('O')) {
					String resp=(JG.JogoToTXT()+"\nVitória do O!").replaceAll("\n", "\7");
					os1.println(resp); 
					os2.println(resp);
					break;
				}
				else
					if(JG.empate()) {
						String resp=(JG.JogoToTXT()+"\nEmpate!").replaceAll("\n", "\7");
						os1.println(resp); 
						os2.println(resp);
						break;
					}*/
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
