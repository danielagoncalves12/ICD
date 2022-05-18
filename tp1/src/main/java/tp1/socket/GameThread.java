package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import tp1.battleship.GameModel;
import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;

public class GameThread extends Thread {

	private Socket player1, player2;
	private BufferedReader is1, is2;
	private PrintWriter os1, os2;

	public GameThread(Socket player1, Socket player2) {		
		this.player1 = player1;
		this.player2 = player2;
			
		try {
			// BufferedReader e PrintWriter do jogador 1
			this.is1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
			this.os1 = new PrintWriter(player1.getOutputStream(), true);	
			
			// BufferedReader e PrintWriter do jogador 2
			this.is2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			this.os2 = new PrintWriter(player2.getOutputStream(), true);		
			
		} catch (IOException e) { e.printStackTrace(); }	
	}
	
	public void run() {

		try { 
			System.out.println("Jogo iniciado!");
			
			// Instância do jogo Batalha Naval
			GameModel game = new GameModel();
			GameThread.sendReply(game, os1, is1, "1"); // Indicar ao jogador que é o jogador número 1
			GameThread.sendReply(game, os2, is2, "2"); // Indicar ao jogador que é o jogador número 2

			GameThread.sendReply(game, os1, is1);      // Enviar o próprio tabuleiro ao jogador 1	
			GameThread.sendReply(game, os1, is1);      // Enviar tabuleiro do adversário ao jogador 1
					
			GameThread.sendReply(game, os2, is2);      // Enviar o próprio tabuleiro ao jogador 2	
			GameThread.sendReply(game, os2, is2);      // Enviar tabuleiro do adversário ao jogador 2		
			
			for(;;) {			
				// --------- Jogador 1 --------- //
				GameThread.sendReply(game, os1, is1, "Sua vez -> Introduza uma jogada: ");
				GameThread.sendReply(game, os1, is1);  // Responde ao pedido da jogada do jogador 1
				
				// Verificar se o jogador 1 ganhou		
				if (game.checkWin("1")) {
					GameThread.sendReply(game, os1, is1, "");
					GameThread.sendReply(game, os1, is1, winMessage(game, "1"));
					GameThread.sendReply(game, os2, is2, winMessage(game, "1"));
					break;
				}			
				GameThread.sendReply(game, os1, is1);  // Responde ao pedido do tabuleiro adversário do jogador 1

				// --------- Jogador 2 --------- //			
				GameThread.sendReply(game, os2, is2, "Sua vez -> Introduza uma jogada: ");
				GameThread.sendReply(game, os2, is2);  // Responde ao pedido da jogada do jogador 2
				
				// Verificar se o jogador 2 ganhou	
				if (game.checkWin("2")) {
					GameThread.sendReply(game, os1, is1, winMessage(game, "2"));
					GameThread.sendReply(game, os2, is2, "");
					GameThread.sendReply(game, os2, is2, winMessage(game, "2"));
					break;
				}				
				GameThread.sendReply(game, os2, is2);  // Responde ao pedido do tabuleiro adversário do jogador 2					
			}					
			
		} catch (IOException | ParserConfigurationException e) {
			System.err.println("Erro na ligaçao : " + e.getMessage());
		} finally {
			// Garantir que o socket é fechado
			try {
				if (is1 != null)     is1.close();
				if (os1 != null)     os1.close();
				if (player1 != null) player1.close();
				if (is2 != null)     is2.close();
				if (os2 != null)     os2.close();
				if (player2 != null) player2.close();
			} catch (IOException e) {
				System.err.print(e.getStackTrace());
			}
		}
		try {
			if (is1 != null)     is1.close();
			if (os1 != null)     os1.close();
			if (player1 != null) player1.close();
			if (is2 != null)     is2.close();
			if (os2 != null)     os2.close();
			if (player2 != null) player2.close();
		} catch (IOException e) {
			System.err.print(e.getStackTrace());
		}
		
		System.out.println("Terminou a Thread " + this.getId() + ", " + player1.getRemoteSocketAddress()+ ", " + player2.getRemoteSocketAddress());
	}
	
	public static void sendReply(GameModel game, PrintWriter os, BufferedReader is) throws ParserConfigurationException, IOException {
		sendReply(game, os, is, "");
	}
	
	public static void sendReply(GameModel game, PrintWriter os, BufferedReader is, String info) throws ParserConfigurationException, IOException {
		
		String request = is.readLine();									   // Lê a mensagem de Request do jogador
		String method  = MessageProcessor.process(request).split(",")[0];  // Primeiro argumento representa o tipo de pedido

		// Resposta a um pedido de jogada
		if (method.equals("Play")) {
			
			String player   = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String position = MessageProcessor.process(request).split(",")[2]; // Posição		
			String result   = game.play(player, position);					   // Aplicação da jogada	
			
			String reply = MessageCreator.messagePlay(player, position, result);
			os.println(reply.replaceAll("\r", "\6").replaceAll("\n", "\7"));
		} 
		
		// Resposta a um pedido de tabuleiro
		else if (method.equals("Board")) {

			String player = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String view   = MessageProcessor.process(request).split(",")[2]; // Tipo de visualização		
			HashMap<String, List<List<Integer>>> board = (view.equals("true") ? game.getBoardPositionsView(player)
																			  : game.getBoardPositions(player));
		
			String reply = MessageCreator.messageBoard(player, view, game.getPoints("1"), game.getPoints("2"), board);
			os.println(reply.replaceAll("\r", "\6").replaceAll("\n", "\7"));
		}
		else if (method.equals("Info")) {

			String player = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String result = info;
			
			String reply = MessageCreator.messageInfo(player, result);
			os.println(reply.replaceAll("\r", "\6").replaceAll("\n", "\7"));
		}
	}
	
	public String winMessage(GameModel game, String player) {
				
		if (game.checkWin("1")) return "Vitoria do Jogador 1! Localizou os 30 navios.";
		else return "Vitoria do Jogador 2! Localizou os 30 navios.";		
	}
}
