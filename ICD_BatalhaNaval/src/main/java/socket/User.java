package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private String nickname, name, picture;  		// Dados da conta do utilizador
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
 
    public String enterQueue(String username) {
    	try {
			return sendRequestGame(username);
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    private void playGame() throws ParserConfigurationException, IOException {
    	System.out.println(enterQueue(nickname));
    	
		String playerNum = "0";
		
        // In�cio do jogo    
		playerNum = sendRequestInfo(playerNum);							// Pede o n�mero do jogador
		System.out.println("Es o jogador numero " + playerNum + "!!");  // Demonstra ao jogador
		sendRequestInfo(playerNum);  	
        System.out.print(sendRequestBoard(playerNum, "true"));  		// Receber o pr�prio tabuleiro
        System.out.print(sendRequestBoard(playerNum, "false"));			// Receber tabuleiro do advers�rio
           
        while (true) {        	
        	
        	// Enviar Request a pedir por informa��o
        	String info = sendRequestInfo(playerNum);
        	System.out.println(info);
        	if (win(info)) break;
        	
        	// Enviar Request com a jogada escolhida
        	String position;
        	do {
        		position = scan.nextLine();
        		if (!checkValid(position)) System.out.println("Posi��o inv�lida! Insira uma posi��o novamente:");
        	}
        	while(!checkValid(position));	
        	System.out.println(sendRequestPlay(playerNum, position));
			
			// Enviar Request a pedir o tabuleiro do advers�rio atualizado
        	System.out.println(sendRequestBoard(playerNum, "true"));
			System.out.println(sendRequestBoard(playerNum, "false"));
		}
    }

    /**
     * Verificar se uma posi��o est� no formato correto.
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
     * Verifica se o jogador ganhou o jogo.
     * @param info
     * @return boolean
     */
    private boolean win(String info) {
    	return (info.substring(0, 7).equals("Vitoria"));
    }
    
    /**
     * Menu inicial que apresenta o perfil do utilizador e 
     * permite o jogador iniciar um novo jogo ou editar o perfil.
     * @param os, is, scan
     * @throws ParserConfigurationException, IOException
     */
    private void mainMenu() throws ParserConfigurationException, IOException {
    	
    	// Apresenta��o do perfil
    	System.out.println("--------------------------------");
    	System.out.println("Bem-vindo " + nickname + "!!");
    	System.out.println("--------------------------------");
    	System.out.println("Seu Perfil: ");
    	System.out.println("Nickname:     " + nickname);
    	System.out.println("Nome publico: " + name);
    	System.out.println("Fotografia:   " + picture);
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
    	System.out.println(sendRequestUpload("picture", nickname, scan.nextLine()));
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
    	
    	String nickname = "", name = "", password = "", picture = "", reply = "";
    	
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
        	reply = sendRequestLogin(nickname, name, password, picture, true);  	
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
        	reply = sendRequestLogin(nickname, name, password, picture, false);  		
    	}
    	
    	// Atualiza��o dos dados do utilizador, dados vindo do servidor
    	String state  = reply.split(",")[0];
		this.nickname = reply.split(",")[1];
		this.name     = reply.split(",")[2];
		this.picture  = reply.split(",")[3];
    	
		System.out.println("Sessao -> " + state + "\n");
		if (state.substring(0, 4).equals("Erro")) return false;
		return true;
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
	public String sendRequestLogin(String nickname, String name, String password, String picture, boolean register) throws ParserConfigurationException, IOException {
	
		os.println(MessageCreator.messageSession(nickname, name, password, picture, register));
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
    public String sendRequestBoard(String player, String view) throws ParserConfigurationException, IOException {
		
		os.println(MessageCreator.messageBoard(player, view));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    /**
     * Envia uma Request ao servidor, a pedir que uma jogada (posi��o do tiro) seja aplicada no jogo e 
     * recebe uma resposta do mesmo, que contem o resultado da jogada. O argumento player indica qual o 
     * jogador a enviar a jogada. O argumento position contem a posi��o do tiro escolhida pelo jogador.
     */
    public String sendRequestPlay(String player, String position) throws ParserConfigurationException, IOException {	
		
    	os.println(MessageCreator.messagePlay(player, position));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    /**
     * Envia uma Request ao servidor, a pedir por informa��o, que varia de acordo com o estado atual do
     * jogo, por exemplo, uma resposta a este tipo de pedido poder� ser um aviso a um jogador que � a sua 
     * vez de jogar, ou ent�o, avisar os jogadores que o jogo terminou porque um dos jogadores ganhou.
     */
    public String sendRequestInfo(String player) throws ParserConfigurationException, IOException {		

    	os.println(MessageCreator.messageInfo(player));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
}