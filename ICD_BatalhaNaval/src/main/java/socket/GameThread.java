package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.ParserConfigurationException;

import battleship.GameModel;
import protocol.MessageCreator;
import protocol.MessageProcessor;
import session.Profile;
import socket.MenuThread.ThreadReader;

public class GameThread extends Thread {

	private ThreadReader reader1, reader2;
	private Semaphore semaphore1, semaphore2;
	private Socket player1, player2;
	private BufferedReader is1, is2;
	private PrintWriter os1, os2;
	private String nickname1, nickname2;
	private boolean playing;

	public GameThread(ThreadReader reader1, ThreadReader reader2, String nickname1, String nickname2) {
		
		this.reader1 = reader1;
		this.reader2 = reader2;
		this.semaphore1 = reader1.getSemaphore();
		this.semaphore2 = reader2.getSemaphore();
		this.player1 = reader1.getSocket();
		this.player2 = reader2.getSocket();
		this.nickname1 = nickname1;
		this.nickname2 = nickname2;
		this.playing = true;
			
		this.is1 = reader1.getBufferedReader();
		this.os1 = reader1.getPrintWriter();
		
		this.is2 = reader2.getBufferedReader();
		this.os2 = reader2.getPrintWriter();
		
		//try {
			// BufferedReader e PrintWriter do jogador 1
			//this.is1 = new BufferedReader(new InputStreamReader(this.player1.getInputStream()));
			//this.os1 = new PrintWriter(this.player1.getOutputStream(), true);	

			// BufferedReader e PrintWriter do jogador 2
			//this.is2 = new BufferedReader(new InputStreamReader(this.player2.getInputStream()));
			//this.os2 = new PrintWriter(this.player2.getOutputStream(), true);		

		//} catch (IOException e) { e.printStackTrace(); }	
	}
	
	public void run() {

		GameModel game = new GameModel();
		String request1 = "", request2 = "";

		try {
			System.out.println("tou aqui");
			
			semaphore1.acquire();
			request1 = reader1.getRequest();
			sendResponsePlayer1(request1, game, "1"); // Indicar ao jogador que é o jogador número 1
		
			semaphore2.acquire();
			request2 = reader2.getRequest();
			sendResponsePlayer2(request2, game, "2"); // Indicar ao jogador que é o jogador número 2
			
			for (int i = 0; i < 3; i++)	{
				semaphore1.acquire();
				request1 = reader1.getRequest();
				sendResponsePlayer1(request1, game);
			}
				
			for (int i = 0; i < 3; i++)	{
				semaphore2.acquire();
				request2 = reader2.getRequest();
				sendResponsePlayer2(request2, game);
			}
			
			while (true) {
				
				semaphore1.acquire();
				request1 = reader1.getRequest();
				sendResponsePlayer1(request1, game, "Sua vez -> Introduza uma jogada: ");
				
				// Verificar se o jogador 1 ganhou
				if (game.checkWin("1")) {
					
					sendResponsePlayer1(request1, game, "        ");
					sendResponsePlayer1(request1, game, winMessage(game, "1"));
					sendResponsePlayer2(request1, game, winMessage(game, "1"));
					Profile.uploadWinsNumber(nickname1);
					break;
				}
				
				for (int i = 0; i < 3; i++) {
					semaphore1.acquire();
					request1 = reader1.getRequest();
					sendResponsePlayer1(request1, game);
				}
				
				semaphore2.acquire();
				request2 = reader2.getRequest();
				sendResponsePlayer2(request2, game, "Sua vez -> Introduza uma jogada: ");
				
				// Verificar se o jogador 1 ganhou
				if (game.checkWin("2")) {

					sendResponsePlayer1(request2, game, winMessage(game, "2"));
					sendResponsePlayer2(request2, game, winMessage(game, "2"));
					Profile.uploadWinsNumber(nickname2);
					break;
				}
				
				for (int i = 0; i < 3; i++) {
					semaphore2.acquire();
					request2 = reader2.getRequest();
					sendResponsePlayer2(request2, game);
				}
			}
				
		} catch (InterruptedException | ParserConfigurationException e1) {			
			e1.printStackTrace();
		}  

	}
	
	public String response(String request, GameModel game, String info) throws ParserConfigurationException {
		
		String method   = MessageProcessor.process(request).split(",")[0];
		String response = "";
		
		// Resposta a um pedido de jogada
		if (method.equals("Play")) {
			
			String player   = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String position = MessageProcessor.process(request).split(",")[2]; // Posição		
			String result   = game.play(player, position);					   // Aplicação da jogada		
			
			response = MessageCreator.messagePlay(player, position, result);
		}
		
		// Resposta a um pedido de tabuleiro
		else if (method.equals("Board")) {

			String player = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String view   = MessageProcessor.process(request).split(",")[2]; // Tipo de visualização		
			HashMap<String, List<List<Integer>>> board = (view.equals("true") ? game.getBoardPositionsView(player)
																			  : game.getBoardPositions(player));
		
			response = MessageCreator.messageBoard(player, view, game.getPoints("1"), game.getPoints("2"), board);
		}
		
		// Resposta a um pedido de informacao
		else if (method.equals("Info")) {

			String player = MessageProcessor.process(request).split(",")[1]; // Número do Jogador
			String result = info;

			response = MessageCreator.messageInfo(player, result);
		}	
		return response;
	}
	
	public void sendResponsePlayer1(String request, GameModel game, String info) throws ParserConfigurationException {

		String response = response(request, game, info);
		os1.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendResponsePlayer1(String request, GameModel game) throws ParserConfigurationException {

		String response = response(request, game, "");
		os1.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendResponsePlayer2(String request, GameModel game, String info) throws ParserConfigurationException {
		
		String response = response(request, game, info);
		os2.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendResponsePlayer2(String request, GameModel game) throws ParserConfigurationException {
		
		String response = response(request, game, "");
		os2.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendReply(GameModel game, PrintWriter os, BufferedReader is) throws ParserConfigurationException, IOException {
		sendReply(game, os, is, "");
	}
	
	public void sendReply(GameModel game, PrintWriter os, BufferedReader is, String info) throws ParserConfigurationException, IOException {
		
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
				
		if (game.checkWin("1")) return "Vitoria do Jogador 1! Localizou os 30 navios.\n";
		else return "Vitoria do Jogador 2! Localizou os 30 navios.\n";		
	}
}
