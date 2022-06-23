package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;

import protocol.MessageCreator;
import protocol.MessageProcessor;

/**
 * @author Daniela Gon�alves A48579 42D
 * Classe User
 */

public class User {

	private final static String HOST = "localhost"; // Endere�o do Servidor
    private final static int    PORT = 49152;       // Porto onde o Servidor aceita conex�es
    private String username, name, color, date, winNum, picture;   // Dados da conta do utilizador
    private Socket socket;
    private BufferedReader is;
    private PrintWriter os;
    private Scanner scan;
    
    public User() {
    	
        try {
            socket = new Socket(HOST, PORT); 										     // Liga��o ao Socket servidor
            is     = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para a leitura do socket
			os     = new PrintWriter(socket.getOutputStream(), true);					 // Stream para a escrita no socket 
			scan   = new Scanner(System.in);											 // Scanner para a introdu��o de dados	
		} 
        catch (IOException e) {
			e.printStackTrace();
		} 					      
    }
    
    public static void main(String[] args) throws ParserConfigurationException {

		User player = new User();
    	
        try {        	
        	boolean sessionState = false;       	
        	do { sessionState = player.sessionState(); } while(!sessionState);    	
            player.mainMenu(); // Menu principal		
        } 
        catch (IOException e) {
           System.err.println(e.getMessage());   
        }
    }	

    private void playGame() throws ParserConfigurationException, IOException {
   
    	String gameID = sendRequestGame(username); // Pedido para procurar e iniciar uma partida 
        String result = null;   
    	
        while (true) {

			// Enviar Request a pedir o tabuleiro do adversario atualizado
        	System.out.println("Seu tabuleiro: ");
        	System.out.println(sendRequestBoard(gameID, username, "true") + "\n");
        	
        	System.out.println("Tabuleiro do adversario: ");
			System.out.println(sendRequestBoard(gameID, username, "false") + "\n");

			if (result != null) if (result.substring(0, 9).equals("O jogador")) mainMenu();
			
        	// Enviar Request com a jogada escolhida
        	String position;
        	do {
        		System.out.print("Opcao: ");
        		position = scan.nextLine();
        		if (!checkValid(position)) System.out.println("Posicao invalida! Insira uma posico novamente:\n");
        	}
        	while(!checkValid(position));
			result = sendRequestPlay(gameID, username, position);
        	System.out.println(result + "\n");
			
		}
    }

    /**
     * Verificar se uma posicao esta no formato correto.
     * @param position
     * @return validade (boolean)
     */
    private boolean checkValid(String position) {

    	if (position.length() == 2) {
    		
    		char number = position.charAt(0);
    		char letter = position.charAt(1);
    		
    		if ((letter >= 'a' && letter <= 'j') || (letter >= 'A' && letter <= 'J'))
    			if (number >= '0' && number <= '9')
    				return true;
    	}
    		
    	if (position.length() == 3) {
    		
    		char firstNumber  = position.charAt(0);
    		char secondNumber = position.charAt(1);
    		char firstLetter  = position.charAt(2);
    		
			if ((firstLetter >= 'a' && firstLetter <= 'j') || (firstLetter >= 'A' && firstLetter <= 'J'))
				if (firstNumber == '1' && secondNumber == '0')
					return true;
    	}
    	return false;
    }
    
