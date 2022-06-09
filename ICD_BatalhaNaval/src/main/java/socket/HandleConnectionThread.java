package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.ParserConfigurationException;

import battleship.GameModel;
import protocol.MessageCreator;
import protocol.MessageProcessor;
import session.Profile;
import session.Session;

public class HandleConnectionThread extends Thread {

	public static ArrayList<GameModel> activeGames = new ArrayList<>();
	public static Hashtable<String, ThreadReader> activeUsers = new Hashtable<>();
	private ThreadReader reader;
	private Socket user;
	private BufferedReader is;
	private PrintWriter os;
	private boolean reading;
	private Semaphore semaphore;

	public HandleConnectionThread(Socket user, Semaphore semaphore) {

		this.semaphore = semaphore;
		this.reading = true;
		this.reader = null;
		this.user = user;
		try {
			this.is = new BufferedReader(new InputStreamReader(user.getInputStream()));
			this.os = new PrintWriter(user.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		Semaphore semaphore = new Semaphore(0);
		reader = new ThreadReader(user, is, os, semaphore);
		reader.start();
		
		String request = "";

		while (reading) {

			try {
				semaphore.acquire();
				request = reader.getRequest();
				sendResponse((request.replaceAll("\r", "\6")).replaceAll("\n", "\7"));

			} catch (InterruptedException | ParserConfigurationException | IOException e) {
				e.printStackTrace();
			}	
		}
	}

	public void sendResponse(String request) throws ParserConfigurationException, IOException {

		// Primeiro argumento representa o tipo de pedido
		String method = MessageProcessor.process(request).split(",")[0]; 

		//System.out.println("Metodo -> " + method);
		
		// - Login - //
		if (method.equals("Login"))
			login(request);

		// - Register - //
		else if (method.equals("Register"))
			register(request);

		// - Find Game - //
		else if (method.equals("FindGame"))
			findGame(request);

		// - Upload Profile - //
		else if (method.equals("Upload"))
			upload(request);
		
		// - Play - //
		else if (method.equals("Play"))
			play(request);
		
		// - Get Board - //
		else if (method.equals("GetBoard"))
			board(request);
	}

	private void play(String request) throws ParserConfigurationException {
		
		String player   = MessageProcessor.process(request).split(",")[1]; // Identificacao do Jogador
		String position = MessageProcessor.process(request).split(",")[2]; // Posição
		
		ArrayList<GameModel> activeGames = GameQueueThread.activeGames;
		GameModel game = null;
		
		for (GameModel g : activeGames) {
			String username1 = g.getUsernames().get(0);
			String username2 = g.getUsernames().get(1);
			
			if (player.equals(username1) || player.equals(username2)) {
				game = g;
				break;
			}
		}
		
		String result = game.play(player, position);  // Aplicação da jogada		
		os.println(MessageCreator.messagePlay(player, position, result));
	}
	
	private void board(String request) throws ParserConfigurationException {
		
		String player = MessageProcessor.process(request).split(",")[1]; // Identificacao do Jogador
		String view   = MessageProcessor.process(request).split(",")[2]; // Tipo de visualização		
		
		ArrayList<GameModel> activeGames = GameQueueThread.activeGames;	
		GameModel game = null;
		
		for (GameModel g : activeGames) {
			
			String username1 = g.getUsernames().get(0);
			String username2 = g.getUsernames().get(1);

			if (player.equals(username1) || player.equals(username2)) {
				game = g;
				break;
			}
		}
		
			HashMap<String, List<List<Integer>>> board = (view.equals("true") ? game.getBoardPositionsView(player)
					  														  : game.getBoardPositions(player));

			os.println(MessageCreator.messageBoard(player, view, game.getPoints(player), game.getOpponentPoints(player), board));
	}
	
	private void login(String request) throws ParserConfigurationException {

		String username = MessageProcessor.process(request).split(",")[1];
		String password = MessageProcessor.process(request).split(",")[2];
		String name = "", picture = "", color = "", date = "";

		String result = "";

		if (!Session.availableNickname(username)) {
			if (Session.login(username, password)) {

				name	= Profile.getName(username);
				picture = Profile.getPicture(username);
				color   = Profile.getColor(username);
				date 	= Profile.getDate(username);
				
				result = "Sucesso!";
				System.out.println("O utilizador " + Profile.getName(username) + " entrou no jogo.");

			} else
				result = "Erro: Palavra-passe incorreta!";
		} else
			result = "Erro: Nome de utilizador não está disponível";

		os.println(MessageCreator.messageSession(username, name, password, picture, color, date, false, result));
	}

	private void register(String request) throws ParserConfigurationException {

		String username = MessageProcessor.process(request).split(",")[1];
		String name 	= MessageProcessor.process(request).split(",")[2];
		String password = MessageProcessor.process(request).split(",")[3];
		String color	= MessageProcessor.process(request).split(",")[4];
		String date		= MessageProcessor.process(request).split(",")[5];
		String picture  = MessageProcessor.process(request).split(",")[6];

		String result = "";

		// Encriptação da palavra-passe
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String hashPassword = DatatypeConverter.printHexBinary(digest).toUpperCase();

		if (Session.availableNickname(username)) {
			Session.register(username, name, hashPassword, color, date, picture);
			result = "Sucesso!";
			System.out.println("O utilizador " + name + " entrou no jogo.");
		} else
			result = "Erro: Nickname em uso.";

		os.println(MessageCreator.messageSession(username, name, hashPassword, color, date, picture, true, result));
	}

	private void findGame(String request) throws ParserConfigurationException, IOException {

		String username = MessageProcessor.process(request).split(",")[1];
		
		if (!activeUsers.containsKey(username)) {
			activeUsers.put(username, this.reader);
		}

		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		os.println(MessageCreator.messageFind(username, "DONE"));
	}

	// TODO
	private void upload(String request) throws ParserConfigurationException {

		String contentType = MessageProcessor.process(request).split(",")[1];
		String username = MessageProcessor.process(request).split(",")[2];
		String value = MessageProcessor.process(request).split(",")[3];

		if (contentType.equals("picture")) {
			Profile.uploadProfilePicture(username, value);
			String result = "Foto de perfil atualizada com sucesso. Por favor reinicie a aplicacao.";
			os.println(MessageCreator.messageUpload(contentType, username, value, result));
		}
	}

	class ThreadReader extends Thread {

		private Socket user;
		private PrintWriter os;
		private BufferedReader is;
		private String request;
		private Semaphore semaphore;

		public ThreadReader(Socket user, BufferedReader is, PrintWriter os, Semaphore semaphore) {
			
			this.user = user;
			this.semaphore = semaphore;
			this.request = "";
			this.is = is;
			this.os = os;
		}

		public String getRequest() {
			return this.request;
		}
		
		public BufferedReader getBufferedReader() {
			return this.is;
		}
		
		public PrintWriter getPrintWriter() {
			return this.os;
		}

		public Socket getSocket() {
			return this.user;
		}
		
		public Semaphore getSemaphore() {
			return semaphore;
		}

		public void run() {

			try {
				for (;;) {

					String line = (String) is.readLine();
					if (line == null)
						break;
					
					//System.out.println("Pedido: " + line);
					this.request = line;
					semaphore.release();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
