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
	
	public static void sendReply(GameModel game, PrintWriter os, BufferedReader is) throws ParserConfigurationException, IOException {
		
		String request = is.readLine();									   // Lê o Request do jogador
		if (request == null) return;									   // Caso o jogador tenha fechado o socket, cancela
		String method  = MessageProcessor.process(request).split(",")[0];  // Primeiro argumento representa o tipo de pedido
		String player = "", argument = "", result = "";
		
		if (method.equals("position")) {
			
			player   = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			argument = MessageProcessor.process(request).split(",")[2]; // Posição da jogada
			result   = game.play(player, argument);						// Aplicação da jogada
		} 
		else if (method.equals("board")) {
			
			player   = MessageProcessor.process(request).split(",")[1]; // Número do jogador
			argument = MessageProcessor.process(request).split(",")[2]; // Tipo de tabuleiro
			result   = (argument.equals("true")) ? game.getBoardView(player) : game.getBoard(player);
		}
		
		String reply = MessageCreator.messageBoard(player, argument, result, true);
		os.println(reply.replaceAll("\r", "\6").replaceAll("\n", "\7"));
	}
	
	public void run() {

		BufferedReader is1 = null, is2 = null;
		PrintWriter    os1 = null, os2 = null;

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

			GameThread.sendReply(game, os1, is1);  // Enviar o próprio tabuleiro ao jogador 1	
			GameThread.sendReply(game, os1, is1);  // Enviar tabuleiro do adversário ao jogador 1
					
			GameThread.sendReply(game, os2, is2);  // Enviar o próprio tabuleiro ao jogador 2	
			GameThread.sendReply(game, os2, is2);  // Enviar tabuleiro do adversário ao jogador 2		
			
			for(;;) {			

				// --------- Jogador 1 --------- //

				os1.println("Sua vez -> Introduza uma jogada:");
				GameThread.sendReply(game, os1, is1);  // Responde ao pedido da jogada do jogador 1
				GameThread.sendReply(game, os1, is1);  // Responde ao pedido do tabuleiro adversário do jogador 1

				// Verificar se o jogador 1 ganhou		
				if (game.checkWin("1")) {
					String result = (game.getBoard("1") + "\nVitoria do Jogador 1! Localizou os 30 navios.").replaceAll("\n", "\7");
					os1.println(result); 
					os2.println(result);
					break;
				}

				// --------- Jogador 2 --------- //

				os2.println("Sua vez -> Introduza uma jogada:");
				GameThread.sendReply(game, os2, is2);  // Responde ao pedido da jogada do jogador 2
				GameThread.sendReply(game, os2, is2);  // Responde ao pedido do tabuleiro adversário do jogador 2

				// Verificar se o jogador 2 ganhou			
				if (game.checkWin("2")) {
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
