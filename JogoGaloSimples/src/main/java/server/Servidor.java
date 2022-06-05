package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public final static int DEFAULT_PORT = 5025; // porto onde vai ficar á espera

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(DEFAULT_PORT);

			Socket newSock1 = null;  // X
			Socket newSock2 = null;  // O

			for (;;) {
				System.out
						.println("Servidor TCP concorrente aguarda ligacao no porto "
								+ DEFAULT_PORT + "...");

				// Espera ligação do primeiro jogador
				System.out.println("Espero pelo Jogador X");
				newSock1 = serverSocket.accept();
				// Espera ligação do segundo jogador
				System.out.println("Espero pelo Jogador O");
				newSock2 = serverSocket.accept();

				Thread th = new HandleConnectionThread(newSock1,newSock2);
				th.start();
			}
		} catch (IOException e) {
			System.err.println("Excepção no servidor: " + e);
		}
	} // end main

} // end ServidorTCP

class HandleConnectionThread extends Thread {

	private Socket connection1;
	private Socket connection2;

	public HandleConnectionThread(Socket connection1, Socket connection2) {
		this.connection1 = connection1;
		this.connection2 = connection2;
	}

	public void run() {

		BufferedReader is1 = null;
		PrintWriter os1 = null;
		
		BufferedReader is2 = null;
		PrintWriter os2 = null;
		
		try { 
			System.out.println("Thread do Jogo: " + this.getId() + ", "
					+ connection1.getRemoteSocketAddress()+ ", "+ connection2.getRemoteSocketAddress());

			is1 = new BufferedReader(new InputStreamReader(
					connection1.getInputStream()));

			os1 = new PrintWriter(connection1.getOutputStream(), true);
			
			is2 = new BufferedReader(new InputStreamReader(
					connection2.getInputStream()));

			os2 = new PrintWriter(connection2.getOutputStream(), true);
			
			Jogo JG = new Jogo();
			
			for(;;) {
				
				// usar o caracter bell como separador em vez do \n
				// não se deve fazer!!! porque não é genérico.
				
				os1.println((JG.JogoToTXT()+"\nJogador X:").replaceAll("\n", "\7")); 
			
				String inputLine1 = is1.readLine();
				if(inputLine1==null)
					break;

				System.out.println(connection1.getRemoteSocketAddress()+" -> Recebi do X: " + inputLine1);
				 
				JG.jogar(Short.parseShort(inputLine1), 'X');
				
					
				if(JG.vitoria('X')) {
					String resp=(JG.JogoToTXT()+"\nVitória do X!").replaceAll("\n", "\7");
					os1.println(resp); 
					os2.println(resp);
					System.out.println(resp.replaceAll("\7", "\n"));
					break;
				}
				else
					if(JG.empate()) {
						String resp=(JG.JogoToTXT()+"\nEmpate!").replaceAll("\n", "\7");
						os1.println(resp); 
						os2.println(resp);
						System.out.println(resp.replaceAll("\7", "\n"));
						break;
					}
					else
						JG.mostrar();
				
				os2.println((JG.JogoToTXT()+"\nJogador O:").replaceAll("\n", "\7"));
				
				String inputLine2 = is2.readLine();
				if(inputLine2==null)
					break;
				
				System.out.println(connection2.getRemoteSocketAddress()+" ->Recebi do O:" + inputLine2);

				JG.jogar(Short.parseShort(inputLine2), 'O');
				
				if(JG.vitoria('O')) {
					String resp=(JG.JogoToTXT()+"\nVitória do O!").replaceAll("\n", "\7");
					os1.println(resp); 
					os2.println(resp);
					System.out.println(resp.replaceAll("\7", "\n"));
					break;
				}
				else
					if(JG.empate()) {
						String resp=(JG.JogoToTXT()+"\nEmpate!").replaceAll("\n", "\7");
						os1.println(resp); 
						os2.println(resp);
						System.out.println(resp.replaceAll("\7", "\n"));
						break;
					}
					else
						JG.mostrar();
			}
			
			
		} catch (IOException e) {
			System.err.println("Erro na ligaçao : "
					+ e.getMessage());
		} finally {
			// garantir que o socket é fechado
			try {
				if (is1 != null)
					is1.close();
				if (os1 != null)
					os1.close();
				if (connection1 != null)
					connection1.close();
				if (is2 != null)
					is2.close();
				if (os2 != null)
					os2.close();
				if (connection2 != null)
					connection2.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", "
				+ connection1.getRemoteSocketAddress()+ ", "
						+ connection2.getRemoteSocketAddress());
	} // end run

} // end HandleConnectionThread
