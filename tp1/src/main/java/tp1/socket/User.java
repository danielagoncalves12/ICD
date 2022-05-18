package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;

/**
 * @author Daniela Gonçalves A48579 42D
 */

public class User {

	private final static String HOST = "localhost"; // Endereço do Servidor
    private final static int    PORT = 1001;        // Porto onde o Servidor aceita conexões
    private static String nickname = "";
    private static String picture = "";
    
    public static void main(String[] args) throws ParserConfigurationException {
        
        Socket     socket = null; 
        BufferedReader is = null;
        PrintWriter    os = null;
        Scanner      scan = null;        
        
        try {
            socket = new Socket(HOST, PORT); 										     // Ligação ao Socket servidor
            is     = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para a leitura do socket
            os     = new PrintWriter(socket.getOutputStream(), true); 					 // Stream para a escrita no socket 
            scan   = new Scanner(System.in);											 // Scanner para a introdução de dados
            
            // Sessão do utilizador 
            if (!sessionState(os, is, scan)) System.exit(0);
            System.out.println("Bem-vindo " + nickname + "!!");
            
            // Menu principal
            mainMenu(os, is, scan);
        } 
        catch (IOException e) {
           System.err.println(e.getMessage());   
        }
        finally {
            try {
            	scan.close();
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null) socket.close();
                
                System.exit(0);
            }
            catch (IOException e) { 
            	System.err.println("Erro I/O -> " + e.getMessage());   
            }
        }
    }	
 
    private static void playGame(PrintWriter os, BufferedReader is, Scanner scan) throws ParserConfigurationException, IOException {
    	
		System.out.println("A espera da conexao de outro jogador...");
		String playerNum = "0";
		
        // Início do jogo    
		playerNum = User.sendRequestInfo(os, is, playerNum);				 // Pede o número do jogador
		System.out.println("Es o jogador numero " + playerNum + "!!");  	 // Demonstra ao jogador
        System.out.print(User.sendRequestBoard(os, is, playerNum, "true"));  // Receber o próprio tabuleiro
        System.out.print(User.sendRequestBoard(os, is, playerNum, "false")); // Receber tabuleiro do adversário
           
        while (true) {        	
        	
        	// Enviar Request a pedir por informação
        	String info = User.sendRequestInfo(os, is, playerNum);
        	System.out.println(info);
        	if (info.substring(0, 7).equals("Vitoria")) break;
        	
        	// Enviar Request com a jogada escolhida
        	System.out.println(User.sendRequestPlay(os, is, playerNum, scan.nextLine()));
			
			// Enviar Request a pedir o tabuleiro do adversário atualizado
			System.out.println(User.sendRequestBoard(os, is, playerNum, "false"));
		}
    }
    
    private static void mainMenu(PrintWriter os, BufferedReader is, Scanner scan) throws ParserConfigurationException, IOException {
		
    	System.out.println("\nMenu principal:");
    	System.out.println("1 - Novo jogo");
    	System.out.println("2 - Editar o perfil");
    	
    	System.out.print("Escolha: ");
    	String option = scan.nextLine();  
    	os.println(option);
    	
        // Menu principal          
        switch(option) {
        
        case "1":
        	playGame(os, is, scan);
        	break;
        	
        case "2":
        	editProfilePicture(os, is, scan);
        	break;        
        }
	}

    public static void editProfilePicture(PrintWriter os, BufferedReader is, Scanner scan) throws ParserConfigurationException, IOException {
    	
    	System.out.println("\nAtualize a sua foto de perfil: ");
    	System.out.print("Imagem: ");
    	System.out.println(sendRequestUpload(os, is, "picture", nickname, scan.nextLine()));
    	mainMenu(os, is, scan);
    }

    // TODO PROTOCOLO
	public static boolean sessionState(PrintWriter os, BufferedReader is, Scanner scan) throws IOException, ParserConfigurationException {
  
    	System.out.println("Inicio de sessao:");
    	System.out.println("1 - Registar");
    	System.out.println("2 - Entrar");
    	
    	System.out.print("Escolha: ");
    	String option = scan.nextLine();
    	System.out.println();
    	
    	switch (option) {
    	
    	// Registo
    	case "1":
    		
    		// Nickname
    		System.out.print("Escolha o seu nickname: ");
    		String nickname1 = scan.nextLine();				

    		// Password
    		System.out.print("Escolha uma palavra-passe: ");
    		String password1 = scan.nextLine();
    		
    		// Imagem
    		System.out.print("Escolha uma foto de perfil: ");
    		String imagePath1 = scan.nextLine();	

    		nickname = nickname1;
    		picture  = imagePath1;
    		os.println(MessageCreator.messageSession(nickname1, password1, imagePath1, true));	  		
    		break;
    		
    	// Login
    	case "2":  		
    		
    		// Nickname
    		System.out.print("Introduza o seu nickname: ");
    		String nickname2 = scan.nextLine();				

    		// Password
    		System.out.print("Introduza a sua palavra-passe: ");
    		String password2 = scan.nextLine();				
    		
    		nickname = nickname2;
    		picture  = "";
    		os.println(MessageCreator.messageSession(nickname2, password2, "", false));		
    		break;
    	}

		// Estado da sessão
		String state = MessageProcessor.process(is.readLine());
		System.out.println("Sessao -> " + state + "\n");
		if (state.substring(0, 4).equals("Erro")) return false;
		return true;
    }
	
	public static String sendRequestUpload(PrintWriter os, BufferedReader is, String contentType, String nickname, String value) throws ParserConfigurationException, IOException {
		
		os.println(MessageCreator.messageUpload(contentType, nickname, value));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
	}
    
    /**
     * Envia uma Request ao servidor, a pedir o tabuleiro e recebe a resposta do mesmo. O argumento player
     * indica a qual jogador o tabuleiro se destina. Quando o argumento view é igual a true, indica que o
     * utilizador quer receber o próprio tabuleiro, com todos os navios visíveis, caso seja falso, indica
     * que quer receber o tabuleiro do adversário, com os navios que ainda não foram encontrados ocultos.
     */
    public static String sendRequestBoard(PrintWriter os, BufferedReader is, String player, String view) throws ParserConfigurationException, IOException {
		
		os.println(MessageCreator.messageBoard(player, view));
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    /**
     * Envia uma Request ao servidor, a pedir que uma jogada (posição do tiro) seja aplicada no jogo e 
     * recebe uma resposta do mesmo, que contem o resultado da jogada. O argumento player indica qual o 
     * jogador a enviar a jogada. O argumento position contem a posição do tiro escolhida pelo jogador.
     */
    public static String sendRequestPlay(PrintWriter os, BufferedReader is, String player, String position) throws ParserConfigurationException, IOException {	
		
    	os.println(MessageCreator.messagePlay(player, position));	
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
    
    /**
     * Envia uma Request ao servidor, a pedir por informação, que varia de acordo com o estado atual do
     * jogo, por exemplo, uma resposta a este tipo de pedido poderá ser um aviso a um jogador que é a sua 
     * vez de jogar, ou então, avisar os jogadores que o jogo terminou porque um dos jogadores ganhou.
     */
    public static String sendRequestInfo(PrintWriter os, BufferedReader is, String player) throws ParserConfigurationException, IOException {		
    	
    	os.println(MessageCreator.messageInfo(player));  	
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		if (reply == null) return null;
		return MessageProcessor.process(reply); 
    }
}