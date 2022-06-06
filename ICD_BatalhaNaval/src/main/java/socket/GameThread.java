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

// TODO JA NAO ESTOU A USAR

public class GameThread extends Thread {

	private ThreadReader reader1, reader2;
	private Semaphore semaphore1, semaphore2;
	private Socket player1, player2;
	private BufferedReader is1, is2;
	private PrintWriter os1, os2;
	private String username1, username2;
	private boolean playing;

	public GameThread(ThreadReader reader1, ThreadReader reader2, String username1, String username2) {
		
		this.reader1 = reader1;
		this.reader2 = reader2;
		this.semaphore1 = reader1.getSemaphore();
		this.semaphore2 = reader2.getSemaphore();
		this.player1 = reader1.getSocket();
		this.player2 = reader2.getSocket();
		this.username1 = username1;
		this.username2 = username2;
		this.playing = true;
			
		this.is1 = reader1.getBufferedReader();
		this.os1 = reader1.getPrintWriter();
		
		this.is2 = reader2.getBufferedReader();
		this.os2 = reader2.getPrintWriter();
	}

	public void run() {

		GameModel game = new GameModel(this.username1, this.username2);
		String request1 = "", request2 = "";

		try {
			
			// Inicio - Primeira vez
			
			//semaphore1.acquire();
			//request1 = reader1.getRequest();
			//sendResponsePlayer1(request1, game, "1"); // Indicar ao jogador que é o jogador número 1

			//semaphore2.acquire();
			//request2 = reader2.getRequest();
			//sendResponsePlayer2(request2, game, "2"); // Indicar ao jogador que é o jogador número 2			
			
			for (int i = 0; i < 2; i++)	{
				semaphore1.acquire();
				request1 = reader1.getRequest();
				System.out.println("Pedido jogador 1: " + request1);
				sendResponsePlayer1(request1, game);
			}

			for (int i = 0; i < 2; i++)	{
				semaphore2.acquire();
				request2 = reader2.getRequest();
				System.out.println("Pedido jogador 2: " + request2);
				sendResponsePlayer2(request2, game);
			}
			
			// Fim - Primeira vez
			
			while (playing) {
				
				// --- Jogada do jogador 1 --- 
				
				for (int i = 0; i < 3; i++) {
					semaphore1.acquire();
					request1 = reader1.getRequest();
					System.out.println("Pedido jogador 1: " + request1);
					sendResponsePlayer1(request1, game);
				}
				
				//semaphore1.acquire();
				//request1 = reader1.getRequest();
				//sendResponsePlayer1(request1, game, "Sua vez -> Introduza uma jogada: ");
				
				//semaphore2.acquire();
				//request2 = reader2.getRequest();
				//sendResponsePlayer2(request2, game, "O outro jogador está a escolher a posição.");
				
				// Verificar se o jogador 1 ganhou
				/*if (game.checkWin("1")) {
					
					sendResponsePlayer1(request1, game, "        ");
					sendResponsePlayer1(request1, game, winMessage(game, "1"));
					sendResponsePlayer2(request1, game, winMessage(game, "1"));
					Profile.uploadWinsNumber(nickname1);
					break;
				}*/
				

				
				// --- Jogada do jogador 2 ---
				
				//semaphore1.acquire();
				//request1 = reader1.getRequest();
				//sendResponsePlayer1(request1, game, "O outro jogador está a escolher a posição.");
				
				//semaphore2.acquire();
				//request2 = reader2.getRequest();
				//sendResponsePlayer2(request2, game, "Sua vez -> Introduza uma jogada: ");
				
				// Verificar se o jogador 1 ganhou
				/*if (game.checkWin("2")) {

					sendResponsePlayer1(request2, game, winMessage(game, "2"));
					sendResponsePlayer2(request2, game, winMessage(game, "2"));
					Profile.uploadWinsNumber(nickname2);
					break;
				}*/
				
				for (int i = 0; i < 3; i++) {
					semaphore2.acquire();
					request2 = reader2.getRequest();
					System.out.println("Pedido jogador 2: " + request2);
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
		
			response = MessageCreator.messageBoard(player, view, game.getPoints(player), game.getOpponentPoints(player), board);
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
		System.out.println("Resposta jogador 1: " + response);
		os1.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendResponsePlayer2(String request, GameModel game, String info) throws ParserConfigurationException {
		
		String response = response(request, game, info);
		os2.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}
	
	public void sendResponsePlayer2(String request, GameModel game) throws ParserConfigurationException {
		
		String response = response(request, game, "");
		System.out.println("Resposta jogador 2: " + response);
		os2.println((response.replaceAll("\r", "\6")).replaceAll("\n", "\7"));
	}

	public String winMessage(GameModel game, String player) {
				
		if (game.checkWin("1")) return "Vitoria do Jogador 1! Localizou os 30 navios.\n";
		else return "Vitoria do Jogador 2! Localizou os 30 navios.\n";		
	}
}