    public void close() {

		try {
			scan.close();
			if (os != null) os.close();
			if (is != null) is.close();
			if (socket != null) socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
        System.exit(0);
    }
    
    /**
     * Menu inicial que apresenta o perfil do utilizador e 
     * permite o jogador iniciar um novo jogo ou editar o perfil.
     * @param os, is, scan
     * @throws ParserConfigurationException, IOException
     */
    private void mainMenu() throws ParserConfigurationException, IOException {
    	
    	System.out.println("A enviar este " + username);
    	
    	String profile = sendRequestProfileInfo(username);
    	this.name    = profile.split(",")[0];
    	this.color   = profile.split(",")[1];
    	this.picture = profile.split(",")[2];
    	this.winNum  = profile.split(",")[3];
    	this.date    = profile.split(",")[4];
    	
    	// Apresenta��o do perfil
    	System.out.println("--------------------------------");
    	System.out.println("Bem-vindo " + username + "!!");
    	System.out.println("--------------------------------");
    	System.out.println("Seu Perfil: ");
    	System.out.println("Nickname:     " + username);
    	System.out.println("Nome publico: " + name);
    	System.out.println("Fotografia:   " + picture);
    	System.out.println("Numero de vitorias: " + winNum);
    	System.out.println("Idade: " + getAge(date) + " anos");
    	System.out.println("Cor: " + color);
    	System.out.println("--------------------------------");
    	
    	// Menu principal   
    	System.out.println("\nMenu principal:");
    	System.out.println("1 - Novo jogo");
    	System.out.println("2 - Editar o perfil");
    	
    	System.out.print("Escolha: ");
    	String option = scan.nextLine();    		
               
        switch(option) {
        
        case "1":  
        	playGame();
        	break;
        	
        case "2":
        	editProfilePicture();
        	break;        
        }
	}

    /**
     * Edi��o da foto de perfil.
     * @param os, is, scan
     * @throws ParserConfigurationException, IOException
     */
    public void editProfilePicture() throws ParserConfigurationException, IOException {
    	
    	System.out.println("\nAtualize a sua foto de perfil: ");
    	System.out.print("Imagem: ");
    	System.out.println(sendRequestUpload("picture", username, scan.nextLine()));
    	mainMenu();
    }

    /**
     * Inicia sess�o, a partir da inscri��o no servidor
     * ou a partir de uma conta existente.
     */
	public boolean sessionState() throws IOException, ParserConfigurationException {
  
    	System.out.println("Inicio de sessao:");
    	System.out.println("1 - Registar");
    	System.out.println("2 - Entrar");
    	
    	System.out.print("Escolha: ");
    	String option = scan.nextLine();
    	System.out.println();
    	
    	// TODO
    	String nickname = "", name = "", password = "", picture = "", color = "#FFFFFF", date = "01/01/1900";
    	String reply = "";
    	
    	switch (option) {
    	
    	// Registo
    	case "1":
    			
    		// Nickname
    		do {
	    		System.out.print("Escolha o seu nickname: ");
	    		nickname = scan.nextLine();	
	    		if (nickname.isEmpty()) System.out.println("Erro: Campo do nickname nao pode estar vazio.");
    		} while(nickname.isEmpty());
    		
    		// Nome
    		do {
	    		System.out.print("Escolha o seu nome publico: ");
	    		name = scan.nextLine();		
	    		if (name.isEmpty()) System.out.println("Erro: Campo do nome publico nao pode estar vazio.");
    		} while(name.isEmpty());
    		
    		// Palavra-passe
    		do {
	    		System.out.print("Escolha a sua palavra-passe: ");
	    		password = scan.nextLine();
	    		if (password.isEmpty()) System.out.println("Erro: Campo da palavra-passe nao pode estar vazia.");
    		} while(name.isEmpty());
    		
    		// Imagem
    		System.out.print("Escolha a sua foto de perfil: ");
    		picture = scan.nextLine();	
    		if (picture.isEmpty()) System.out.println("Aviso: Foto de perfil pre-predefinida atribuida.");
    		
    		// Envio do pedido e recep��o da resposta
        	reply = sendRequestLogin(nickname, name, password, color, date, picture, true);  	
    		break;
    		
    	// Login
    	case "2":  		

    		// Nickname
    		do {
	    		System.out.print("Introduza o seu nickname: ");
	    		nickname = scan.nextLine();	
	    		if (nickname.isEmpty()) System.out.println("Erro: Campo do nickname nao pode estar vazio.");
    		} while(nickname.isEmpty());
    		
    		// Palavra-passe
    		do {
	    		System.out.print("Introduza a sua palavra-passe: ");
	    		password = scan.nextLine();
	    		if (password.isEmpty()) System.out.println("Erro: Campo da palavra-passe nao pode estar vazio.");
    		} while(password.isEmpty());

    		// Envio do pedido e recep��o da resposta
        	reply = sendRequestLogin(nickname, name, password, color, date, picture, false);  		
    	}
    	
    	// Atualiza��o dos dados do utilizador, dados vindo do servidor

		System.out.println("Sessao -> " + reply + "\n");
		if (reply.substring(0, 4).equals("Erro")) return false;
		this.username = nickname;
		return true;
    }
	
	private String getAge(String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate birth = LocalDate.parse(date, formatter);
		return String.valueOf(ChronoUnit.YEARS.between(birth, LocalDate.now()));
	}
	
	public String sendRequestGame(String nickname) throws ParserConfigurationException, IOException {
		
		os.println(MessageCreator.messageFind(nickname));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply);
	}
	
