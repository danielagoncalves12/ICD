package tp1.battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameModel {

	// Constantes
	private int MAXPOINTS = 30;
	
	// Vari�veis
	private int[][] boardPlayer1 = new int[10][10]; // Tabuleiro do jogador 1
	private int[][] boardPlayer2 = new int[10][10]; // Tabuleiro do jogador 2
	private int pointsPlayer1, pointsPlayer2; 		// Pontua��o dos jogadores
	
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
		
		pointsPlayer1 = 0;
		pointsPlayer2 = 0; 
		
		// Pergunta ao jogador, pelas posi��es dos navios
		randomShipPosition(1);
		randomShipPosition(2);
	}

	/**
	 * Receber o tabuleiro, pela vista do advers�rio, as posi��es dos navios ainda
	 * n�o descobertos n�o s�o relevadas. Esta fun��o � usada unicamente para apresentar
	 * o tabuleiro do advers�rio ao jogador.
	 * 
	 * @param player - Jogador 1 ou 2
	 * @return String com apresenta��o do tabuleiro.
	 */
	public String getBoard(int player) {
	
		String board = "Tabuleiro do jogador " + player + ":\nPontuacao atual -> Jogador 1: " 
					 			  + pointsPlayer1 + " pontos, Jogador 2: " + pointsPlayer2 + " pontos.\n\n";
		
		if (player == 1) return board + view.printBoard(boardPlayer1);
		else return board + view.printBoard(boardPlayer2);
	}	
	
	/**
	 * Receber o tabuleiro, pela vista do jogador, as posi��es dos navios s�o reveladas
	 * visualmente para o jogador. Esta fun��o � usada unicamente para apresentar o 
	 * pr�prio tabuleiro para o jogador.
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
			newLine = Arrays.stream(originalBoard[i].clone()).map(j -> 
			
					(j == ShipType.TYPE1HIDDEN) ? ShipType.TYPE1SHOW : 
					(j == ShipType.TYPE2HIDDEN) ? ShipType.TYPE2SHOW  : 
					(j == ShipType.TYPE3HIDDEN) ? ShipType.TYPE3SHOW  : 
					(j == ShipType.TYPE4HIDDEN) ? ShipType.TYPE4SHOW  : j).toArray();	
			
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
	 * @return String que cont�m o estado do jogo.
	 */
	public String play(int player, String choice) {
		
		int line, column;
		
		if (choice.length() == 2) {
			line   = Character.getNumericValue(choice.charAt(0)) - 1;
			column = columnValue.get(Character.toUpperCase(choice.charAt(1)) + "");
		}
		else {
			int line1  = Character.getNumericValue(choice.charAt(0));
			int line2  = Character.getNumericValue(choice.charAt(1));
			line   = Integer.parseInt("" + line1 + line2) - 1;
			column = columnValue.get(Character.toUpperCase(choice.charAt(2)) + "");	
		}

		int state;
		int[][] board = (player == 1) ? boardPlayer2 : boardPlayer1;

		state = board[line][column];
		if (state >= 6 && state <= 9) if (player == 1) pointsPlayer1++; else pointsPlayer2++;
			
		board[line][column] = (state == ShipType.EMPTYHIDDEN) ? ShipType.EMPTY :      // N�o encontrou navio
							  (state == ShipType.TYPE1HIDDEN) ? ShipType.TYPE1SHOW :  // Encontrou Porta-avi�es
							  (state == ShipType.TYPE2HIDDEN) ? ShipType.TYPE2SHOW :  // Encontrou Navio-tanque
							  (state == ShipType.TYPE3HIDDEN) ? ShipType.TYPE3SHOW :  // Encontrou Contratorpedeiro
							  (state == ShipType.TYPE4HIDDEN) ? ShipType.TYPE4SHOW :  // Encontrou Submarino
							  board[line][column];
		
		return (state == ShipType.EMPTYHIDDEN) ? "Tiro no mar!" 
			 : (state == ShipType.TYPE1HIDDEN) ? "Porta-avioes atingido!"
			 : (state == ShipType.TYPE2HIDDEN) ? "Navio-tanque atingido!"
			 : (state == ShipType.TYPE3HIDDEN) ? "Contratorpedeiro atingido!"
			 : (state == ShipType.TYPE4HIDDEN) ? "Submarino atingido!"
			 : "Espaco ja explorado.";		 
	}

	public void randomShipPosition(int player) {
		
		String letters = "ABCDEFGHIJ";
		int[] shipSizes  = {5, 4, 3, 2};  // Dimens�o de cada navio
		int[] shipNumber = {1, 2, 3, 4};  // N�mero de navios a posicionar de cada tipo
		int[] shipSymbol = {ShipType.TYPE1HIDDEN, ShipType.TYPE2HIDDEN, ShipType.TYPE3HIDDEN, ShipType.TYPE4HIDDEN};

		for (int i = 0; i < 4; i++) {

			String position = "";  					 // Posi��o
			boolean vertical = false, check = false; // Sentido e verifica��o da posi��o
			int number = 0;		  					 // N�mero de navios por posicionar
			
			while(number != shipNumber[i]) {
			
				do {			
					String randomLine = String.valueOf(new Random().nextInt(10) + 1);
					char randomColumn = letters.charAt(new Random().nextInt(letters.length()));					
					if (randomLine.length() == 1) randomLine = "0" + randomLine;
					position = randomLine + randomColumn;		
	
					vertical = Math.random() < 0.5; 						      		  // Escolhe se o navio ser� posicionado verticalmente ou horizontalmente		
					check = checkValidPosition(player, position, shipSizes[i], vertical); // Verifica se a posi��o � v�lida
					
					// Caso a posi��o seja v�lida, o navio � introduzido
					if (check) {
						int steps  = 0;
						String strLine = "" + position.charAt(0) + position.charAt(1);
						
						int line     = Integer.parseInt(strLine) - 1;
						int column   = columnValue.get(position.charAt(2) + "");
						
						while(steps != shipSizes[i]) {		
							
							if (vertical) {
								if (player == 1) boardPlayer1[line++][column] = shipSymbol[i];
								if (player == 2) boardPlayer2[line++][column] = shipSymbol[i];
							}
							else {
								if (player == 1) boardPlayer1[line][column++] = shipSymbol[i];
								if (player == 2) boardPlayer2[line][column++] = shipSymbol[i];
							}
							steps++;
						}
						number++;
					}
				} while(!check);
			}		
		}
	}	
	
	public static void main(String[] args) {
		
		GameModel game = new GameModel();
		System.out.println(game.getBoardView(1));
	}

	public boolean checkValidPosition(int player, String position, int shipSize, boolean vertical) {

		int[][] board = (player == 1) ? boardPlayer1 : boardPlayer2;
		
		char line1  = position.charAt(0);
		char line2  = position.charAt(1);
		char column = position.charAt(2);
		
		// Verifica��o do sintaxe da posi��o
		if (Character.isDigit(line1) && Character.isDigit(line2)) {
			
			String line = "" + Character.getNumericValue(line1) + Character.getNumericValue(line2);
			int line_number = Integer.parseInt(line) - 1;
					
			// Verifica��o do sintaxe da posi��o
			if (Character.isLetter(column)) {

				int column_number = columnValue.get(column + "");
				int steps = 0;						
				while(steps != shipSize) {

					// Verifica��o - Dentro do tabuleiro
					if (line_number < 0 || line_number >= 10)     return false; // Erro: Navio fora do tabuleiro
					if (column_number < 0 || column_number >= 10) return false; // Erro: Navio fora do tabuleiro

					// Verifica��o - Colis�o com outro navio
					if (board[line_number][column_number] != 0)   return false; // Erro: Colis�o com outro navio.
					
					// Verifica��o - 1 Espa�o de afastamento (Cont�guos)
					if (!checkContiguous(board, line_number, column_number)) return false;
	
					// Verificar proxima posicao
					if (vertical) line_number++;
					else column_number++;			
					steps++;
				}			
				return true;
			}
		}	
		System.out.println("Erro: Sintaxe inv�lida");		
		return false;
	}

	
	
	private boolean checkContiguous(int[][] board, int lin, int col) {

		if (col != 0) {
			if (board[lin][col-1] != 0) return false;
			if (lin != 0) if (board[lin-1][col-1] != 0) return false;
			if (lin != 9) if (board[lin+1][col-1]	!= 0) return false;
		}
		
		if (col != 9) {
			if (board[lin][col+1] != 0) return false;
			if (lin != 0) if (board[lin-1][col+1] != 0) return false;
			if (lin != 9) if (board[lin+1][col+1]	!= 0) return false;
		}

		if (lin != 0) if (board[lin-1][col] != 0) return false;
		if (lin != 9) if (board[lin+1][col] != 0) return false;
		
		return true;
	}
	
	/*
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
