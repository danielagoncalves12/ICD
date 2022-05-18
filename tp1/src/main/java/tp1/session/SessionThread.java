package tp1.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;
import tp1.socket.GameThread;

public class SessionThread extends Thread {

	public static Hashtable<String, Socket> activeUsers = new Hashtable<String, Socket>();
	private Socket user;
	private BufferedReader is;
	private PrintWriter os;
	private String nickname;
	
	public SessionThread(Socket user) {		
		this.user = user;
		try {
			this.is = new BufferedReader(new InputStreamReader(user.getInputStream()));
			this.os = new PrintWriter(user.getOutputStream(), true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void run() {

		try {
			if (login()) // Inicia sessão, fazendo login ou criando uma conta
				menu();  // Espera pela escolha do jogador (Iniciar novo jogo ou editar perfil)

		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

	// TODO PROTOCOLO
	private synchronized void menu() throws IOException, ParserConfigurationException {
		
		String option = is.readLine();
		
		switch(option) {
		
		case "1":		
			activeUsers.put(nickname, user);  // Utilizar ativo e à espera para jogar
			startGame();					  // Iniciar jogo
			break;
			
		case "2":
			editProfile();
			menu();
			break;		
		}
	}

	private synchronized void editProfile() throws IOException, ParserConfigurationException {
		
		String request = MessageProcessor.process((is.readLine().replaceAll("\r", "\6")).replaceAll("\n", "\7"));
		String contentType = request.split(",")[1];
		String nickname    = request.split(",")[2];
		String value 	   = request.split(",")[3];

		if (contentType.equals("picture")) {
			Profile.uploadProfilePicture(nickname, value);
			String result = "Foto de perfil atualizada com sucesso.";
			os.println(MessageCreator.messageUpload(contentType, nickname, value, result));
		}	
	}
	
	private synchronized boolean login() throws IOException, ParserConfigurationException {

		String message = MessageProcessor.process(is.readLine());
		String option  = message.split(",")[0];
		
		// Registar
		if (option.equals("true")) {

			String nick = message.split(",")[1];
			String pass = message.split(",")[2];
			String pic  = message.split(",")[3];
			
			if (Session.availableNickname(nick)) {
				
				Session.register(nick, pass, pic);
				os.println(MessageCreator.messageSession(nick, pass, pic, true, "Sucesso!"));
				System.out.println("O utilizador " + nick + " entrou no jogo.");
				nickname = nick;
				return true;
			} 
			else {
				os.println(MessageCreator.messageSession(nick, pass, pic, true, "Erro: Nickname em uso."));
				return false;
			}
		}
		// Iniciar sessão
		else if (option.equals("false")) {

			String nick = message.split(",")[1];
			String pass = message.split(",")[2];
			String pic  = message.split(",")[3];
			
			if (!Session.availableNickname(nick)) {
				
				if (Session.login(nick, pass)) {
					
					Session.login(nick, pass);
					os.println(MessageCreator.messageSession(nick, pass, pic, false, "Sucesso!"));
					System.out.println("O utilizador " + nick + " entrou no jogo.");
					nickname = nick;
					return true;
					
				} else {
					os.println(MessageCreator.messageSession(nick, pass, pic, false, "Erro: Palavra-passe incorreta!"));
					return false;
				}
			}
			else {
				os.println(MessageCreator.messageSession(nick, pass, pic, false, "Erro: O utilizador nao esta registado."));
				return false;
			}
		}
		return false;
	}

	public synchronized void startGame() {

		if (activeUsers.keySet().size() >= 2) {
					
			String nickname1 = (String) activeUsers.keySet().toArray()[0]; // Nickname do jogador 1
			String nickname2 = (String) activeUsers.keySet().toArray()[1]; // Nickname do jogador 2
			Socket player1 = (Socket) activeUsers.values().toArray()[0];   // Socket do jogador 1
			Socket player2 = (Socket) activeUsers.values().toArray()[1];   // Socket do jogador 2
			
			activeUsers.remove(nickname1);
			activeUsers.remove(nickname2);
			
			System.out.println("A iniciar jogo: " + nickname1 + " vs " + nickname2 + ".");
			
			new GameThread(player1, player2).start(); // Iniciar o jogo
			
			startGame();
		}
	}
}