	/**
     * Envia uma Request ao servidor, a pedir para iniciar sess�o, a partir de login ou inscrevendo-se.
     * Para iniciar sess�o o utilizador deve inserir o seu nickname (chave prim�ria) e palavra-passe,
     * para inscrever-se o utilizador deve introduzir todos os dados, nomeadamente, nickname, nome publico,
     * palavra-passe e fotografia (caso queira). � retornado o resultado da sess�o e os dados atualizados
     * de acordo com a base de dados do servidor.
     */
	public String sendRequestLogin(String nickname, String name, String password, String color, String date, String picture, boolean register) throws ParserConfigurationException, IOException {
	
		os.println(MessageCreator.messageSession(nickname, name, password, color, date, picture, register));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply);
	}
	
	/**
     * Envia uma Request ao servidor, a pedir para atualizar um certo dado e recebe a resposta do mesmo. 
     * Os dados a ser atualizados, podem ser a palavra-passe, fotografia, nome publico do jogador, etc...
     * O argumento contenttype destina-se a identificar o tipo de dado a ser atualizado. O argumento
     * nickname � utilizado como chave prim�ria e o argumento value transforma os dados atualizados,
     * que v�o substituir os antigos.
     */
	public String sendRequestUpload(String contentType, String nickname, String value) throws ParserConfigurationException, IOException {

		os.println(MessageCreator.messageUpload(contentType, nickname, value));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
	}
    
    /**
     * Envia uma Request ao servidor, a pedir o tabuleiro e recebe a resposta do mesmo. O argumento player
     * indica a qual jogador o tabuleiro se destina. Quando o argumento view � igual a true, indica que o
     * utilizador quer receber o pr�prio tabuleiro, com todos os navios vis�veis, caso seja falso, indica
     * que quer receber o tabuleiro do advers�rio, com os navios que ainda n�o foram encontrados ocultos.
     */
    public String sendRequestBoard(String gameID, String player, String view) throws ParserConfigurationException, IOException {
		
		os.println(MessageCreator.messageBoard(gameID, player, view));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    /**
     * Envia uma Request ao servidor, a pedir que uma jogada (posi��o do tiro) seja aplicada no jogo e 
     * recebe uma resposta do mesmo, que contem o resultado da jogada. O argumento player indica qual o 
     * jogador a enviar a jogada. O argumento position contem a posi��o do tiro escolhida pelo jogador.
     */
    public String sendRequestPlay(String gameID, String username, String position) throws ParserConfigurationException, IOException {	
		
    	os.println(MessageCreator.messagePlay(gameID, username, position));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    public String sendRequestHonorBoard() throws ParserConfigurationException, IOException {
    	
    	os.println(MessageCreator.messageHonorBoard());
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    public String sendRequestProfileInfo(String username) throws ParserConfigurationException, IOException {
    	
    	os.println(MessageCreator.messageGetProfileInfo(username));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
public String sendRequestPlayers() throws ParserConfigurationException, IOException {
    	
    	os.println(MessageCreator.messageGetPlayers());
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
}