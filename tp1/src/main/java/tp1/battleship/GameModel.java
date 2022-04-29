package tp1.battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GameModel {

	private int[][] boardPlayer1 = new int[10][10]; // Tabuleiro do jogador 1
	private int[][] boardPlayer2 = new int[10][10]; // Tabuleiro do jogador 2
	
	private GameView view = new GameView();			// Visualiza��o na consola
	private HashMap<String, Integer> columnValue = new HashMap<>();
	
	public GameModel() {
		
		// Preencher o tabuleiro inicial com zeros [Por explorar (Sem navio)]
		Arrays.stream(boardPlayer1).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(boardPlayer2).forEach(a -> Arrays.fill(a, 0));
		
		columnValue.put("A", 0);
		columnValue.put("B", 1);
		columnValue.put("C", 2);
		columnValue.put("D", 3);
		columnValue.put("E", 4);
		columnValue.put("F", 5);
		columnValue.put("G", 6);
		columnValue.put("H", 7);
		columnValue.put("I", 8);
		columnValue.put("J", 9);
		
		// Pergunta ao jogador, pelas posi��es dos navios
		//chooseShipPosition();
		randomShipPosition(boardPlayer1);
		randomShipPosition(boardPlayer2);
	}
	
	public void randomShipPosition(int[][] boardPlayer) {
		
		for (int i = 0; i < boardPlayer.length; i++) {
			for (int j = 0; j < boardPlayer[0].length; j++) {
				boardPlayer[i][j] = (Math.random() > 0.85 ? 1 : 0);
			}	
		}
	}
	
	public String play(int player, String choice) {
		
		int line   = Character.getNumericValue(choice.charAt(0)) - 1;
		int column = columnValue.get(choice.charAt(1) + "");
		
		int state;
		String result;
		
		if (player == 1) {
			state = boardPlayer2[line][column];
			boardPlayer2[line][column] = (state == 0) ? 3 : (state == 1) ? 2 : (state == 2) ? 2 : 3;
		}
		else {
			state = boardPlayer1[line][column];
			boardPlayer1[line][column] = (state == 0) ? 3 : (state == 1) ? 2 : (state == 2) ? 2 : 3;
		}
		return (state == 0) ? "Tiro no mar!" 
			 : (state == 1) ? "Navio atingido!"
			 : (state == 2) ? "Navio j� descoberto..."
			 : "Espa�o j� explorado.";		 
	}
	
	public String getBoard(int player) {
	
		if (player == 1) return view.printBoard(boardPlayer1);
		else return view.printBoard(boardPlayer2);
	}
	
	public void chooseShipPosition() {
		
		String[] positionRequests = {
				"Posicione um porta-avi�es (5 quadrados)",
				"Posicione dois navios-tanque (4 quadrados)",
				"Posicione tr�s contratorpedeiros (3 quadrados)",
				"Posicione quatro submarinos (2 quadrados)"
		};
		
		int[] shipSizes  = {5, 4, 3, 2};
		int[] shipNumber = {1, 2, 3, 4};
		
		Scanner scan = new Scanner(System.in);

		for (int i = 0; i < positionRequests.length; i++) {
		
			System.out.println(positionRequests[i]);
			
			String position = "";  // Posi��o introduzida pelo utilizador
			boolean check = false; // Verifica��o da posi��o
			int number = 0;		   // N�mero de navios por posicionar
			
			while(number != shipNumber[i]) {
			
				do {
					System.out.println("Insira o n�mero e letra (Ex. 1A): ");
					position = scan.nextLine();
					check = checkValidPosition(position, shipSizes[i]);
	
					// Caso a posi��o seja v�lida, � introduzido o navio
					if (check) {
						int steps  = 0;
						int line   = Character.getNumericValue(position.charAt(0)) - 1;
						int column = columnValue.get(position.charAt(1) + "");
						
						while(steps != shipSizes[i]) {		
							boardPlayer1[line++][column] = 2;
							steps++;
						}
						number++;
					}
				} while(!check);
				this.view.printBoard(boardPlayer1);
			}		
		}
		System.out.println("Todos os navios foram posicionados!");
	}
	
	public boolean checkValidPosition(String position, int shipSize) {
		
		char line   = position.charAt(0);
		char column = position.charAt(1);
		
		// Verifica��o do sintaxe da posi��o
		if (Character.isDigit(line)) {
			
			int line_number = Character.getNumericValue(line) - 1;
			if (Character.isLetter(column)) {

				int column_number = columnValue.get(column + "");
				int steps = 0;						
				while(steps != shipSize) {
					
					// Verifica��o - Dentro do tabuleiro
					if (line_number < 0 || line_number >= 10) {
						System.out.println("Erro: Navio fora do tabuleiro.");		
						return false;
					}

					// Verifica��o - Colis�o com outro navio
					if (boardPlayer1[line_number][column_number] != 0) {
						System.out.println("Erro: Colis�o com outro navio.");		
						return false;
					}
					line_number++;
					steps++;
				}			
				return true;
			}
		}
		
		System.out.println("Erro: Sintaxe inv�lida");		
		return false;
	}
	
	
	public static void main(String[] args) {
		
		new GameModel();
	}
}
