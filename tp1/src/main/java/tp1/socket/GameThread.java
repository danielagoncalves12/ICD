package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;

import tp1.battleship.GameModel;
import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;

public class GameThread extends Thread {

	private Socket player1, player2;

	public GameThread(Socket player1, Socket player2) {
		
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void run() {

		BufferedReader is1 = null;
		PrintWriter    os1 = null;

		BufferedReader is2 = null;
		PrintWriter    os2 = null;
		
		MessageProcessor msgProcessor = new MessageProcessor();
		MessageCreator   msgCreator   = new MessageCreator();
		
		try { 

			System.out.println("Jogo iniciado!");
			
			// BufferedReader e PrintWriter do jogador 1
			is1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
			os1 = new PrintWriter(player1.getOutputStream(), true);
			os1.println("1");		
			
			// BufferedReader e PrintWriter do jogador 2
			is2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			os2 = new PrintWriter(player2.getOutputStream(), true);
			os2.println("2");
			
			// Instância do jogo Batalha Naval
			GameModel game = new GameModel();

			
			
			os1.println(game.getBoardView(1).replaceAll("\n", "\7"));  // Enviar tabuleiro do jogador 1
			
			// Enviar tabuleiro do adversário ao jogador 1
			String      board1 = is1.readLine();
			String     player1 = msgProcessor.process(board1);
			String replyBoard1 = msgCreator.messageBoard(player1, game.getBoard(player1), true);			
			os1.println(replyBoard1.replaceAll("\r", "\6").replaceAll("\n", "\7"));

			
			os2.println(game.getBoardView(2).replaceAll("\n", "\7"));  // Enviar tabuleiro do jogador 2
			
			// Enviar tabuleiro do adversário ao jogador 2
			String      board2 = is2.readLine();
			String     player2 = msgProcessor.process(board2);
			String replyBoard2 = msgCreator.messageBoard(player2, game.getBoard(player2), true);			
			os2.println(replyBoard2.replaceAll("\r", "\6").replaceAll("\n", "\7"));

			
			
			for(;;) {			

				// --------- Jogador 1 --------- //
				
				// Jogador 1, recebe o tabuleiro do Jogador 2
				os1.println(("Sua vez -> Introduza uma jogada:").replaceAll("\n", "\7")); 

				// Jogada do jogador 1
				String msg1 = is1.readLine();
				if (msg1 == null) break;

				// Processa o pedido recebido (posicao) e envia uma mensagem de resposta ao jogador
				player1 = msgProcessor.process(msg1).split(",")[0];
				String position1 = msgProcessor.process(msg1).split(",")[1];		
				String replyPos1 = msgCreator.messagePosition(player1, position1, game.play(player1, position1), true);
				os1.println(replyPos1.replaceAll("\n", "\7"));	   // Envia o resultado da jogada para o jogador 1
							
				// Pedido do tabuleiro para o jogador 1
				msg1 = is1.readLine(); if (msg1 == null) break;
				
				// Processa o pedido recebido (tabuleiro) e envia uma mensagem de resposta ao jogador
				player1 = msgProcessor.process(msg1);
				board1 = game.getBoard(player1);
				replyBoard1 = msgCreator.messageBoard(player1, board1, true);
				os1.println(replyBoard1.replaceAll("\r", "\6").replaceAll("\n", "\7")); // Envia o tabuleiro para o jogador 1

				// Verificar se o jogador 1 ganhou		
				if (game.checkWin(1)) {
					String result = (game.getBoard("1") + "\nVitoria do Jogador 1! Localizou os 30 navios.").replaceAll("\n", "\7");
					os1.println(result); 
					os2.println(result);
					break;
				}

				// --------- Jogador 2 --------- //
				
				// Jogador 2, recebe o tabuleiro do Jogador 1
				os2.println(("Sua vez -> Introduza uma jogada:").replaceAll("\n", "\7")); 

				// Jogada do jogador 2
				String msg2 = is2.readLine();
				if (msg2 == null) break;

				// Processa o pedido recebido (posicao) e envia uma mensagem de resposta ao jogador
				player2 = msgProcessor.process(msg2).split(",")[0];
				String position2 = msgProcessor.process(msg2).split(",")[1];		
				String replyPos2 = msgCreator.messagePosition(player2, position2, game.play(player2, position2), true);
				os2.println(replyPos2.replaceAll("\n", "\7"));	   // Envia o resultado da jogada para o jogador 1
							
				// Pedido do tabuleiro para o jogador 2
				msg2 = is2.readLine(); if (msg2 == null) break;
				
				// Processa o pedido recebido (tabuleiro) e envia uma mensagem de resposta ao jogador
				player2 = msgProcessor.process(msg2);
				board2  = game.getBoard(player2);
				replyBoard2 = msgCreator.messageBoard(player2, board2, true);
				os2.println(replyBoard2.replaceAll("\r", "\6").replaceAll("\n", "\7")); // Envia o tabuleiro para o jogador 2

				// Verificar se o jogador 2 ganhou			
				if (game.checkWin(2)) {
					String result = (game.getBoard("2") + "\nVitoria do Jogador 2! Localizou os 30 navios.").replaceAll("\n", "\7");
					os1.println(result); 
					os2.println(result);
					break;
				}
			}		
		} catch (IOException | ParserConfigurationException e) {
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
