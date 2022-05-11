package tp1.battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameView {
	
	private static Map<Integer, String> symbols = new HashMap<Integer, String>();

	public static void convertSymbols() {
		
		symbols.put(ShipType.EMPTYHIDDEN, new String(" ")); // Por explorar (Sem navio)
		symbols.put(ShipType.EMPTY, new String("."));       // Pressionado (Sem Sucesso)
	    
		symbols.put(ShipType.TYPE1SHOW, new String("P"));   // Porta-aviões encontrado
		symbols.put(ShipType.TYPE2SHOW, new String("N"));   // Navio-tanque encontrado
		symbols.put(ShipType.TYPE3SHOW, new String("C"));   // Contratorpedeiro encontrado
		symbols.put(ShipType.TYPE4SHOW, new String("S"));   // Submarino encontrado
	    
		symbols.put(ShipType.TYPE1HIDDEN, new String(" ")); // Porta-aviões escondido
		symbols.put(ShipType.TYPE2HIDDEN, new String(" ")); // Navio-tanque escondido
		symbols.put(ShipType.TYPE3HIDDEN, new String(" ")); // Contratorpedeiro escondido
		symbols.put(ShipType.TYPE4HIDDEN, new String(" ")); // Submarino escondido	
	}
	
	/** 
	 * Método printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public static String printBoard(String player, String view, int[][] board) {

		int pointsPlayer1 = 0;
		int pointsPlayer2 = 0;
		
		String intro = "";
		if (view.equals("true")) {
			intro = "\nTodos os navios foram posicionados aleatoriamente!\nTabuleiro do Jogador " + player + ":\n\n";
		}
		else {
			intro = "\nPontuacao atual - Jogador 1: " + pointsPlayer1 + " pontos, Jogador 2: " + pointsPlayer2 + " pontos." +
					"\nTabuleiro do jogador " + (player.equals("1") ? 2 : 1) + ":\n\n";
		}
		
		convertSymbols();
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		StringBuilder strBoard = new StringBuilder();

		// Apresentação das letras
		strBoard.append("    ");
		for (int i = 0; i < 10; i++) {
			strBoard.append(" " + letters[i] + " ");
		}
		strBoard.append("\n");

		// Apresentação da borda do tabuleiro
		strBoard.append("   X ");
		for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		
		// Apresentação do conteúdo do tabuleiro
		for (int i = 0; i < 10; i++) {
			
			strBoard.append(i+1 + " ");
			strBoard.append((i != 9) ? " |" : "|");

			for (int j = 0; j < 10; j++) { 
				strBoard.append(" " + symbols.get(board[i][j]) + " ");
				strBoard.append((j == 9) ? "|" : "");
			}
			strBoard.append("\n");
		}
		
		// Apresentação da borda do tabuleiro
		strBoard.append("   X ");
		for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		return intro + strBoard.toString();
	}
}
