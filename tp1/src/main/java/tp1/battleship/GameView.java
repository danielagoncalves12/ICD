package tp1.battleship;

import java.util.HashMap;
import java.util.Map;

public class GameView {
	
	private Map<Integer, String> view = new HashMap<Integer, String>();

	/** 
	 * Método printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public String printBoard(int[][] board) {
		
		view.put(0, new String(" ")); // Por explorar (Sem navio)
	    view.put(1, new String(" ")); // Por explorar (Com navio)
	    view.put(2, new String("X")); // Pressionado (Sucesso)
	    view.put(3, new String(".")); // Pressionado (Sem sucesso)
		
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
				strBoard.append(" " + view.get(board[i][j]) + " ");
				strBoard.append((j == 9) ? "|" : "");
			}
			strBoard.append("\n");
		}
		
		// Apresentação da borda do tabuleiro
		strBoard.append("   X ");
		for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		return strBoard.toString();
	}
}
