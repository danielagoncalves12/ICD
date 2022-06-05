import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Porfírio Filipe
 *
 */
public class User {

    private final static String DEFAULT_HOST = "localhost"; // "pfenvy"; //"194.210.190.133"; // Máquina onde reside o servidor
    private final static int DEFAULT_PORT = 5025; // porto onde o servidor está á espera
    
    public static void main(String[] args) {
        
        Socket socket     = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        
        try {
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            // Mostrar os parametros da ligação
            System.out.println("Ligação: " + socket);
            
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
            System.err.println("Erro na ligação " + e.getMessage());   
        }
        finally {
        	
            // No fim de tudo, fechar o socket
            try {
            	if (os != null) os.close();
            	if (is != null) is.close();
                if (socket != null ) socket.close();
                System.out.println("Fechou a ligação!");
            }
            catch (IOException e) { 
                // if an I/O error occurs when closing this socket
            }
        } // end finally
    } // end main
} // end ClienteTCP

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

} // end ThreadReader


