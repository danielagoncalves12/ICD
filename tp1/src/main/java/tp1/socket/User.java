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
            socket = new Socket(HOST, PORT);  // Ligação ao Socket servidor

            is = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            os = new PrintWriter(socket.getOutputStream(), true); 

            try (Scanner in = new Scanner(System.in)) {
		        for(;;) {
		        	
					// Mostrar o que se recebe do socket
					System.out.println(is.readLine().replaceAll("\7", "\n")); 
					String jogada = in.nextLine();
					os.println(jogada);
					System.out.println(is.readLine().replaceAll("\7", "\n")); 
					System.out.println(is.readLine().replaceAll("\7", "\n")); 
				}
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