package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Daniela Gon�alves A48579 42D
 * 
 */

public class Client {

	private final static String HOST = "localhost"; // Endere�o do Servidor
    private final static int PORT    = 1000;        // Porto onde o Servidor aceita conex�es
    
    public static void main(String[] args) {
        
        Socket     socket = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        Scanner      scan = null;
        
        try {
            socket = new Socket(HOST, PORT);  // Cria��o do Socket

            System.out.println("Liga��o: " + socket + "\n");              			   // Mostrar os parametros da liga��o
            os   = new PrintWriter(socket.getOutputStream(), true); 				   // Stream para escrita no socket
            is   = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            scan = new Scanner(System.in);

            // Receber o Menu do Servidor
            String inputLine;
            while(!((inputLine = is.readLine()).isEmpty())) System.out.println(inputLine);
            
            System.out.print("\nEscolha: ");
            String option = scan.nextLine();
            os.write(option);
            
        } 
        catch (IOException e) {
            System.err.println("Erro na liga��o -> " + e.getMessage());   
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
