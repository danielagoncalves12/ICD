package socket;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.ParserConfigurationException;

import battleship.GameModel;
import protocol.MessageCreator;
import protocol.MessageProcessor;
import session.Profile;
import session.Session;

public class HandleConnectionThread extends Thread {

	public static ArrayList<GameModel> activeGames = new ArrayList<>();
	public static ArrayList<String> activeUsers = new ArrayList<>();
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

		//System.out.println(request);
		
		// Primeiro argumento representa o tipo de pedido
		String method = MessageProcessor.process(request).split(",")[0]; 

		switch (method) {
		
		case "Login"   : login(request); break;
		case "Register": register(request); break;
		case "FindGame": findGame(request); break;
		case "Upload"  : upload(request); break;
		case "Play"    : play(request); break;
		case "GetBoard": board(request); break;
		case "HonorBoard" : honorBoard(request); break;
		case "ProfileInfo": profile(request); break;
		case "GetPlayers" : players(request); break;
		
		}
	}
	
	private void players(String request) throws ParserConfigurationException {
		
		ArrayList<String> players = Profile.getAllPlayersUsername();
		os.println(MessageCreator.messageGetPlayers(players));
	}
	
	private void profile(String request) throws ParserConfigurationException {
		
		String username = MessageProcessor.process(request).split(",")[1];

		HashMap<String, String> profileInfo = new HashMap<>();
		profileInfo.put("Name", Profile.getName(username));
		profileInfo.put("Color", Profile.getColor(username));
		profileInfo.put("Date", Profile.getDate(username));
		profileInfo.put("Picture", Profile.getPicture(username));
		profileInfo.put("WinsNum", Profile.getWinNum(username));
		
		os.println(MessageCreator.messageGetProfileInfo(username, profileInfo));
	}
	
	private void honorBoard(String request) throws ParserConfigurationException {
		
		HashMap<String, String> players = Profile.getHonorBoard();
		
		String[] names      = new String[10];
		String[] pictures   = new String[10];
		String[] winsNumber = new String[10];
		
		Object[] usernames = players.keySet().toArray();
		
		for (int i = 0; i < usernames.length; i++) {
			
			String username = (String) usernames[i];
			names[i] 	    = Profile.getName(username);
			pictures[i]     = Profile.getPicture(username);
			winsNumber[i]   = Profile.getWinNum(username);
		}
		os.println(MessageCreator.messageHonorBoard(names, pictures, winsNumber));
	}

	private void play(String request) throws ParserConfigurationException {
		
		String gameID   = MessageProcessor.process(request).split(",")[1]; // Identificacao do Jogo
		String player   = MessageProcessor.process(request).split(",")[2]; // Identificacao do Jogador
		String position = MessageProcessor.process(request).split(",")[3]; // Posicao
		
		ArrayList<GameModel> activeGames = GameQueueThread.activeGames;
		GameModel gameTarget = null;
		
		for (GameModel game : activeGames) {
			
			String id = game.getGameID();
			
			if (id.equals(gameID)) {
				gameTarget = game;
				break;
			}
		}
		if (gameTarget == null) os.println(MessageCreator.messagePlay(gameID, player, position, "Este jogo já terminou"));
		
		String result = gameTarget.play(player, position);  // Aplicacao da jogada		
		os.println(MessageCreator.messagePlay(gameID, player, position, result));
	}
	
	private void board(String request) throws ParserConfigurationException {
		
		String gameID = MessageProcessor.process(request).split(",")[1]; // Identificacao do Jogo
		String player = MessageProcessor.process(request).split(",")[2]; // Identificacao do Jogador
		String view   = MessageProcessor.process(request).split(",")[3]; // Tipo de visualizacao		
		
		ArrayList<GameModel> activeGames = GameQueueThread.activeGames;	
		GameModel gameTarget = null;
		
		for (GameModel game : activeGames) {
			
			String id = game.getGameID();

			if (id.equals(gameID)) {
				gameTarget = game;
				break;
			}
		}
	
		HashMap<String, List<List<Integer>>> board = (view.equals("true") ? gameTarget.getBoardPositionsView(player)
				  														  : gameTarget.getBoardPositions(player));
		os.println(MessageCreator.messageBoard(gameID, player, view, board));
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
			result = "Erro: Nome de utilizador nao esta disponivel";

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

		// Conversao da imagem para base64
		byte[] image = Base64.getDecoder().decode(picture);
		BufferedImage img = null;
		
		try {
			String filename = username + ".png";
			img = ImageIO.read(new ByteArrayInputStream(image));
			File out = new File("src/main/webapp/pictures/" + filename);
			ImageIO.write(img, "png", out);

			picture = filename;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Encriptacao da palavra-passe
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
		if (!activeUsers.contains(username)) {
			activeUsers.add(username);
			System.out.println("A procura de jogo " + username);
			System.out.println("ActiveUsers -> " + activeUsers);
		}
		else {
			os.println(MessageCreator.messageFind(username, "ERROR"));
			System.out.println("Ja estou a procura de jogo " + username);
			return;
		}
		
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Jogo encontrado para " + username + ", ID: " + GameQueueThread.getGameID());
		os.println(MessageCreator.messageFind(username, GameQueueThread.getGameID()));
	}

	private void upload(String request) throws ParserConfigurationException {

		String contentType = MessageProcessor.process(request).split(",")[1];
		String username    = MessageProcessor.process(request).split(",")[2];
		String value 	   = MessageProcessor.process(request).split(",")[3];
				
		String result = Profile.upload(contentType, username, value);
		os.println(MessageCreator.messageUpload(contentType, username, value, result));
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
