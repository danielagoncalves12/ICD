package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 */

public class User {

	private final static String HOST = "localhost"; // Endereço do Servidor
    private final static int    PORT = 1000;        // Porto onde o Servidor aceita conexões
    
    public static void main(String[] args) {
        
        Socket     socket = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        Scanner      scan = null;
        
        try {
            socket = new Socket(HOST, PORT);  // Criação do Socket

            // System.out.println("Ligação: " + socket + "\n");              		   // Mostrar os parametros da ligação
            os   = new PrintWriter(socket.getOutputStream(), true); 				   // Stream para escrita no socket
            is   = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            scan = new Scanner(System.in);

            System.out.println("Nickname: ");
            
            try (Scanner scanner = new Scanner(System.in)) {
				String inputString = scanner.nextLine();
				
				// Stream para escrita no socket
				os = new PrintWriter(socket.getOutputStream(), true); 
				  // Stream para leitura do socket
	            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	            // lança a tarefa que vai ler a lista
	            Thread th = new ThreadReader(is);
				th.start();
				// Escreve no socket, regista utilizador
				os.println(inputString);
				
				System.out.println("Press Enter key to finish...");
				scanner.nextLine();
			}
        } 
        catch (IOException e) {
            System.err.println("Erro na ligação -> " + e.getMessage());   
        }
        finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null ) socket.close();
            }
            catch (IOException e) { 
            	System.err.println("Erro I/O -> " + e.getMessage());   
            }
        }

    }	
}

class ThreadReader extends Thread {

	private BufferedReader is;

	public ThreadReader(BufferedReader is) {
		this.is = is;
	}

	public void run() {

		try {
			for(;;) {
				
				String nickNames = (String) is.readLine();
				if(nickNames==null)
					break;
				System.out.println(nickNames.replaceAll("\7", "\n"));
			}

		} catch (IOException e) {
			
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId());
	} // end run

} 