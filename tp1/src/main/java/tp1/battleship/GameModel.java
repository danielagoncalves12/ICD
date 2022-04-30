package tp1.battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameModel {

	// Constantes
	private int MAXPOINTS = 30;
	
	// Variáveis
	private int[][] boardPlayer1 = new int[10][10]; // Tabuleiro do jogador 1
	private int[][] boardPlayer2 = new int[10][10]; // Tabuleiro do jogador 2
	private int pointsPlayer1, pointsPlayer2; 		// Pontuação dos jogadores
	
	private GameView view = new GameView();			// Visualização na consola
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
		
		pointsPlayer1 = 0;
		pointsPlayer2 = 0; 
		
		// Pergunta ao jogador, pelas posições dos navios
		randomShipPosition(1);
		randomShipPosition(2);
	}

	/**
	 * Receber o tabuleiro, pela vista do adversário, as posições dos navios ainda
	 * não descobertos não são relevadas. Esta função é usada unicamente para apresentar
	 * o tabuleiro do adversário ao jogador.
	 * 
	 * @param player - Jogador 1 ou 2
	 * @return String com apresentação do tabuleiro.
	 */
	public String getBoard(int player) {
	
		String board = "Tabuleiro do jogador " + player + ": Tente atirar num navio!\n\n";
		
		if (player == 1) return board + view.printBoard(boardPlayer1);
		else return board + view.printBoard(boardPlayer2);
	}	
	
	/**
	 * Receber o tabuleiro, pela vista do jogador, as posições dos navios são reveladas
	 * visualmente para o jogador. Esta função é usada unicamente para apresentar o 
	 * próprio tabuleiro para o jogador.
	 * 
	 * @param player
	 * @return
	 */
	public String getBoardView(int player) {

		String board = "O seu tabuleiro: Todos os navios foram posicionados aleatoriamente!\n\n";
		
		int[][] originalBoard = (player == 1) ? boardPlayer1 : boardPlayer2; 
		int[][] copiedBoard   = new int[10][10];
		int[] newLine;
		
		for(int i = 0; i < originalBoard.length; i++) {
			newLine = Arrays.stream(originalBoard[i].clone()).map(j -> j == 1 ? 2 : 0).toArray();
		    copiedBoard[i] = newLine;
		}
		return board + view.printBoard(copiedBoard);
	}

	/**
	 * @param player - Jogador 1 ou 2
	 * @return
	 */
	public boolean checkWin(int player) {
		
		if (player == 1) return pointsPlayer1 == MAXPOINTS;
		else return pointsPlayer2 == MAXPOINTS;
	}
	
	/**
	 * @param player - Jogador 1 ou 2
	 * @param choice - Jogada, linha e coluna (Exemplo: 1A, 5F, etc)
	 * @return String que contém o estado do jogo.
	 */
	public String play(int player, String choice) {
		
		int line   = Character.getNumericValue(choice.charAt(0)) - 1;
		int column = columnValue.get(Character.toUpperCase(choice.charAt(1)) + "");		
		int state;

		if (player == 1) {
			state = boardPlayer2[line][column];
			if (state == 1) pointsPlayer1++;
			boardPlayer2[line][column] = (state == 0) ? 3 : (state == 1) ? 2 : (state == 2) ? 2 : 3;
		}
		else {
			state = boardPlayer1[line][column];
			if (state == 1) pointsPlayer2++;
			boardPlayer1[line][column] = (state == 0) ? 3 : (state == 1) ? 2 : (state == 2) ? 2 : 3;
		}
		return (state == 0) ? "Tiro no mar!" 
			 : (state == 1) ? "Navio atingido!"
			 : (state == 2) ? "Navio já descoberto..."
			 : "Espaço já explorado.";		 
	}

	public void randomShipPosition(int player) {
		
		String letters = "ABCDEFGHIJ";
		int[] shipSizes  = {5, 4, 3, 2};
		int[] shipNumber = {1, 2, 3, 4};

		for (int i = 0; i < 4; i++) {

			String position = "";  // Posição
			boolean vertical = false, check = false; // Sentido e verificação da posição
			int number = 0;		  					 // Número de navios por posicionar
			
			while(number != shipNumber[i]) {
			
				do {			
					String randomLine = String.valueOf(new Random().nextInt(10) + 1);
					char randomColumn = letters.charAt(new Random().nextInt(letters.length()));					
					if (randomLine.length() == 1) randomLine = "0" + randomLine;
					position = randomLine + randomColumn;		
	
					vertical = Math.random() < 0.5; 						      // Escolhe se o navio será posicionado verticalmente ou horizontalmente		
					check = checkValidPosition(position, shipSizes[i], vertical); // Verifica se a posição é válida
					
					// Caso a posição seja válida, o navio é introduzido
					if (check) {
						int steps  = 0;
						String strLine = "" + position.charAt(0) + position.charAt(1);
						
						int line     = Integer.parseInt(strLine) - 1;
						int column   = columnValue.get(position.charAt(2) + "");
						
						while(steps != shipSizes[i]) {		
							
							if (vertical) {
								if (player == 1) boardPlayer1[line++][column] = 1;
								if (player == 2) boardPlayer2[line++][column] = 1;
							}
							else {
								if (player == 1) boardPlayer1[line][column++] = 1;
								if (player == 2) boardPlayer2[line][column++] = 1;
							}
							steps++;
						}
						number++;
					}
				} while(!check);
			}		
		}
	}	

	public boolean checkValidPosition(String position, int shipSize, boolean vertical) {

		char line1  = position.charAt(0);
		char line2  = position.charAt(1);
		char column = position.charAt(2);
		
		// Verificação do sintaxe da posição
		if (Character.isDigit(line1) && Character.isDigit(line2)) {
			
			String line = "" + Character.getNumericValue(line1) + Character.getNumericValue(line2);
			int line_number = Integer.parseInt(line) - 1;
					
			// Verificação do sintaxe da posição
			if (Character.isLetter(column)) {

				int column_number = columnValue.get(column + "");
				int steps = 0;						
				while(steps != shipSize) {
					
					// Verificação - Dentro do tabuleiro
					if (line_number < 0 || line_number >= 10)     return false; // Erro: Navio fora do tabuleiro
					if (column_number < 0 || column_number >= 10) return false; // Erro: Navio fora do tabuleiro

					// Verificação - Colisão com outro navio
					if (boardPlayer1[line_number][column_number] != 0) return false; // Erro: Colisão com outro navio.
	
					if (vertical)  line_number++;
					if (!vertical) column_number++;			
					steps++;
				}			
				return true;
			}
		}	
		System.out.println("Erro: Sintaxe inválida");		
		return false;
	}

	/*
	public void chooseShipPosition() {
		
		String[] positionRequests = {
				"Posicione um porta-aviões (5 quadrados)",
				"Posicione dois navios-tanque (4 quadrados)",
				"Posicione três contratorpedeiros (3 quadrados)",
				"Posicione quatro submarinos (2 quadrados)"
		};
		
		int[] shipSizes  = {5, 4, 3, 2};
		int[] shipNumber = {1, 2, 3, 4};
		
		Scanner scan = new Scanner(System.in);

		for (int i = 0; i < positionRequests.length; i++) {
		
			System.out.println(positionRequests[i]);
			
			String position = "";  // Posição introduzida pelo utilizador
			boolean check = false; // Verificação da posição
			int number = 0;		   // Número de navios por posicionar
			
			while(number != shipNumber[i]) {
			
				do {
					System.out.println("Insira o número e letra (Ex. 1A): ");
					position = scan.nextLine();
					check = checkValidPosition(position, shipSizes[i]);
	
					// Caso a posição seja válida, é introduzido o navio
					if (check) {
						int steps  = 0;
						String strLine = "" + position.charAt(0) + position.charAt(1);
						int line   = Integer.parseInt(strLine) - 1;
						int column = columnValue.get(position.charAt(2) + "");
						
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
	}*/

	
}
